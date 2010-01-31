package app.pa1;

import java.lang.Math;
import java.util.ArrayList;
import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class TaskActor extends Actor {

	int p, c, time;
	
	
	public void start() {
		// While loop sending out msgs to server
		//send(server_actor, "lcm", )
	}
	
	public void setP(int p)
	{
		this.p = p;
	}
	
	public void setC(int c)
	{
		this.c = c;
	}
	
	public void setTime(int time)
	{
		this.time = time;
	}
}
