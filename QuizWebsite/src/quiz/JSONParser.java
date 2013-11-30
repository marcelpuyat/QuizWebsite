package quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import question.FillBlankQuestion;
import question.MatchingQuestion;
import question.MultChoiceMultAnswerQuestion;
import question.MultipleChoiceQuestion;
import question.PictureQuestion;
import question.Question;
import question.QuestionTypes;
import question.SingleAnswerQuestion;
import user.User;
import customObjects.SelfRefreshingConnection;
import customObjects.StringBooleanPair;
import customObjects.StringPair;


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


	public static JSONObject getJSONfromRequest(HttpServletRequest request) throws IOException {
		StringBuilder json_str_response = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while( (str = br.readLine()) != null ){
			json_str_response.append(str);
	    }  
		return new JSONObject(json_str_response.toString());
	}
	
	public static Quiz storeNewQuizWithJSON(JSONObject newQuizJSON, SelfRefreshingConnection con) throws ClassNotFoundException {
		String quizName = newQuizJSON.getString("quiz_name");
		String description = newQuizJSON.getString("description");
		String creator = newQuizJSON.getString("creator");
		int maxScore = newQuizJSON.getInt("max_score");
		boolean isImmediatelyCorrected = newQuizJSON.getBoolean("is_immediately_corrected");
		boolean isMultPage = newQuizJSON.getBoolean("is_multiple_page");
		boolean isRandomized = newQuizJSON.getBoolean("is_randomized");
		boolean isPracticable = newQuizJSON.getBoolean("is_practicable");
		ArrayList<Question> questions = getQuestionsFromJSONArray(newQuizJSON.getJSONArray("questions"));
		
		// Adds Quiz to db
		Quiz newQuiz = new Quiz(con, quizName, creator, description, questions, maxScore, isRandomized, isMultPage, isPracticable, isImmediatelyCorrected);
		return newQuiz;
	}
	
	public static void editQuizWithJSON(JSONObject newQuizJSON, SelfRefreshingConnection con, int quiz_id) {
		String quizName = newQuizJSON.getString("quiz_name");
		String description = newQuizJSON.getString("description");
		String creator = newQuizJSON.getString("creator");
		int maxScore = newQuizJSON.getInt("max_score");
		boolean isImmediatelyCorrected = newQuizJSON.getBoolean("is_immediately_corrected");
		boolean isMultPage = newQuizJSON.getBoolean("is_multiple_page");
		boolean isRandomized = newQuizJSON.getBoolean("is_randomized");
		boolean isPracticable = newQuizJSON.getBoolean("is_practicable");
		ArrayList<Question> questions = getQuestionsFromJSONArray(newQuizJSON.getJSONArray("questions"));
		Quiz.editQuiz(quiz_id, con, quizName, creator, description, questions, maxScore, isRandomized, isMultPage, isPracticable, isImmediatelyCorrected);
		return;
	}
	
	public static ArrayList<String> getTagsFromJSONArray(JSONArray json) {
		ArrayList<String> tags = new ArrayList<String>();
		
		for (int i = 0; i < json.length(); i++) {
			tags.add(json.getString(i));
		}
		
		return tags;
	}
	
	private static ArrayList<Question> getQuestionsFromJSONArray(JSONArray jSONquestions) {
		ArrayList<Question> questions = new ArrayList<Question>(jSONquestions.length());
		for (int i = 0; i < jSONquestions.length(); i++) {
			questions.add(parseQuestionJSONIntoQuestion((JSONObject) jSONquestions.get(i)));
		}
		return questions;
	}
	
	private static Question parseQuestionJSONIntoQuestion(JSONObject question) {
		String type = question.getString("type");
		
		if (type.equals("multiple-choice")) {
			return parseJSONIntoMultipleChoiceQuestion(question.getJSONObject("data"));
		} else if (type.equals("multiple-answer")) {
			return parseJSONIntoMultAnswerQuestion(question.getJSONObject("data"));
		} else if (type.equals("picture-response")) {
			return parseJSONIntoPictureQuestion(question.getJSONObject("data"));
		} else if (type.equals("matching")) {
			return parseJSONIntoMatchingQuestion(question.getJSONObject("data"));
		} else if (type.equals("fill-blank")) {
			return parseJSONIntoFillBlankQuestion(question.getJSONObject("data"));
		} else if (type.equals("single-answer")) {
			return parseJSONIntoSingleAnswerQuestion(question.getJSONObject("data"));
		}
		
		// Means type was wrongly formatted
		else return null;
	}
		
	private static Question parseJSONIntoSingleAnswerQuestion(
			JSONObject question) {
		String prompt = question.getString("prompt");
		int score = question.getInt("score");
		JSONArray answers = question.getJSONArray("correct");
		HashSet<String> possibleAnswers = new HashSet<String>();
		for (int i = 0; i < answers.length(); i++) {
			possibleAnswers.add(answers.getString(i));
		}
		return new SingleAnswerQuestion(prompt, possibleAnswers, score);
	}

	private static Question parseJSONIntoFillBlankQuestion(JSONObject question) {
		String optionalPrompt = question.getString("optional_prompt");
		String firstPrompt = question.getString("first_prompt");
		String secondPrompt = question.getString("second_prompt");
		int score = question.getInt("score");
		JSONArray answers = question.getJSONArray("correct");
		HashSet<String> possibleAnswers = new HashSet<String>();
		for (int i = 0; i < answers.length(); i++) {
			possibleAnswers.add(answers.getString(i));
		}
		return new FillBlankQuestion(optionalPrompt, firstPrompt, secondPrompt, possibleAnswers, score);
	}

	private static Question parseJSONIntoMatchingQuestion(JSONObject question) {
		String prompt = question.getString("prompt");
		JSONArray arrayOfStringPairs = question.getJSONArray("correct");
		int score = question.getInt("score");
		ArrayList<StringPair> pairs = new ArrayList<StringPair>();
		for (int i = 0; i < arrayOfStringPairs.length(); i++) {
			JSONArray strings = (JSONArray)arrayOfStringPairs.get(i);
			String firstString = (String)strings.get(0);
			String secondString = (String)strings.get(1);
			StringPair newPair = new StringPair(firstString, secondString);
			pairs.add(newPair);
		}
		return new MatchingQuestion(prompt, pairs, score);
	}

	private static Question parseJSONIntoPictureQuestion(JSONObject question) {
		String prompt = question.getString("prompt");
		String imgUrl = question.getString("img_url");
		int score = question.getInt("score");
		JSONArray answers = question.getJSONArray("correct");
		HashSet<String> possibleAnswers = new HashSet<String>();
		for (int i = 0; i < answers.length(); i++) {
			possibleAnswers.add(answers.getString(i));
		}
		return new PictureQuestion(prompt, possibleAnswers, imgUrl, score);
	}

	private static Question parseJSONIntoMultAnswerQuestion(JSONObject question) {
		String prompt = question.getString("prompt");
		JSONArray arrayOfStringPairs = question.getJSONArray("correct");
		int score = question.getInt("score");
		boolean partialCredit = question.getBoolean("partial_credit");
		ArrayList<StringBooleanPair> pairs = new ArrayList<StringBooleanPair>();
		for (int i = 0; i < arrayOfStringPairs.length(); i++) {
			JSONArray strings = (JSONArray)arrayOfStringPairs.get(i);
			String string = (String)strings.get(0);
			Boolean value = (Boolean)strings.get(1);
			StringBooleanPair newPair = new StringBooleanPair(string, value.booleanValue());
			pairs.add(newPair);
		}
		return new MultChoiceMultAnswerQuestion(prompt, pairs, score, partialCredit);
	}

	private static Question parseJSONIntoMultipleChoiceQuestion(
			JSONObject question) {
		String prompt = question.getString("prompt");
		int answerIndex = question.getInt("correct");
		int score = question.getInt("score");
		ArrayList<String> options = new ArrayList<String>();
		JSONArray jSONoptions = question.getJSONArray("options");
		for (int i = 0; i < jSONoptions.length(); i++) {
			options.add(jSONoptions.getString(i));
		}
		return new MultipleChoiceQuestion(prompt, options, answerIndex, score);
	}

	/**
	 * Quiz is formatted differently depending on type.
	 *    
	 * @param quiz A Quiz Object
	 * @param quizInfo Quiz info
	 * @param userHistory List of user's results on this quiz
	 * @return Quiz object formatted as JSON
	 */
	public static JSONObject parseQuizIntoJSON(Quiz quiz, ArrayList<QuizResults> userHistory) {
		
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
		return jSONquiz;
	}
	
	public static JSONObject parseQuizInfoIntoJSON(QuizInfo quizInfo, User user) {
		JSONObject jSONinfo = new JSONObject();
		
		/* This long block is explained by the Wiki spec here:
		 * https://github.com/djoeman84/QuizWebsite/wiki/Quiz-Page-Servlet-Interaction 
		 */
		jSONinfo.put("user_history", parseListIntoSpec(quizInfo.getUserHistory(user.getUserId())));
		jSONinfo.put("best_alltime", parseListIntoSpec(quizInfo.getBestScoresAllTime()));
		jSONinfo.put("best_today", parseListIntoSpec(quizInfo.getBestScoresToday()));
		jSONinfo.put("recent_scores", parseListIntoSpec(quizInfo.getRecentScores()));
		jSONinfo.put("average_score", quizInfo.getAverageScore());
		jSONinfo.put("description", quizInfo.getDescription());
		jSONinfo.put("creator", quizInfo.getUsernameOfCreator());
		jSONinfo.put("quiz_name", quizInfo.getQuizName());
		jSONinfo.put("is_practicable", quizInfo.isPracticable());
		
		boolean isEditable = false;
		
		try {
			if (user != null) {
				isEditable = (user.getUserName().equals(quizInfo.getUsernameOfCreator()) || user.isAdmin());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		jSONinfo.put("is_editable", isEditable);
		
		return jSONinfo;
	}
	
	/**
	 * Format is in spec
	 * @param list
	 * @return
	 */
	private static JSONArray parseListIntoSpec(ArrayList<QuizResults> list) {
		JSONArray userQuizTakingInstances = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject quizTakingInstance = new JSONObject();
			
			JSONObject date = new JSONObject();
			
			Calendar dateTaken = list.get(i).getDateTaken();
				int year = dateTaken.get(Calendar.YEAR);
				int month = dateTaken.get(Calendar.MONTH) + 1; // MONTHS ARE INDEXED FROM 0!
				int day = dateTaken.get(Calendar.DATE);
				date.put("year", year);
				date.put("month", month);
				date.put("date", day);
			quizTakingInstance.put("date", date);
			quizTakingInstance.put("time", list.get(i).getTimeTaken());
			quizTakingInstance.put("score", list.get(i).getUserPercentageScore());
			try {
				quizTakingInstance.put("name", list.get(i).getUsername());
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
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
	public static QuizResults parseJSONIntoQuizResults(JSONObject jSONresults, SelfRefreshingConnection con, User user) {
		
		double timeTaken = jSONresults.getDouble("time");
		double userPercentageScore = jSONresults.getDouble("percentage");
		String quizIDString = jSONresults.getString("quiz_id");
		long quizID = Long.parseLong(quizIDString);
		
		QuizResults results = new QuizResults(user.getUserId(), quizID, userPercentageScore, timeTaken, con);
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
				
		for (String answer : question.getPossibleAnswers()) {
			q_data.append("correct", answer);
		}
		
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
				
		for (StringBooleanPair pair : optionsAndAnswers) {
			JSONArray newPair = new JSONArray();
			newPair.put(pair.getStr());
			newPair.put(pair.getBool());
			q_data.append("correct", newPair);
		}
		
		q_data.accumulate("score", question.getScore());
		q_data.accumulate("partial_credit", question.isPartialCredit());
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
		
//		JSONArray pairsArray = new JSONArray();
		
		for (StringPair pair : optionsAndAnswers) {
			JSONArray newPair = new JSONArray();
			newPair.put(pair.getFirst());
			newPair.put(pair.getSecond());
//			pairsArray.put(newPair);
			q_data.append("correct", newPair);
		}
		
//		q_data.accumulate("answers", pairsArray);
		q_data.accumulate("score", question.getScore());
		questionInfo.accumulate("data", q_data);
		return questionInfo;
	}
}
