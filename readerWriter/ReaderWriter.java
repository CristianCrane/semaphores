package concurrency_reader_writer;

import java.util.concurrent.Semaphore;

public class ReaderWriter 
{
	final static int bufferSize = 4;
	final static int totalData = 15;
	static Semaphore bufferFull = new Semaphore(0);
	static Semaphore bufferEmpty = new Semaphore(bufferSize);
	
	public static void main(String[] args) throws InterruptedException 
	{		
		int[] buffer = new int[bufferSize];
		
		// create threads
		Reader r = new Reader(buffer,totalData);
		Writer w = new Writer(buffer,totalData);
		
		// start threads
		r.start();
		w.start();
		
		// wait for threads to finish
		r.join();
		w.join();
		
		System.out.println("All data has been written.");
		
	}
}
