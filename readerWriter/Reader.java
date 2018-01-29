package concurrency_reader_writer;

import java.util.Random;

public class Reader extends Thread
{
	int[] buffer;
	int rIndex;
	int totalData;
	Random rand = new Random();
	
	public Reader(int[] buffer, int totalData)
	{
		setName("Reader");
		this.buffer = buffer;
		this.totalData = totalData;
		rIndex = 0;
	}
	
	public void run()
	{
		for (int i = 0; i < totalData; i++)
		{
			// wait for a full buffer
			waitForFullBuffer();
			
			// Read from the next available buffer
			readData();
			
			// signal a buffer is available to write to
			signal();
		}
		
		
	}
	
	void waitForFullBuffer()
	{
		try 
		{ 
			ReaderWriter.bufferFull.acquire(); 
		} 
		catch (InterruptedException e) 
		{
			// just continue
		}
	}
	
	void signal()
	{
		ReaderWriter.bufferEmpty.release();
	}
	
	void readData()
	{
		// simulate processing the data by randWait
		randWait();
		
		// simulate 'reading' by just getting the value and printing it to console
		int availableIndex = rIndex % buffer.length;
		msg("Buffer["+availableIndex+"] = "+buffer[availableIndex]);
		rIndex++;
		
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
		System.out.println("   ["+getName() + "]: " + s);
	}
}
