
package ct414;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static java.lang.Boolean.TRUE;

public class ExamEngine implements ExamServer {

    private List<Assessment> assessment_list;
    private List<Student> student_list;

    // Constructor is required
    public ExamEngine() {
        super();
    }

    public ExamEngine(List<Assessment> assessment_list, List<Student> student_list) {
        this.assessment_list = assessment_list;
        this.student_list = student_list;
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws 
                UnauthorizedAccess, RemoteException {
        try {
            for (int i = 0; i < student_list.size(); i++){
                if(student_list.get(i).getStudentID()==studentid){
                    if(student_list.get(i).getPassword().equals(password)){
                        Random rand = new Random();
                        int token = rand.nextInt(999999)+1;
                        student_list.get(i).setToken(token);
                        return token;
                    } else {
                        throw new UnauthorizedAccess("Password Incorrect");
                    }
                }
            }
            throw new UnauthorizedAccess("ID not found");
        } catch (UnauthorizedAccess e){
            System.out.println(e.toString());
            return 0;
        }
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        List<String> summaries = new ArrayList<>();

        for(int i = 0; i < student_list.size(); i++) {
            if(student_list.get(i).getStudentID()==studentid&&student_list.get(i).getToken()==token){
                for (int j=0;j<assessment_list.size();j++){
                    if(assessment_list.get(j).getAssociatedID()==studentid) {
                        summaries.add(assessment_list.get(j).getInformation());
                    }
                }
            }
        }

        return summaries;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        for(int i = 0; i < student_list.size(); i++) {
            if (student_list.get(i).getStudentID() == studentid && student_list.get(i).getToken() == token) {
                for (int j = 0; j < assessment_list.size(); j++) {
                    if (assessment_list.get(j).getCourseCode().equals(courseCode)) {
                        return assessment_list.get(j);
                    }
                }
            }
        }

        return null;
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws 
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        for(int i = 0; i < student_list.size(); i++) {
            if(student_list.get(i).getStudentID()==studentid&&student_list.get(i).getToken()==token){
                if(new GregorianCalendar().getTime().compareTo(completed.getClosingDate())<=0) {
                    completed.setCompleted(TRUE);
                    System.out.println("Assignment submitted");
                } else {
                    System.out.println("Closing date has passed");
                }
            }
        }

        // TBD: You need to implement this method!
    }

    public static void main(String[] args) {

        Question q1 = new QuestionObj(1, "This is question 1", new String[]{"a","b","c"});
        Question q2 = new QuestionObj(2, "This is question 2", new String[]{"a","b","c","d"});
        Question q3 = new QuestionObj(3, "This is question 3", new String[]{"a","b","c","d","e"});
        Question q4 = new QuestionObj(4, "This is question 4", new String[]{"a","b","c"});

        List<Question> set1 = new ArrayList<>();
        set1.add(q1);
        set1.add(q2);
        set1.add(q3);

        List<Question> set2 = new ArrayList<>();
        set2.add(q2);
        set2.add(q3);
        set2.add(q4);

        Date closingDate1 = new GregorianCalendar(2018, Calendar.FEBRUARY, 15).getTime();
        Date closingDate2 = new GregorianCalendar(2018, Calendar.FEBRUARY, 18).getTime();

        Assessment assessment1 = new AssessmentObj("4BCT: Assessment 1", closingDate1, set1, 1, "4BCT");
        Assessment assessment2 = new AssessmentObj("4BSC: Assessment 2", closingDate2, set2, 2, "4BSC");

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        Student stud1 = new Student(1, "password1");
        Student stud2 = new Student(2, "password2");
        List<Student> students = new ArrayList<>();
        students.add(stud1);
        students.add(stud2);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine(assessments, students);
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }

}
