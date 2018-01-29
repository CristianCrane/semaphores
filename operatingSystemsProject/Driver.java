import java.util.Scanner;

/* cs340 - Operating Systems Project 1: Exam Day
 * Written by: Cristian Crane for Spring 2017 term @ Queens College
 * 
 * This program implements Threads to simulate a professor administering a test.
 * Each student is a thread attempting to get into a classroom of fixed size.
 * The professor administers three tests, each student must take two.
 * The purpose of this project is to understand concurrency issues when synchronizing multiple threads.
 */
public class Driver 
{
	static int numOfStudents;
	static int classCapacity;
	static int groupSize;
	
	public static void main(String[] args) 
	{
		long time = System.currentTimeMillis();
		
		if (args.length < 3)
		{
			System.out.println("Not enough arguments. Expecting # of students, class capacity, and group size of students.");
		}
		else
		{
			numOfStudents = Integer.parseInt(args[0]);
			classCapacity = Integer.parseInt(args[1]);
			groupSize = Integer.parseInt(args[2]);
			
			validateInput();
			
			Classroom c = new Classroom(classCapacity);
			Professor p = new Professor(c,numOfStudents,time);
			
			p.start(); // professor will start his students as well
		}
	}
	
	public static void validateInput()
	{
		Scanner in = new Scanner(System.in);
		while ( classCapacity * 3 < numOfStudents * 2 )
		{
			int numTests = numOfStudents * 2;
			int testsAvailable = classCapacity * 3;
			 
			System.out.println("If there are " + numOfStudents + " students trying to take " + numTests + " tests, but only " + testsAvailable + " are avilable, then not all students will be able to take a test!");
			System.out.println("Please enter larger class Capacity or reduce the number of students. (Alternatively Press Q to quit)");
			
			System.out.println("Number of students: ");
			String numStudents = in.next();
			
			if (numStudents.equalsIgnoreCase("Q"))
			{
				System.out.println("Program terminated.");
				System.exit(0);
			}
			
			numOfStudents = Integer.parseInt(numStudents);
			
			System.out.println("Class capacity: ");
			classCapacity = in.nextInt();
		}
		in.close();
	}

}
