package quizPckg;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import questionPckg.MultipleChoiceQuestion;
import questionPckg.Question;
import questionPckg.QuestionTypes;
import questionPckg.SingleAnswerQuestion;

/**
 * Has static methods to parse quiz into JSON & back. The rest of the methods
 * are helper methods for this quiz parsing (including parsing different types
 * of questions)
 * 
 * Information on how JSON for each Question type can be found in comments of
 * each method.
 * @author marcelp
 *
 */
public class JSONParser {


	/**
	 * Quiz is formatted differently depending on type.
	 *    
	 * @param quiz A Quiz Object
	 * @return Quiz object formatted as JSON
	 */
	public static JSONObject parseQuizIntoJSON(Quiz quiz) {
		
		JSONObject jSONquiz = new JSONObject();
				
		ArrayList<Question> questions = quiz.getQuestions();
		
		JSONArray questionsFormatted = new JSONArray();
		
		for (int i = 0; i < questions.size(); i++) {
			JSONObject jSONquestion = parseQuestionIntoJSON(questions.get(i));
			questionsFormatted.put(jSONquestion);
		}
		
		jSONquiz.put("questions", questionsFormatted);

		return jSONquiz;
	}
	
	/**
	 * Returns a JSONObject given quiz results in this format:
	 * {
	 *   feedback: {
	 *      total_score:100
	 *      total_correct: 4
	 *      total_possible: 20
	 *      }
	 * }
	 * @param quizResults QuizResults object
	 * @return JSONObject according to our spec
	 */
	public static JSONObject parseQuizResultsIntoJSON(QuizResults quizResults) {
		JSONObject jSONresults = new JSONObject();
		
		JSONObject feedback = new JSONObject();
		
		feedback.accumulate("total_score", quizResults.getPercentageScore());
		feedback.accumulate("total_correct", quizResults.getUserScore());
		feedback.accumulate("total_possible", quizResults.getMaxScore());
		
		jSONresults.put("feedback", feedback);
		
		return jSONresults;
	}
	
	/**
	 * Return a QuizResults object given a JSONObject of answers from the client.
	 * 
	 * @param jSONanswers JSON of answers
	 * @param quiz The quiz taken
	 * @return
	 */
	public static QuizResults parseJSONIntoQuizResults(JSONObject jSONanswers, Quiz quiz) {
		
		ArrayList<String> answers = new ArrayList<String>();
		
		JSONArray arrayOfAnswers = (JSONArray) jSONanswers.get("answers");
		
		for (int i = 0; i < arrayOfAnswers.length(); i++) {
			JSONObject questionResult = (JSONObject)arrayOfAnswers.get(i);
			String type = (String)questionResult.get("type");
			
			JSONObject data = (JSONObject)questionResult.get("data");
			
			if (type.equals("multiple-choice")) {
				answers.add(getAnswerFromMultChoiceJSON(data));
			}
			else if (type.equals("single-answer")) {
				answers.add(getAnswerFromSingleAnswerJSON(data));
			}
			else break;
			// TODO FINISH THIS WITH MORE TYPES
		}
		
		int userScore = quiz.checkAnswers(answers);
		
		QuizResults results = new QuizResults(null, quiz.getID(), userScore, quiz.getMaxScore());
		return results;
	}
	
	/**
	 * Returns the answer string given a multiple choice JSON answer object (based on spec)
	 * @param data
	 * @return
	 */
	private static String getAnswerFromMultChoiceJSON(JSONObject data) {
		return data.getString("index_selected");
	}
	
	/**
	 * Returns the answer string given a single-answer JSON answer object (based on spec)
	 * @param data
	 * @return
	 */
	private static String getAnswerFromSingleAnswerJSON(JSONObject data) {
		return data.getString("answer");
	}
	
	/**
	 * Parses a question into a JSONObject. Read below to see how each type
	 * is formatted.
	 * @param question
	 * @return
	 */
	private static JSONObject parseQuestionIntoJSON(Question question) {
		switch (question.getQuestionType()) {
		
		case QuestionTypes.MULTIPLE_CHOICE: return parseMultChoiceIntoJSON((MultipleChoiceQuestion)question);
		
		case QuestionTypes.SINGLE_ANSWER: return parseSingleAnswerIntoJSON((SingleAnswerQuestion)question);

		default: return null;
		}
	}
	
	/**
	 * Given a multiple choice question, returns a JSONObject
	 * formatted as: {type:
	 * 				  data: {prompt: (one string)
	 * 	                     options: (list of strings)
	 *  					 correct: (int, which is the index of the correct answer)
	 *                       score:  (int)
	 *                       } 
	 * @param question
	 * @return
	 */
	private static JSONObject parseMultChoiceIntoJSON(MultipleChoiceQuestion question) {
		JSONObject questionInfo = new JSONObject();
		questionInfo.accumulate("type", "multiple-choice");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompts().get(0));
		q_data.accumulate("options", question.getOptions());
		q_data.accumulate("correct", question.getAnswer());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
	
	/**
	 * Given a single answer question, returns a JSONObject
	 * formatted as: {type:
	 * 				  data: {prompt: (one string)
	 * 	                     correct: (list of strings)
	 *                       score: (int)
	 *                       }              
	 * @param question
	 * @return
	 */
	private static JSONObject parseSingleAnswerIntoJSON(SingleAnswerQuestion question) {
		JSONObject questionInfo = new JSONObject();
		questionInfo.accumulate("type", "single-answer");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompts().get(0));
		q_data.accumulate("correct", question.getPossibleAnswers());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
}
