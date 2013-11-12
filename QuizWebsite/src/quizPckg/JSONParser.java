package quizPckg;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import questionPckg.MatchingQuestion;
import questionPckg.MultChoiceMultAnswerQuestion;
import questionPckg.MultipleChoiceQuestion;
import questionPckg.PictureQuestion;
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
		jSONquiz.put("description", quizInfo.getDescription());
		jSONquiz.put("creator", quizInfo.getUsernameOfCreator());
		jSONquiz.put("is_immediately_corrected", quiz.isImmediatelyCorrected());
		jSONquiz.put("max_score", quiz.getMaxScore());
		jSONquiz.put("user_history", parseListIntoSpec(userHistory));
		jSONquiz.put("best_alltime", parseListIntoSpec(quizInfo.getBestScoresAllTime()));
		jSONquiz.put("best_today", parseListIntoSpec(quizInfo.getBestScoresToday()));
		jSONquiz.put("recent_scores", parseListIntoSpec(quizInfo.getRecentScores()));
		jSONquiz.put("average_score", quizInfo.getAverageScore());
		jSONquiz.put("is_editable", quizInfo.isEditable());
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
			quizTakingInstance.put("score", list.get(i).getUserScore());
			quizTakingInstance.put("name", list.get(i).getUsername());
			
			userQuizTakingInstances.put(quizTakingInstance);
		}
		return userQuizTakingInstances;
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
		
		feedback.accumulate("total_score", (double)quizResults.getUserScore() / quizResults.getMaxScore());
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
			else if(type.equals("picture-response")) {
				answers.add(getAnswerFromPictureResponseJSON(data));
			}
			else if(type.equals("multiple-answer")) {
				answers.add(getAnswerFromMultChoiceMultAnswerJSON(data));
			}
			else if (type.equals("matching")) {
				answers.add(getAnswerFromMatchingAnswerJSON(data));
			}
			else break;
			// TODO FINISH THIS WITH MORE TYPES
		}
		
		int userScore = quiz.checkAnswers(answers);
		
		Calendar currentDate = Calendar.getInstance();
		
		// UPDATE THIS TO INCLUDE TIME ONCE IT IS PASSED THROUGH JSON! 
		QuizResults results = new QuizResults(null, quiz.getID(), userScore, quiz.getMaxScore(), currentDate, 0);
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
	 * Returns the answer string given a picture-response JSON answer object (based on spec)
	 * @param data
	 * @return
	 */
	private static String getAnswerFromPictureResponseJSON(JSONObject data) {
		return data.getString("answer");
	}
	
	/**
	 * Returns answer string (in form 001010 where 1 is true and 0 is false)
	 * @param data
	 * @return
	 */
	private static String getAnswerFromMultChoiceMultAnswerJSON(JSONObject data) {
		String answer = "";
		JSONArray list = (JSONArray)data.getJSONArray("answer");
		for (int i = 0; i < list.length(); i++) {
			if (list.getBoolean(i)) answer += "1";
			else answer += "0";
		}
		return answer;
	}
	
	/**
	 * Returns answer string (in form of "015324" where ArrayList of ints had values [0, 1, 5, 3, 2, 4] )
	 * @param data
	 * @return
	 */
	private static String getAnswerFromMatchingAnswerJSON(JSONObject data) {
		String answer = "";
		JSONArray list = (JSONArray)data.getJSONArray("answer");
		for (int i = 0; i < list.length(); i++) {
			answer += String.valueOf(((Integer)list.get(i)).intValue());
		}
		return answer;
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
		
		default: return null; // Add more types
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
		q_data.accumulate("options", question.getOptions());
		ArrayList<Boolean> answers = new ArrayList<Boolean>();
		String answer = question.getAnswer();
		for (int i = 0; i < answer.length(); i++) {
			if (answer.charAt(i) == '0') answers.add(false);
			else answers.add(true);
		}
		q_data.accumulate("correct", answers);
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
		q_data.accumulate("left_options", question.getLeftOptions());
		q_data.accumulate("right_options", question.getRightOptions());
		q_data.accumulate("correct", question.getAnswer());
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
}
