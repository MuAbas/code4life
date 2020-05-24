# code4life
code4life Silver League, in Scala

---

# Strategies
#### Wood 2 League:
`constraints:` The only constraints were that the robot can carry up to only 3 samples and 10 molecules, so the strategy was:
1. First, compare all samples and mark the sample of the highest health(points)
2. Go to the DIAGNOSIS module and get that sample
3. Find the needed molecule, go to the MOLECULES module and connect that molecule
4. If there isn't a needed molecule(the sample is ready for research), go to the LABORATORY module and connect the sample

#### Wood 1 League:
`New Constraints:` A new rule states that the samples now need to be diagnosed after acquiring them from the new SAMPLES module, plus samples have ranks now, the higher the rank the harder it is to research the sample.
The previous strategy didn't change much, except:
1. If the robot isn't carrying a sample, go to the SAMPLES module and connect a sample of an arbitrary rank between 1 and 3 (I chose rank 2)
2. If the robot is carrying a sample, go to DIAGNOSIS module and connect to diagnose the sample
3. Find the needed molecule and follow remaining steps as the first league

#### Bronze League:
`New Constraints:` Molecules are now limited in supply and expertise are unlocked. The only change in the strategy was:
* Expertise can be used instead of connecting new molecules. So the cost of researching a sample is decreased.
* Limited molecules didn't affect the result in this league.

#### Silver League:
`New Constraints:` Nothing new was added (I think) but the competition increased. So the new strategy was to increase the number of researches done by carrying multiple samples at the same time:
1. Number of samples carried in the first round should be 2, after that it's increased to 3
2. Ranks of samples start low and increase gradually with the increase of expertise
3. After obtaining these samples from the SAMPLES module, diagnose every one of them at the DIAGNOSE module
4. If possible, go to the MOLECULES module and connect all needed molecules for one of the diagnosed samples, after that, connect all needed molecules for the other samples until reaching the limit of molecules that can be carried (10)
5. If no more molecules can be carried/obtained and if there are at least 2 samples that can be researched, then go to the LABORATORY module and connect all fit samples
6. If no more molecules can be carried and no sample is fit to connect at the LABORATORY module, then return one of the samples to the cloud at the DIAGNOSE module
7. All samples (carried ones) should be either returned to the cloud at the DIAGNOSE module or connected at the LABORATORY module before acquiring new samples from the SAMPLES module
