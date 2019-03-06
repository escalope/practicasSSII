# CSP examples

A couple of examples for CSP reusing the AIMA framework published in Maven. The sources have been simplified and refactored to be more OO, though more work is needed, yet


Run first:  

	mvn compile

To run the australia map colouring demo
	
	mvn exec:java -Dexec.mainClass=ssii.p4.map.MapColoringCSPDemo
	
To run the wedding seat assignment problem

	mvn exec:java -Dexec.mainClass=ssii.p4.wedding.WeddingCSPV2
	mvn exec:java -Dexec.mainClass=ssii.p4.wedding.WeddingCSP
	

