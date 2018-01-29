package concurrency_mutex;

import java.util.Random;

public class TicketSeller extends Thread
{
	int tixSold;
	boolean soldOut;
	Random rand = new Random();
	
	public TicketSeller(int i)
	{
		setName("Vendor-"+i);
		tixSold = 0;
		soldOut = false;
	}
	
	public void run()
	{
		while (!soldOut)
		{
			// simulate talking to customer
			randWait();
			
			// wait for lock on tix
			getLock();
			
			// if there are still tix available
			if (MutexExample.ticketsAvailable != 0)
			{
				MutexExample.ticketsAvailable--;
				tixSold++;
				msg("I sold a ticket! ("+MutexExample.ticketsAvailable+" left)");
				// release lock on tickets
				releaseLock();
			}

			// else were sold out
			else
			{
				soldOut = true;
				releaseLock();
			}
			
		}
		
		msg("Tickets are sold out. I sold "+tixSold+" tickets today!");
		
	}
	
	void getLock()
	{
		// aquire lock on tickets
		try 
		{ 
			MutexExample.mutex.acquire(); 
		} 
		catch (InterruptedException e) 
		{
			// just continue
		}
	}
	
	void releaseLock()
	{
		MutexExample.mutex.release();
	}
	
	void randWait()
	{
		try {
			sleep(rand.nextInt(2000));
		} catch (InterruptedException e) {

		}
	}
	
	public void msg(String s)
	{
		System.out.println("["+getName() + "]: " + s);
	}
}
