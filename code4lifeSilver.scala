import math._               //given code
import scala.util._         //given code
import scala.io.StdIn._     //given code
import scala.collection.mutable.ArrayBuffer

object Player extends App {                                             //given code
    val projectCount = readLine.toInt                                   //given code
    for(i <- 0 until projectCount) {                                    //given code
        val Array(a, b, c, d, e) = (readLine split " ").map (_.toInt)   //given code
    }                                                                   //given code

    class Sample{ //Sample class that holds the following values/attributes
        var sampleId = 0
        var carriedBy = 0
        var rank = 0
        var health = 0
        var cost = Array.empty[Int]
        var diagnosed = false
        def initialize(a:Int, b:Int, c:Int, d:Int, e:Array[Int]) = { //method to initialize the values (I didn't use a constructor)
            sampleId = a
            carriedBy = b
            rank = c
            health = d
            cost = e
            diagnosed = health > 0 //true if we already know the health(points) of a sample
        }
    }

    var samplesLimit = 0 //variable usage exaplined below
    var submittableCount = 0 //variable usage exaplined below

    // game loop
    while(true) {
        var samples = new ArrayBuffer[Sample]() //samples array that will hold all samples carried by the robot

        var myStorage = Array.empty[Int] //array of molecules the robot currently holds
        var myExpertise = Array.empty[Int] //array of expertise the robotI currently has
        var myTarget = "" //the robot's position

        for(i <- 0 until 2) {                                                                                       //given code
            val Array(target, _eta, _score, _storageA, _storageB, _storageC, _storageD, _storageE, _expertiseA,     //given code
            _expertiseB, _expertiseC, _expertiseD, _expertiseE) = readLine split " "                                //given code
            val eta = _eta.toInt                                                                                    //given code
            val score = _score.toInt                                                                                //given code
            val storageA = _storageA.toInt                                                                          //given code
            val storageB = _storageB.toInt                                                                          //given code
            val storageC = _storageC.toInt                                                                          //given code
            val storageD = _storageD.toInt                                                                          //given code
            val storageE = _storageE.toInt                                                                          //given code
            val expertiseA = _expertiseA.toInt                                                                      //given code
            val expertiseB = _expertiseB.toInt                                                                      //given code
            val expertiseC = _expertiseC.toInt                                                                      //given code
            val expertiseD = _expertiseD.toInt                                                                      //given code
            val expertiseE = _expertiseE.toInt                                                                      //given code
            if (i == 0){ //if the index is 0, then it's me (my robot)
                myStorage = Array(storageA, storageB, storageC, storageD, storageE) //assign myStorage array's values
                myExpertise = Array(expertiseA, expertiseB, expertiseC, expertiseD, expertiseE) //assign myExpertise array's values
                myTarget = target //update the position
            }
        }
        val Array(availableA, availableB, availableC, availableD, availableE) = (readLine split " ").map (_.toInt)                          //given code
        val sampleCount = readLine.toInt                                                                                                    //given code
        for(i <- 0 until sampleCount) {                                                                                                     //given code
            val Array(_sampleId, _carriedBy, _rank, expertiseGain, _health, _costA, _costB, _costC, _costD, _costE) = readLine split " "    //given code
            val sampleId = _sampleId.toInt                                                                                                  //given code
            val carriedBy = _carriedBy.toInt                                                                                                //given code
            val rank = _rank.toInt                                                                                                          //given code
            val health = _health.toInt                                                                                                      //given code
            val costB = _costB.toInt                                                                                                        //given code
            val costA = _costA.toInt                                                                                                        //given code
            val costC = _costC.toInt                                                                                                        //given code
            val costD = _costD.toInt                                                                                                        //given code
            val costE = _costE.toInt                                                                                                        //given code

            val cost = Array(costA, costB, costC, costD, costE) //an array of this sample's molecule costs
            if(carriedBy == 0){ //if the sample is carried by my robot
                val sample = new Sample() //create, initialize and add this sample to the samples array
                sample.initialize(sampleId, carriedBy, rank, health, cost);
                samples += sample
            }
        }
        val available = Array(availableA, availableB, availableC, availableD, availableE) //array of currently available molecules at the Molecules module

        var samplesToCarry = 0 //deciding how many samples to carry at a time depending on the number of expertise
        if(myExpertise.sum<2) samplesToCarry = 2
        else samplesToCarry = 3

        //variable samplesLimit is used to allow the robot to fetch more samples when its hands are empty
        //stops it from fetching samples at each turn or whenever there is enough space to carry more
        if(samples.size == 0) samplesLimit = samplesToCarry 

        if (samplesLimit > 0 && samples.size < samplesToCarry){ //check if the robot can carry more samples
            if(myExpertise.sum < 5) // these are arbitrary rules/numbers to decide samples of what ranks to goto and connect tp
                gAndC("SAMPLES", 1, myTarget) //using gAndC function to goto the SAMPLES module and connect rank 1 samples
            else{
                if(samples.size > 0 && myExpertise.sum >= 5 && myExpertise.sum < 8)
                    gAndC("SAMPLES", 1, myTarget)

                else if(samples.size > 1 && myExpertise.sum >= 10) //different criteria to fetch rank 3 samples
                    gAndC("SAMPLES", 3, myTarget)

                else
                    gAndC("SAMPLES", 2, myTarget) // fetching rank 2 samples
            }
            samplesLimit-=1
        }
        else {
            var undiagnosedSampleId = -1
            def findUndiagnosed { //function to update undiagnosedSampleId variable if there's a sample from the array of samples that isn't diagnosed
                for(x <- 0 until samples.size){
                    if(!samples(x).diagnosed){
                        undiagnosedSampleId = samples(x).sampleId
                        return //exit the function whenever an undiagnosed sample is found
                    }
                }
            }
            findUndiagnosed //call the function above

            if(undiagnosedSampleId != -1){ //check if there'se a sample that we're carrying and needs to be diagnosed
                gAndC("DIAGNOSIS", undiagnosedSampleId, myTarget) //goto DIAGNOSIS module and connect to diagnose a sample
            }
            else{ //if all samples are diagnosed, we check which samples are ready-to-submit, and which molecules they need
                var submittableSamples = new ArrayBuffer[Sample]() //array to hold ready-to-submit samples(to connect at LABORATORY)
                def getSubmittableSample { //function to fill the above array, can be merged with the above function, but this way is clearer
                    for (x <- 0 until samples.size){
                        var fulfilledMolecules = 0
                        for(w <- 0 until 5){
                            if(samples(x).cost(w) <= (myStorage(w)+myExpertise(w))) //checks if the robot already has the needed molecules of a sample
                                fulfilledMolecules+=1
                        }
                        if(fulfilledMolecules==5){ //if, for this sample, number of fulfilled needed molecules are 5, then it's ready-to-submit/research
                            submittableSamples+=samples(x) //add sample to the array
                        }
                    }
                    if(submittableSamples.size>submittableCount) //variable submittableCount is used to store the number of ready-to-submit samples
                        submittableCount = submittableSamples.size //updated only when the current ready-to-submit samples' count is higher than that of the previous turn
                    else if(submittableSamples.isEmpty) //set its value to 0 if there are no ready-to-submit samples
                        submittableCount = 0
                }
                getSubmittableSample //call the function above
                
                var neededMolecule = 'Z'
                var neededMoleculeIndex = 0
                val molecules = Array('A','B','C','D','E')
                def getNeededMolecule {
                    for( x <- 0 until samples.size){
                        for( w <- 0 until 5){
                            if ((myStorage(w)+myExpertise(w)) < samples(x).cost(w) && x == 0){ //if the first sample needs more amounts of molecule at index w then,
                                neededMolecule = molecules(w) // update neededMolecule variable to hold the value of the missing molecule
                                neededMoleculeIndex = w // save its index for later use
                                return //exit the function
                            }
                            else if(x != 0 && ((myStorage(w)+myExpertise(w))-samples(x-1).cost(w)) < samples(x).cost(w)){ //if the robot is holding more than one sample then,
                                neededMolecule = molecules(w) //find out which molecule we need to carry more amounts of without counting the
                                neededMoleculeIndex = w //previous sample's molecule needs (allows for accumulating more molecules of different samples)
                                return
                            }
                        }
                    }
                }
                getNeededMolecule //call the function above
                
                //if the neededMolecule variable is updated(not its original value 'Z') it means there's a molecule missing
                if(neededMolecule != 'Z'){
                    //if myStorage(amount of molecules the robot is carrying) is less than the limit(10) and if there aren't at least 2 pending ready-to-submit samples
                    //and if the needed molecule is available at the MOLECULES module, then goto and connect
                    if( (myStorage.sum<10) && (submittableCount<2) && (available(neededMoleculeIndex) > 0))
                        gAndC("MOLECULES", neededMolecule, myTarget)
                    //otherwise if we have some ready-to-submit samples then submit at the LABORATORY module
                    else if(!submittableSamples.isEmpty)
                        gAndC("LABORATORY", submittableSamples(0).sampleId, myTarget)
                    //otherwise return the first sample the robot is carrying at the DIAGNOSIS module (it isn't ready-to-submit OR its needed molecule isn't available OR the robot is carrying 10 molecules(limit))
                    else gAndC("DIAGNOSIS", samples(0).sampleId, myTarget)
                }
                //if the neededMolecule's value is 'Z', it means that we don't any molecule and we can submit at the LABORATORY module
                else gAndC("LABORATORY", submittableSamples(0).sampleId, myTarget)
            }
        }
    }
    //function used to GOTO a module and CONNECT either a rank, molecule or sample ID
    def gAndC[A](module: String, info: A, position: String){
        if(position != module) println("GOTO "+module) //if the robot isn't at the requested module, then GOTO
        else println("CONNECT "+info) //otherwise connect
    }
}
