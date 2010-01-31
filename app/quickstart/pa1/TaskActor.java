package app.quickstart.pa1;

import java.lang.Math;
import java.util.ArrayList;
import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class TaskActor extends Actor {

	int p, c, time, release_time, deadline = 0;
	int j = 1; // Message counter
	ActorName server_actor;
	
	public TaskActor(Integer p, Integer c, Integer time, ActorName server_actor) {
		this.p = p;
		this.c = c;
		this.time = time;
		send(getActorName(), "start");
	}
	
	public void start() {
		int i=0;
		while(i<5) {
			release_time = release_time + j * p;
			deadline = p;
			send(server_actor, "lcm", release_time, c, deadline);
			i++;
			j++;
		}
	}
}
