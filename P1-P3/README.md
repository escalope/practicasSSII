# AI-Vacuum-World

A variation of the Vacuum World problem reusing the AIMA framework published in Maven. The sources have been simplified and refactored to be more OO, though more work is needed, yet. There is one main app (ssii.p1.VacuumApp) and
two tests to show how agent functionality can be checked. The material is part of the Intelligent Systems subject from FDI faculty at UCM.

Run first  
	mvn compile

Then run the main app with
	mvn exec:java -Dexec.mainClass=ssii.p1.VacuumApp
This should run a simple problem solving agent looking for dirt and with perfect information about the environment. The execution is continous, adding dirt as the vacuum agent is cleaning it.

Tests are included as an example of how to check the agent behaviors. They can be run with
	mvn test

No output will be generated, unless there is something wrong. To run the tests with output, they can be executed from command line with:

	mvn exec:java -Dexec.mainClass=ssii.p1.BreadthFirstSearchTest -Dexec.classpathScope="test"
and
	mvn exec:java -Dexec.mainClass=ssii.p1.DepthFirstSearchTest -Dexec.classpathScope="test"
