package app.quickstart.pa1;

import java.lang.Math;
import java.util.ArrayList;
import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;

public class TaskCreatorEDF extends Actor {

	// -1 for random p/c values.  Or set 2, 4, etc for
	// such a server-actors utilization (where the number XY represents X.Y utilization)
	// If Y is not set, then the form becomes 0.X
	int SIMULATE = 2;
	
	ActorName server_actor;
	Integer p = 0, c = 0; // period and computation time
	
	
	public TaskCreatorEDF(ActorName server_actor) throws CreateActorException {
		this.server_actor = server_actor;
		init();
	}
	
	public void init() throws CreateActorException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		int i, time = 0;
		for(i=0;i<10;i++) {
			ArrayList<Task> t = makeTasks(time);
			while(!t.isEmpty())
				tasks.add(t.remove(t.size()-1));
			time += 15;
		}
		System.out.println("Tasks size on the client: " + tasks.size());
		send(server_actor, "edf", tasks);
	}	
	
	/*
	 * Returns 3 tasks in an ArrayList
	 */
	public ArrayList<Task> makeTasks(int i) {
		ArrayList<Task> t = new ArrayList<Task>();
    	switch(SIMULATE) {
    	case 2:
    		t.add(new Task(1, 4, 4*i, 4*i+4));
    		t.add(new Task(3, 6, 6*i, 6*i+6));
    		t.add(new Task(6, 15, 15*i, 15*i+15));
    		return t;
    	}
		return t;
	}
	
}
