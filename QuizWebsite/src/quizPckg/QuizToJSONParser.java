package quizPckg;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import questionPckg.MultipleChoiceQuestion;
import questionPckg.Question;
import questionPckg.QuestionTypes;
import questionPckg.SingleAnswerQuestion;

/**
 * Has a static method to parse quiz into JSON. The rest of the methods
 * are helper methods for this quiz parsing (including parsing different types
 * of methods)
 * 
 * Information on how JSON for each Question type can be found in comments of
 * each method.
 * @author marcelp
 *
 */
public class QuizToJSONParser {


	/**
	 * Quiz is formatted differently depending on type.
	 *    
	 * @param quiz A Quiz Object
	 * @return Quiz object formatted as JSON
	 */
	public static JSONObject parseQuizIntoJSON(Quiz quiz) {
		
		// Quiz to be passed
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
	 * Parses a question into a JSONObject. Read below to see how each type
	 * is formatted.
	 * @param question
	 * @return
	 */
	private static JSONObject parseQuestionIntoJSON(Question question) {
		if (question.getQuestionType() == QuestionTypes.MULTIPLE_CHOICE) {
			return parseMultChoiceIntoJSON((MultipleChoiceQuestion)question);
		}
		
		if (question.getQuestionType() == QuestionTypes.SINGLE_ANSWER) {
			return parseSingleAnswerIntoJSON((SingleAnswerQuestion)question);
		}
		
		// ADD MORE HERE
		else return null;
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
