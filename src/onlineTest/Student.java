package onlineTest;

import java.io.Serializable;
import java.util.HashMap;

public class Student{

	String name;
	HashMap<Integer, Exam> exams = new HashMap<>();
	
	public Student(String name, HashMap<Integer, Exam> exams) {
		
		this.name = name;
		this.exams = exams;
		
	}
	
	
	
}
