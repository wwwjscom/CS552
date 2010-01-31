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
	int time = 0; // Logical time 

	public TaskCreator() {
		System.out.println(" Hello");
	}
	
	/* Creates & starts the 10 task actors we need */
	public void init() {
		int i;
		for(i=0;i<10;i++) {
			createTaskActor();
			startTaskActor(TAs.get(i));
		}
	}
	
	
	
	public void startTaskActor(ActorName actor_name) {
		try {
			call(actor_name, "start");
		} catch (CommunicationException e) {
    	    System.out.println("> TaskCreator.startTaskActor: " + e); 
		}
	}
	
	
	public void createTaskActor() {
		ActorName actor_name = null;
		
		/* Create task-actor */
	    try {
			actor_name = create("app.pa1.taskactor.World");
			TAs.add(actor_name);
		} catch (CreateActorException e) {
    	    System.out.println("> TaskCreator.createTaskActor: " + e); 
		}
		
		/* Setup the p and c values, and time */
		int p = ((int)(Math.random()) * 5);
		int c = ((int)(Math.random()) * p);
		try {
			call(actor_name, "setP", p);
			call(actor_name, "setC", c);
			call(actor_name, "setTime", time);
		} catch (CommunicationException e) {
    	    System.out.println("> TaskCreator.createTaskActor: " + e); 
		}

	}
	
}
