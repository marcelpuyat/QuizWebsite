package customObjects;

/**
 * Integer pair sorted by first value.
 * @author marcelp
 *
 */
public class RatingAndQuizID implements Comparable<RatingAndQuizID> {

	private Double first;
	private Integer second;
	
	public RatingAndQuizID(Double rating, Integer quizID) {
		this.setQuizID(quizID);
		this.setRating(rating);
	}

	/**
	 * @param first the first to set
	 */
	private void setRating(Double first) {
		this.first = first;
	}

	/**
	 * @return the first
	 */
	public Double getRating() {
		return first;
	}

	/**
	 * @param second the second to set
	 */
	private void setQuizID(Integer second) {
		this.second = second;
	}

	/**
	 * @return the second
	 */
	public Integer getQuizID() {
		return second;
	}

	@Override
	public int compareTo(RatingAndQuizID o) {
		return o.first.intValue() - this.first.intValue();
	}
}
