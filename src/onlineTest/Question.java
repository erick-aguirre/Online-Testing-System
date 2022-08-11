package onlineTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Question <T>{

	
	int quesNo;
	String question;
	T answer;
	double points;
	T studentAnswer;
	
	public Question(int quesNo, String text, double points, T answer){
		
		this.quesNo = quesNo;
		this.question = text;
		this.points = points;
		this.answer = answer;
		
		
	}
	
	public String getText() {
		
		return this.question;
		
	}
	
	public double getPoints() {
		
		return this.points;
		
	}
	
	public T getAnswer() {
		
		return this.answer;
		
	}
	
	public void setAnswer(T answer) {
		
		this.studentAnswer = answer;
		
	}
	
	public double pointsEarned() {
		
		if (studentAnswer.getClass() == Boolean.class) {
			
			if (studentAnswer == answer) {
				
				return points;
				
			}
			
		}
		else {
			
			String [] arrayStu = (String[]) studentAnswer;
			
			String [] correct = (String []) answer;
			
			if(Arrays.equals(arrayStu, correct)) {
				
				return points;
				
			}
			else {
				
				if(arrayStu[0].length() == 1) {
					
					return 0;
					
				}
				else {
					
					ArrayList<String> studentAns = new ArrayList<>(Arrays.asList(arrayStu));
					ArrayList<String> correctAns = new ArrayList<>(Arrays.asList(correct));
					double earned = 0;
					
					double perPoint = points/correct.length;
					
					for (int i = 0; i < arrayStu.length; i++) {
						
						if (correctAns.contains(arrayStu[i])) {
							
							earned += perPoint;
							
						}
						
					}
					
					
					return earned;
					
				}
				
			}
			
		}
		
		return 0;
		
	}
	
	
}
