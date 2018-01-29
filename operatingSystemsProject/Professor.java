import java.util.Random;

// professor administers 3 exams
public class Professor extends Thread 
{
	Random rand = new Random();
	long time;
	volatile boolean arrived;
	volatile boolean gradesPosted;
	Classroom classroom;	
	Student[] students;
	int[][] grades;
	
	public Professor(Classroom c, int numOfStudents, long t)
	{
		setName("Professor");
		classroom = c;
		time = t;
		students = getStudents(numOfStudents);
		grades = new int[students.length][3];
		arrived = false;
		gradesPosted = false;
	}
	
	@Override
	public void run() 
	{
		arrive();
		
		admitStudents(1);
		startTest(1);
		endTest(1);
		takeBreak();
		
		admitStudents(2);
		startTest(2);
		endTest(2);
		takeBreak();
		
		admitStudents(3);
		startTest(3);
		endTest(3);
		
		displayGrades();
		
		for (Student s: students)
		{
			try { s.join();} catch (InterruptedException e) {}
		}
		
		leave();
	}

	public void arrive()
	{
		try 
		{
			sleep(rand.nextInt(5000));
		} 
		catch (InterruptedException e) 
		{
			System.out.println("Arrival was interrupted");
		}
		
		msg("Arrived");
		arrived = true;
	}
	
	public void admitStudents(int i)
	{
		msg("Test " + i + " starting soon..");
		classroom.testInProgress = false;
		
		try 
		{
			sleep(5000);
		} 
		catch (InterruptedException e) 
		{
			msg("interrupted from admitStudents()");
		}
	}

	public void startTest(int i)
	{
		msg("Test " + i + " started");
		classroom.testInProgress = true;
		
		try 
		{
			sleep(10000);
		} 
		catch (InterruptedException e) 
		{
			msg("interrupted from startTest()");
		}
	}
	
	public void endTest(int i)
	{
		msg("Test " + i + " over");
		
		while (classroom.hasStudents())
		{
			Student s = classroom.leaveRoom();
			giveGrade(s,i);
			s.interrupt();	
		}
	}
	
	public void giveGrade(Student s, int test)
	{
		int grade = rand.nextInt(90) + 10;
		grades[s.id][test-1] = grade;
	}
	
	public void takeBreak()
	{	
		msg("On break");
		
		try 
		{
			sleep(rand.nextInt(5000));
		} 
		catch (InterruptedException e) 
		{
			msg("Break interrupted");
		}
		
		msg("Break over");
	}
	
	public void displayGrades()
	{
		msg("Grades for all students:");
		for (int student = 0; student < students.length; student++)
		{
			System.out.print("Student-" + student + ": ");
			for (int grade = 0; grade < 3; grade++)
			{
				System.out.print(grades[student][grade]);
				if (grade != 2)
					System.out.print(", ");
			}
			System.out.println();
		}
		
		gradesPosted = true;
	}
	
	public void leave()
	{
		msg("All students have left. Going home.");
	}
	
	private Student[] getStudents(int numOfStudents)
	{
		Student[] s = new Student[numOfStudents];
		
		for (int i = 0; i < numOfStudents; i++)
		{
			s[i] = new Student(i,this,classroom,time);
		}
		
		return s;
	}
	
	public void start()
	{
		// start the professor
		super.start();
		
		// start all his students
		for (Student s: students) s.start();
	}
	
	public synchronized boolean hasArrived()
	{
		return arrived;
	}
	
	public synchronized boolean hasPostedGrades()
	{
		return gradesPosted;
	}
	
	public void msg(String m) 
	{
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
}
