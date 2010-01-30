package app.pa1;

import java.lang.Math;
import java.util.ArrayList;
import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class TaskCreator extends Actor {

	int NUM_OF_TA = 10; // Number of task actors to create
	ArrayList<ActorName> TAs = new ArrayList<ActorName>();	// Array to track our TAs

	
	public void createTaskActor() {
		int p = ((int)(Math.random()) * 5);
		int c = ((int)(Math.random()) * p);
		
		// Create task-actor and pass in p and c
	    try {
			ActorName actor_name = create("app.pa1.taskactor.World");
			TAs.add(actor_name);
		} catch (CreateActorException e) {
    	    System.out.println("> TaskCreator.createTaskActor: " + e); 
		}

	}
	
}
