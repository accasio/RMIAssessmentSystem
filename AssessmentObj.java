package ct414;


import java.util.Date;
import java.util.List;

public class AssessmentObj implements Assessment {

    private String info;
    private Date closingDate;
    private List<Question> questions;
    private int ID;
    private String courseCode;

    public AssessmentObj(String info, Date closingDate, List<Question> questions, int ID, String courseCode){
        this.info = info;
        this.closingDate = closingDate;
        this.questions = questions;
        this.ID = ID;
        this.courseCode = courseCode;
    }

    @Override
    public String getInformation() {
        return this.info;
    }

    @Override
    public Date getClosingDate() {
        return this.closingDate;
    }

    @Override
    public List<Question> getQuestions() {
        return this.questions;
    }

    @Override
    public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
        return null;
    }


    public String getCourseCode() {
        return this.courseCode;
    }

    @Override
    public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {

    }

    @Override
    public int getSelectedAnswer(int questionNumber) {
        return 0;
    }

    @Override
    public int getAssociatedID() {
        return this.ID;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
