package ct414;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client {
    private ExamServer server;
    private int sessToken;
    private int user;

    public Client(ExamServer server) throws NoMatchingAssessment, UnauthorizedAccess, RemoteException {

        while(true) {
            this.server = server;
            login();
            System.out.println("Available Assessments:");
            ArrayList<String> assessmentTitles = (ArrayList<String>) server.getAvailableSummary(this.sessToken, this.user);

            if(assessmentTitles.isEmpty()){
                System.out.println("No Assessments Available");
                break;
            }

            String[] strArray = (String[]) assessmentTitles.toArray(new String[0]);

            String assessChoice = (String) JOptionPane.showInputDialog(null, "Which assessment?",
                    "Assessment Choice", JOptionPane.QUESTION_MESSAGE, null, // Use
                    // default
                    // icon
                    strArray, // Array of choices
                    strArray[0]); // Initial choice


            System.out.println(assessChoice);
            String courseCode = assessChoice.split(":")[0];
            System.out.println(courseCode);
            Assessment assessment = null;
            assessment = server.getAssessment(this.sessToken, this.user, courseCode);

            if(assessment==null){
                System.out.println("Assessment not found");
                break;
            }

            JOptionPane.showMessageDialog(null, "Assignment due: " + assessment.getClosingDate());

            ArrayList<Question> questions = (ArrayList<Question>) assessment.getQuestions();

            int index = 0;
            for (Question q : questions){
                System.out.println("Q" + q.getQuestionNumber() + " - " + q.getQuestionDetail());

                strArray = q.getAnswerOptions();

                String qChoice = (String) JOptionPane.showInputDialog(null, q.getQuestionDetail(),
                        "Question " + q.getQuestionNumber(), JOptionPane.QUESTION_MESSAGE, null, strArray, strArray[0]);

                try {
                    assessment.selectAnswer(q.getQuestionNumber(), java.util.Arrays.asList(strArray).indexOf(qChoice));
                } catch (InvalidQuestionNumber invalidQuestionNumber) {
                    invalidQuestionNumber.printStackTrace();
                } catch (InvalidOptionNumber invalidOptionNumber) {
                    invalidOptionNumber.printStackTrace();
                }
            }

            this.server.submitAssessment(this.sessToken, this.user, assessment);

        }
    }

    public void loginCreds() throws UnauthorizedAccess, RemoteException {
        try {
            int user = Integer.parseInt(JOptionPane.showInputDialog("Please input ID: "));
            String pass = JOptionPane.showInputDialog("Please input password: ");
            this.sessToken = this.server.login(user, pass);
        } catch (NullPointerException  e) {
            System.out.println(this.sessToken);
        }
        this.user = user;
    }

    public void login(){
        try {
            loginCreds();
            if(this.sessToken==0){throw new Exception("Login Failed");}
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null,
                    "Incorrect Details! Try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            login();
        }
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            System.setProperty("java.security.policy","file:./global.policy");

            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry();
            ExamServer exam = (ExamServer) registry.lookup(name);
            System.out.println("Connected to servers...");
            new Client(exam);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


