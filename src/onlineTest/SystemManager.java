package onlineTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SystemManager implements Manager {

	HashMap<String, Student> students = new HashMap<>();
	HashMap<Integer, Exam> exams = new HashMap<>();

	String[] cutOffs = { "A", "B", "C", "D", "F" };
	double[] doubCut = { 90, 80, 70, 60, 0 };

	@Override
	public boolean addExam(int examId, String title) {

		if (exams.containsKey(examId) == true) {

			return false;

		}

		exams.put(examId, new Exam(examId, title));

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			entry.getValue().exams.put(examId, new Exam(examId, title));

		}

		return true;

	}

	@Override
	public void addTrueFalseQuestion(int examId, int questionNumber, String text, double points, boolean answer) {

		exams.get(examId).addTrueFalseQuestion(questionNumber, text, points, answer);

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			entry.getValue().exams.get(examId).addTrueFalseQuestion(questionNumber, text, points, answer);

		}

	}

	@Override
	public void addMultipleChoiceQuestion(int examId, int questionNumber, String text, double points, String[] answer) {

		exams.get(examId).addMultipleChoiceQuestion(questionNumber, text, points, answer);

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			entry.getValue().exams.get(examId).addMultipleChoiceQuestion(questionNumber, text, points, answer);

		}

	}

	@Override
	public void addFillInTheBlanksQuestion(int examId, int questionNumber, String text, double points,
			String[] answer) {

		exams.get(examId).addFillInTheBlanksQuestion(questionNumber, text, points, answer);

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			entry.getValue().exams.get(examId).addMultipleChoiceQuestion(questionNumber, text, points, answer);

		}

	}

	@Override
	public String getKey(int examId) {

		if (!exams.containsKey(examId)) {

			return "Exam not Found";

		}

		TreeMap<Integer, Question> questions = new TreeMap<>();

		for (Map.Entry<Integer, Question> entry : exams.get(examId).questions.entrySet()) {

			questions.put(entry.getKey(), entry.getValue());

		}

		String key = "";

		for (Map.Entry<Integer, Question> entry : questions.entrySet()) {

			key += "Question Text: " + entry.getValue().getText() + "\n";

			key += "Points: " + entry.getValue().getPoints() + "\n";

			if (entry.getValue().getAnswer().getClass().isArray()) {

				String[] ans = (String[]) entry.getValue().getAnswer();

				Arrays.sort(ans);

				String returner = "[";

				for (int i = 0; i < ans.length; i++) {

					returner += ans[i];

					if (i != ans.length - 1) {

						returner += ",";

					}

				}

				returner += "]";

				key += "Correct Answer: " + returner + "\n";

			} else {

				boolean finalan = (boolean) entry.getValue().getAnswer();

				if (finalan == true) {

					key += "Correct Answer: " + "True" + "\n";

				} else {

					key += "Correct Answer: " + "False" + "\n";

				}

			}

		}

		return key;

	}

	@Override
	public boolean addStudent(String name) {

		if (students.containsKey(name)) {

			return false;

		}

		HashMap<Integer, Exam> copy = new HashMap<>();

		students.put(name, new Student(name, new HashMap<Integer, Exam>()));

		for (Map.Entry<Integer, Exam> entry : exams.entrySet()) {

			students.get(name).exams.put(entry.getKey(), new Exam(entry.getKey(), entry.getValue().examTitle));
			students.get(name).exams.get(entry.getKey()).passInQuestions(entry.getValue().questions);

		}

		return true;

	}

	@Override
	public void answerTrueFalseQuestion(String studentName, int examId, int questionNumber, boolean answer) {

		students.get(studentName).exams.get(examId).questions.get(questionNumber).setAnswer(answer);

	}

	@Override
	public void answerMultipleChoiceQuestion(String studentName, int examId, int questionNumber, String[] answer) {

		students.get(studentName).exams.get(examId).questions.get(questionNumber).setAnswer(answer);

	}

	@Override
	public void answerFillInTheBlanksQuestion(String studentName, int examId, int questionNumber, String[] answer) {

		students.get(studentName).exams.get(examId).questions.get(questionNumber).setAnswer(answer);

	}

	@Override
	public double getExamScore(String studentName, int examId) {

		double points = 0;

		for (Map.Entry<Integer, Question> entry : students.get(studentName).exams.get(examId).questions.entrySet()) {

			points += entry.getValue().pointsEarned();

		}

		return points;

	}

	@Override
	public String getGradingReport(String studentName, int examId) {

		String report = "";
		double scoreEarned = 0;
		double possible = 0;

		for (Map.Entry<Integer, Question> entry : students.get(studentName).exams.get(examId).questions.entrySet()) {

			report += "Question #" + entry.getValue().quesNo + " " + entry.getValue().pointsEarned() + " points out of "
					+ entry.getValue().getPoints() + "\n";
			scoreEarned += entry.getValue().pointsEarned();
			possible += entry.getValue().getPoints();

		}

		report += "Final Score: " + scoreEarned + " out of " + possible;

		return report;

	}

	@Override
	public void setLetterGradesCutoffs(String[] letterGrades, double[] cutoffs) {

		this.cutOffs = letterGrades;
		this.doubCut = cutoffs;

	}

	@Override
	public double getCourseNumericGrade(String studentName) {

		double total = 0;

		TreeMap<Integer, Exam> treeMap = new TreeMap<>();

		for (Map.Entry<Integer, Exam> entry : students.get(studentName).exams.entrySet()) {

			total += this.getExamScore(studentName, entry.getValue().examId) / entry.getValue().getTotalPoints();

		}

		return total * 100 / exams.size();

	}

	@Override
	public String getCourseLetterGrade(String studentName) {

		double total = getCourseNumericGrade(studentName);

		for (int i = 0; i < this.doubCut.length; i++) {

			if (total >= doubCut[i]) {

				return this.cutOffs[i];

			}

		}

		return "";

	}

	@Override
	public String getCourseGrades() {

		String returner = "";

		TreeMap<String, Student> ordered = new TreeMap<>();

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			ordered.put(entry.getKey(), entry.getValue());

		}

		for (Map.Entry<String, Student> entry : ordered.entrySet()) {

			returner += entry.getKey() + " " + getCourseNumericGrade(entry.getKey()) + " "
					+ getCourseLetterGrade(entry.getKey()) + "\n";

		}

		return returner;

	}

	@Override
	public double getMaxScore(int examId) {

		double max = 0;

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			double checker = this.getExamScore(entry.getKey(), examId);

			if (checker > max) {

				max = checker;

			}

		}

		return max;

	}

	@Override
	public double getMinScore(int examId) {

		double min = 100000;

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			double checker = this.getExamScore(entry.getKey(), examId);

			if (checker < min) {

				min = checker;

			}

		}

		return min;

	}

	@Override
	public double getAverageScore(int examId) {

		double average = 0;

		double checker = 0;

		int counter = 0;

		for (Map.Entry<String, Student> entry : students.entrySet()) {

			checker += this.getExamScore(entry.getKey(), examId);
			counter++;

		}

		average = checker;

		return average / counter;

	}

}
