package concurrency_mutex;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class MutexExample 
{

	static Semaphore mutex = new Semaphore(1,true);	
	static int ticketsAvailable = 30;
	
	public static void main(String[] args) throws InterruptedException 
	{
		ArrayList<TicketSeller> ts = new ArrayList<TicketSeller>();
		
		// create threads
		for (int i = 0; i < 5; i++)
		{
			TicketSeller t = new TicketSeller(i);
			ts.add(t);
		}
		
		// start threads
		for (TicketSeller t : ts) t.start();
		
		// join threads
		for (TicketSeller t : ts) t.join();
		
		System.out.println("Done!");
		
	}
}
