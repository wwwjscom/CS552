package app.quickstart.pa1;

import java.math.BigInteger;
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
}
