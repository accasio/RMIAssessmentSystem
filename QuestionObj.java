package ct414;

public class QuestionObj implements Question {

    private int questionNumber;
    private String questionDetail;
    private String[] answerOptions;

    public QuestionObj(int questionNumber, String questionDetail, String[] answerOptions) {
        this.questionNumber = questionNumber;
        this.questionDetail = questionDetail;
        this.answerOptions = answerOptions;
    }

    @Override
    public int getQuestionNumber() {
        return this.questionNumber;
    }

    @Override
    public String getQuestionDetail() {
        return this.questionDetail;
    }

    @Override
    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public void setAnswerOptions(String[] answerOptions) {
        this.answerOptions = answerOptions;
    }
}
