package quiz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import customObjects.StringBooleanPair;
import customObjects.StringPair;

import question.FillBlankQuestion;
import question.MatchingQuestion;
import question.MultChoiceMultAnswerQuestion;
import question.MultipleChoiceQuestion;
import question.PictureQuestion;
import question.Question;
import question.QuestionTypes;
import question.SingleAnswerQuestion;


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
	 * @param quizInfo Quiz info
	 * @param userHistory List of user's results on this quiz
	 * @return Quiz object formatted as JSON
	 */
	public static JSONObject parseQuizIntoJSON(Quiz quiz, QuizInfo quizInfo, ArrayList<QuizResults> userHistory) {
		
		JSONObject jSONquiz = new JSONObject();
				
		ArrayList<Question> questions = quiz.getQuestions();
		
		JSONArray questionsFormatted = new JSONArray();
		
		for (int i = 0; i < questions.size(); i++) {
			JSONObject jSONquestion = parseQuestionIntoJSON(questions.get(i));
			questionsFormatted.put(jSONquestion);
		}
		
		/* This long block is explained by the Wiki spec here:
		 * https://github.com/djoeman84/QuizWebsite/wiki/Quiz-Page-Servlet-Interaction 
		 */
		jSONquiz.put("questions", questionsFormatted);
		jSONquiz.put("quiz_id", quiz.getID());
		jSONquiz.put("quiz_name", quiz.getName());
		jSONquiz.put("description", quiz.getDescription());
		jSONquiz.put("creator", quiz.getCreator());
		jSONquiz.put("is_immediately_corrected", quiz.isImmediatelyCorrected());
		jSONquiz.put("max_score", quiz.getMaxScore());
		jSONquiz.put("is_multiple_page", quiz.isMultiplePage());
		jSONquiz.put("is_randomized", quiz.isRandom());
		jSONquiz.put("is_practicable", quiz.isPracticable());
		jSONquiz.put("user_history", parseListIntoSpec(userHistory));
//		jSONquiz.put("best_alltime", parseListIntoSpec(quizInfo.getBestScoresAllTime()));
//		jSONquiz.put("best_today", parseListIntoSpec(quizInfo.getBestScoresToday()));
//		jSONquiz.put("recent_scores", parseListIntoSpec(quizInfo.getRecentScores()));
//		jSONquiz.put("average_score", quizInfo.getAverageScore());
//		jSONquiz.put("is_editable", quizInfo.isEditable());
		return jSONquiz;
	}
	
	/**
	 * Format is in spec
	 * @param userHistory
	 * @return
	 */
	private static JSONArray parseListIntoSpec(ArrayList<QuizResults> list) {
		JSONArray userQuizTakingInstances = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject quizTakingInstance = new JSONObject();
			
			JSONObject date = new JSONObject();
			date.put("year", list.get(i).getDateTaken().get(Calendar.YEAR));
			date.put("month", list.get(i).getDateTaken().get(Calendar.MONTH));
			date.put("date", list.get(i).getDateTaken().get(Calendar.DATE));
			quizTakingInstance.put("date", date);
			quizTakingInstance.put("time", list.get(i).getTimeTaken());
			quizTakingInstance.put("score", list.get(i).getUserPercentageScore());
			quizTakingInstance.put("name", list.get(i).getUsername());
			
			userQuizTakingInstances.put(quizTakingInstance);
		}
		return userQuizTakingInstances;
	}
	
	/**
	 * Return a QuizResults object given a JSONObject of answers from the client.
	 * 
	 * @param jSONanswers JSON of answers
	 * @param quiz The quiz taken
	 * @return
	 */
	public static QuizResults parseJSONIntoQuizResults(JSONObject jSONresults, Connection con) {
		
		double timeTaken = jSONresults.getLong("time");
		double userPercentageScore = jSONresults.getInt("percentage");
		String quizIDString = jSONresults.getString("quiz_id");
		long quizID = Long.parseLong(quizIDString);
		
		/* Use this later on if you want to calculate percentage score */
		Quiz thisQuiz = new Quiz(quizID, con);
		
		QuizResults results = new QuizResults((long)0, quizID, userPercentageScore, timeTaken, con);
		return results;
	}

	/**
	 * Parses a question into a JSONObject. Read below to see how each type
	 * is formatted.
	 * @param question
	 * @return
	 */
	private static JSONObject parseQuestionIntoJSON(Question question) {
		JSONObject questionInfo = new JSONObject();
		
		switch (question.getQuestionType()) {
		
		case QuestionTypes.MULTIPLE_CHOICE: return parseMultChoiceIntoJSON((MultipleChoiceQuestion)question, questionInfo);
		
		case QuestionTypes.SINGLE_ANSWER: return parseSingleAnswerIntoJSON((SingleAnswerQuestion)question, questionInfo);

		case QuestionTypes.PICTURE_RESPONSE: return parsePictureResponseIntoJSON((PictureQuestion)question, questionInfo);
		
		case QuestionTypes.MULT_CHOICE_MULT_ANSWER: return parseMultChoiceMultAnswerIntoJSON((MultChoiceMultAnswerQuestion)question, questionInfo);
		
		case QuestionTypes.MATCHING: return parseMatchingIntoJSON((MatchingQuestion)question, questionInfo);
		
		case QuestionTypes.FILL_BLANK: return parseBlankIntoJSON((FillBlankQuestion)question, questionInfo);
		default: return null; // Add more types
		}
	}
	
	private static JSONObject parseBlankIntoJSON(FillBlankQuestion question, JSONObject questionInfo) {
		questionInfo.accumulate("type", "fill-blank");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("optional_prompt", question.getOptionalPrompt());
		q_data.accumulate("first_prompt", question.getFirstPrompt());
		q_data.accumulate("second_prompt", question.getSecondPrompt());
		
		JSONArray answersArray = new JSONArray();
		
		for (String answer : question.getPossibleAnswers()) {
			answersArray.put(answer);
		}
		
		q_data.accumulate("answers", answersArray);
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
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
	 * @param questionInfo Empty JSONObject
	 * @return
	 */
	private static JSONObject parseMultChoiceIntoJSON(MultipleChoiceQuestion question, JSONObject questionInfo) {
		questionInfo.accumulate("type", "multiple-choice");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompt());
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
	 * @param questionInfo empty JSONObject
	 * @return
	 */
	private static JSONObject parseSingleAnswerIntoJSON(SingleAnswerQuestion question, JSONObject questionInfo) {
		questionInfo.accumulate("type", "single-answer");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompt());
		q_data.accumulate("correct", question.getPossibleAnswers());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
	
	/**
	 * Given a picture response question, returns a JSONObject
	 * formatted as: {type:
	 *                data: {img_url: (url string)
	 *                       prompt: (one string)
	 *                       correct: (list of strings)
	 *                       score: (int)
	 *                       }
	 * @param question
	 * @param questionInfo Empty JSONObject
	 * @return
	 */
	private static JSONObject parsePictureResponseIntoJSON(PictureQuestion question, JSONObject questionInfo) {
		questionInfo.accumulate("type", "picture-response");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("img_url", question.getPictureURL());
		q_data.accumulate("prompt", question.getPrompt());
		q_data.accumulate("correct", question.getPossibleAnswers());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
	
	/**
	 * Given a mult choice mult answer question, returns a JSONObject
	 * formatted as: {type:
	 * 				  data: {prompt: (one string)
	 *   					 options: (list of strings)
	 *   					 correct: (list of booleans)
	 *   					 score: (int)
	 *                }
	 * @param question
	 * @param questionInfo
	 * @return
	 */
	private static JSONObject parseMultChoiceMultAnswerIntoJSON(MultChoiceMultAnswerQuestion question, JSONObject questionInfo) {
		questionInfo.accumulate("type", "multiple-answer");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompt());
		
		ArrayList<StringBooleanPair> optionsAndAnswers = question.getPairs();
		
		JSONArray pairsArray = new JSONArray();
		
		for (StringBooleanPair pair : optionsAndAnswers) {
			JSONArray newPair = new JSONArray();
			newPair.put(pair.getStr());
			newPair.put(pair.getBool());
			pairsArray.put(newPair);
		}
		
		q_data.accumulate("answers", pairsArray);
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
	
	/**
	 * Given a matching question, returns a JSONObject
	 * formatted as: {type:
	 * 				  data: {prompt: (one string)
	 *   					 left_options: (list of strings)
	 *   					 right_options: (list of strings)
	 *   					 correct: (list of Integers)
	 *   					 score: (int)
	 *                }
	 * @param question
	 * @param questionInfo
	 * @return
	 */
	private static JSONObject parseMatchingIntoJSON(MatchingQuestion question, JSONObject questionInfo) {
		questionInfo.accumulate("type", "matching");
		
		JSONObject q_data = new JSONObject();
		
		q_data.accumulate("prompt", question.getPrompt());
		ArrayList<StringPair> optionsAndAnswers = question.getPairs();
		
		JSONArray pairsArray = new JSONArray();
		
		for (StringPair pair : optionsAndAnswers) {
			JSONArray newPair = new JSONArray();
			newPair.put(pair.getFirst());
			newPair.put(pair.getSecond());
			pairsArray.put(newPair);
		}
		
		q_data.accumulate("answers", pairsArray);
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
}
