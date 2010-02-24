package app.quickstart.pa1;

import java.math.BigInteger;
import java.util.ArrayList;

import aa.core.Actor;
import aa.core.ActorName;
import aa.core.CommunicationException;
import aa.core.CreateActorException;


public class ServerActor extends Actor {

	int time, dropped, processed = 0;
	
	public ServerActor() {
		//send(getActorName(), "start");
	}
	
	/* Calls for LCM to be computed if we can
	 * meet the deadline, otherwise it drops
	 * the request
	 */
	public void compute_lcm(Integer a, Integer b, Integer release_time, Integer c, Integer deadline) {

		if(deadline >= time + c) {
			// Increment the clock by the computation time
			time = time + c;
			
			// OK to process
			String sa = ""+a;
			String sb = ""+b;
			lcm(sa, sb);
			processed += 1;
		} else {
			// Drop the message
			dropped += 1;
		}
		println("Dropped: " + dropped + "\tProcessed: " + processed);
	}
	
	/*
	 * Computes the LCM
	 */
	public void lcm(String a, String b) {
		BigInteger big_a = new BigInteger(a);
		BigInteger big_b = new BigInteger(b);
		
		BigInteger numerator = big_a;
		BigInteger denominator = big_a.gcd(big_b);
		BigInteger lcm = (numerator.divide(denominator)).multiply(big_b);
	}
	
	
	/* 
	 * Loop over an array of tasks, processing the EDF task.
	 * Allow for preemption.
	 */
	public void edf(ArrayList<Task> tasks) {		
		while(!tasks.isEmpty()) {
			Task curr_task = null;

			if(TYPE == "RMS")
				 curr_task = next_task_rms(tasks, time);
			else if(TYPE == "EDF")
				 curr_task = next_task_rms(tasks, time);

			if(curr_task == null && !tasks.isEmpty()) {
				// No tasks have been released that are ready to run,
				// increment time and start over.
				time++;
				continue;
			} else if(curr_task == null && tasks.isEmpty()) {
				System.out.println("curr_task is null, returning.");
				break; // No more tasks left
			}
			
			/* Drop all tasks whose deadline has been missed */
			while(curr_task.d < time) {
				dropped++;
				tasks.remove(curr_task);
				if(TYPE == "RMS")
					curr_task = next_task_rms(tasks, time);
				else if(TYPE == "EDF")
					curr_task = next_task_rms(tasks, time);
			}
			
			/* Process one time unit of work on the task */
			if(useIP)
				curr_task.c(curr_task.c() -2);
			else
				curr_task.c(curr_task.c() -1);
			
			if(curr_task.c() <= 0) {
				processed++;
				tasks.remove(curr_task);
			}
			
			time++;
			println("(" + time + ") Dropped: " + dropped + "\tProcessed: " + processed);
			System.out.println("Tasks size on the client: " + tasks.size());
		}
		println("(" + time + ") Dropped: " + (dropped + tasks.size()) + "\tProcessed: " + processed);
	}
	
	
	/* 
	 * Return the next task, earliest deadline first.
	 */
	public Task next_task_edf(ArrayList<Task> tasks, int curr_time) {
		Task next = null;
		int min = 999999;
		for(int i=0; i<tasks.size();i++) {
			Task curr = tasks.get(i);
			//System.out.println("r: " + curr.r() + " d: " + curr.d());
			if(time >= curr.r() && curr.d() < min) {
				min = curr.d();
				//System.out.println("New min: " + min);
				next = curr;
			}
		}
		return next;
	}
	
	
	/* 
	 * Return the next task, based on period (RMS).
	 */
	public Task next_task_rms(ArrayList<Task> tasks, int curr_time) {
		Task next = null;
		int min = 999999;
		for(int i=0; i<tasks.size();i++) {
			Task curr = tasks.get(i);
			if(time >= curr.r() && curr.t() < min) {
				min = curr.t();
				//System.out.println("New min: " + min);
				next = curr;
			}
		}
		return next;
	}
	
	
	ActorName server_actor;
	Integer p = 0, c = 0; // period and computation time
	
	
	public void initEDF() throws CreateActorException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		int i, time = 0;
		for(i=0;i<10;i++) {
			ArrayList<Task> t = makeTasks(time);
			while(!t.isEmpty())
				tasks.add(t.remove(t.size()-1));
			time++;
		}
		System.out.println("Tasks size on the client: " + tasks.size());
		edf(tasks);
	}	
	
	
	
	// -1 for random p/c values.  Or set 2, 4, etc for
	// such a server-actors utilization (where the number XY represents X.Y utilization)
	// If Y is not set, then the form becomes 0.X
	int SIMULATE = 5;
	String TYPE = "RMS"; // Either RMS of EDF (hw2, question 1 & 2)
	boolean useIP = true; // Use Imprecise Computation (hw2, question 2)
	
	/*
	 * Returns 3 tasks in an ArrayList
	 */
	public ArrayList<Task> makeTasks(int i) {
		ArrayList<Task> t = new ArrayList<Task>();
    	switch(SIMULATE) {
    	case 1:
    		t.add(new Task(1, 4, 4*i, 4*i+4));
    		t.add(new Task(1, 6, 6*i, 6*i+6));
    		t.add(new Task(1, 20, 20*i, 20*i+20));
    		return t;
    	case 3:
    		t.add(new Task(1, 4, 4*i, 4*i+4));
    		t.add(new Task(3, 6, 6*i, 6*i+6));
    		t.add(new Task(5, 20, 20*i, 20*i+20));
    		return t;
    	case 5:
    		t.add(new Task(1, 4, 4*i, 4*i+4));
    		t.add(new Task(3, 6, 6*i, 6*i+6));
    		t.add(new Task(11, 20, 20*i, 20*i+20));
    		return t;
    	case 8:
    		t.add(new Task(2, 4, 4*i, 4*i+4));
    		t.add(new Task(5, 6, 6*i, 6*i+6));
    		t.add(new Task(17, 20, 20*i, 20*i+20));
    		return t;
    	case 10:
    		t.add(new Task(4, 4, 4*i, 4*i+4));
    		t.add(new Task(6, 6, 6*i, 6*i+6));
    		t.add(new Task(20, 20, 20*i, 20*i+20));
    		return t;
    	case 15:
    		t.add(new Task(5, 4, 4*i, 4*i+4));
    		t.add(new Task(10, 6, 6*i, 6*i+6));
    		t.add(new Task(30, 20, 20*i, 20*i+20));
    		return t;
    	}
		return t;
	}
	
	
}
