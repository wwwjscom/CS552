package app.quickstart.pa1;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class Main extends Actor {

	ActorName task_creator, server_actor;
	
	public Main() {
		try {
			server_actor = create("app.quickstart.pa1.ServerActor");
			
			// Use this line for EDF (PA1, #1)
			server_actor = create("app.quickstart.pa1.ServerActor");
			send(server_actor, "initEDF");
			
			// Use this line for PA1
			//task_creator = create("app.quickstart.pa1.TaskCreator", server_actor);
		} catch (CreateActorException e) {
    	    System.out.println("> Main.main: " + e); 
		}
	}
}
