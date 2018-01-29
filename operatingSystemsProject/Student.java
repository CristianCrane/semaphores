import java.util.Random;

// each student should take two exams.
public class Student extends Thread
{
	Random rand = new Random();
	
	int id;
	Professor prof;
	Classroom classroom;
	long time;
	
	public Student(int id, Professor p, Classroom c, long t)
	{
		this.id = id;
		setName("Student-" + id);
		prof = p;
		classroom = c;
		time = t;
	}
	
	@Override
	public void run() 
	{
		arrive();
		
		waitForProfessor();
		
		// exam 1
		enterClassroom();
		takeExam(); 
		takeBreak();
		
		// exam 2
		enterClassroom();
		takeExam(); 
		waitForGrades();
		
		// join other students
		for (int i = (this.id) - 1; i >= 0; i--)
		{
			try { prof.students[i].join();} catch (InterruptedException e) {}
		}
		
		leave();
	}
	
	public void arrive()
	{
		try {
			sleep(rand.nextInt(5000));
		} catch (InterruptedException e) {
			System.out.println("Arrival was interrupted");
		}
		msg("Arrived");
	}
	
	public void waitForGrades()
	{
		while (!prof.hasPostedGrades()) {} // busywait
	}
	
	public void enterClassroom()
	{
		setPriority(Thread.MAX_PRIORITY);
		try 
		{
			sleep(10);
		} 
		catch (InterruptedException e) 
		{
			msg("Interrupted from enterClassroom()");
		}
		setPriority(Thread.NORM_PRIORITY);
		
		if (classroom.testInProgress() || classroom.isFull())
		{
			msg("Missed exam");
			yield();
			yield();
			while (classroom.testInProgress() || classroom.isFull()){} 
		}
		
		classroom.enterRoom(this);
	}
	
	public void takeExam()
	{
		try 
		{
			sleep(999999);
		} 
		catch (InterruptedException e) 
		{
			
		}
	}
	
	public void takeBreak()
	{	
		msg("On break");
		try {
			sleep(rand.nextInt(5000));
		} catch (InterruptedException e) {
			msg("Break interrupted");
		}
		msg("Break over");
	}
	
	public void waitForProfessor()
	{
		while (!prof.hasArrived()) {} // busywait
	}
	
	public void leave()
	{
		msg("Left school");
	}
	
	public void msg(String m) 
	{
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
}
