package concurrency_reader_writer;

import java.util.Random;

public class Writer extends Thread
{
	int[] buffer;
	int wIndex;
	int totalData;
	Random rand = new Random();
	
	public Writer(int[] buffer, int totalData)
	{
		setName("Writer");
		this.buffer = buffer;
		this.totalData = totalData;
		wIndex = 0;
	}
	
	public void run()
	{
		for (int i = 0; i < totalData; i++)
		{
			// simulate processing data
			randWait();
			
			// wait on semaphore
			waitForEmptyBuffer();
			
			// write to the next available buffer
			writeData();
			
			// signal a buffer is available to read from
			signal();
		}
	}
	
	void waitForEmptyBuffer()
	{
		try 
		{ 
			ReaderWriter.bufferEmpty.acquire(); 
		} 
		catch (InterruptedException e) 
		{
			// just continue
		}
	}
	
	void signal()
	{
		ReaderWriter.bufferFull.release();
	}
	
	void randWait()
	{
		try {
			sleep(rand.nextInt(2000));
		} catch (InterruptedException e) {

		}
	}
	
	int getData()
	{
		return rand.nextInt(100) + 1; // return some random data
	}
	
	void writeData()
	{
		int availableIndex = wIndex % buffer.length;
		buffer[availableIndex] = getData();
		msg("Buffer["+availableIndex+"] = "+buffer[availableIndex]);
		wIndex++;
	}
	
	public void msg(String s)
	{
		System.out.println("["+getName() + "]: " + s);
	}
}
