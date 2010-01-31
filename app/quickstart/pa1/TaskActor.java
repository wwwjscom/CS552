package app.quickstart.pa1;

import java.lang.Math;
import java.util.ArrayList;
import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class TaskActor extends Actor {

	int p, c, time, deadline = 0;
	int release_time, j = 1; // j: Message counter
	ActorName server_actor;
	
	public TaskActor(Integer p, Integer c, Integer time, ActorName server_actor) {
		this.p = p;
		this.c = c;
		this.time = time;
		this.server_actor = server_actor;
		send(getActorName(), "start");
	}
	
	public void start() {
		int i=0;
		while(i<5) {
			release_time 	= p + j * p;
			//deadline 		= get_random(p) + release_time; // To randomize the deadline, call this.
			deadline		= p + release_time;
			send(server_actor, "compute_lcm", get_random(500), get_random(500), release_time, c, deadline);
			i++;
			j++;
		}
	}
	
	public int get_random(int max) {
		return ((int)(Math.random() * max));
	}
}