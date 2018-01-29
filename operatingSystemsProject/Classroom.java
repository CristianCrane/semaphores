import java.util.LinkedList;
import java.util.Queue;

public class Classroom 
{
	volatile boolean isFull;
	volatile boolean testInProgress;
	Queue<Student> room;
	int maxCapacity;
	int studentCount;
	
	public Classroom(int maxCap)
	{
		isFull = false;
		testInProgress = false;
		maxCapacity = maxCap;
		studentCount = 0;
		room = new LinkedList<Student>();
	}
	
	public synchronized void enterRoom(Student s)
	{
		room.add(s);
		s.msg("Entered classroom");	
	}
	
	public synchronized Student leaveRoom()
	{
		setStudents(getStudents()-1);
		Student s = room.remove();
		s.msg("Left classroom");

		return s;
	}
	
	public synchronized boolean isFull()
	{
		if (getStudents() == maxCapacity)
		{
			return true;
		}
		else
		{
			setStudents(getStudents()+1);
			return false;
		}
	}
	
	public synchronized boolean testInProgress()
	{
		return testInProgress;
	}
	
	public synchronized boolean hasStudents()
	{
		return !(room.isEmpty());
	}

	public synchronized int getStudents()
	{
		return studentCount;
	}
	
	public synchronized void setStudents(int s)
	{
		studentCount = s;
	}
}
