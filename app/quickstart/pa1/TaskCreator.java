package app.quickstart.pa1;

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
	ActorName server_actor;

	public TaskCreator(ActorName server_actor) {
		this.server_actor = server_actor;
		init();
	}
	
	/* Creates & starts the 10 task actors we need */
	public void init() {
		int i;
		for(i=0;i<10;i++) {
			createTaskActor();
		}
	}	
	
	public void createTaskActor() {
		ActorName actor_name = null;
		
		/* Create task-actor */
	    try {
			Integer p = ((int)(Math.random() * 5));
			Integer c = ((int)(Math.random() * p));
			actor_name = create("app.quickstart.pa1.TaskActor", p, c, time, server_actor);
			TAs.add(actor_name);
		} catch (CreateActorException e) {
    	    System.out.println("> TaskCreator.createTaskActor: " + e); 
		}
		
		/* Setup the p and c values, and time */

	}
	
}
