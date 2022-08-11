package onlineTest;

import java.util.HashMap;
import java.util.Map;

public class Exam {

	int examId;
	String examTitle;
	
	
	HashMap<Integer, Question> questions = new HashMap<>();
	
	public Exam (int id, String title) {
		
		this.examId = id;
		this.examTitle = title;
		
	}
	
	public void addTrueFalseQuestion(int quesNo, String text, double points, boolean answer) {
			
		questions.put(quesNo, new Question(quesNo, text, points, answer));
		
	
		
	}
	
	public void addMultipleChoiceQuestion(int quesNo, String text, double points, String[] answer) {
		
		questions.put(quesNo, new Question(quesNo, text, points, answer));
		
	}
	
	public void addFillInTheBlanksQuestion(int quesNo, String text, double points, String[] answer) {
		
		questions.put(quesNo, new Question(quesNo, text, points, answer));
		
		
	}
	
	public void passInQuestions(HashMap<Integer, Question> pass) {
		
		for(Map.Entry<Integer, Question> entry : pass.entrySet()) {
			
			int num = entry.getKey();
			String text = new String(entry.getValue().question);
			double points = entry.getValue().points;
			
			
			
			questions.put(num,new Question(num, text, points, entry.getValue().answer));
			
		}
		
		
	}
	
	public int getTotalPoints() {
		
		int returner = 0;
		
		for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
			
			returner += entry.getValue().getPoints();
			
		}
		
		return returner;
		
	}
	
	
	
}
