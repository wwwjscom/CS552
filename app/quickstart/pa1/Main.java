package app.quickstart.pa1;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class Main extends Actor {

	public Main() {
		try {
			ActorName server_actor = create("app.quickstart.pa1.ServerActor");
			create("app.quickstart.pa1.TaskCreator", server_actor);
		} catch (CreateActorException e) {
    	    System.out.println("> Main.main: " + e); 
		}
	}
}
