package ct414;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;

public class Client {
    private ExamServer server;
    private int sessToken;
    private int user;

    public Client(ExamServer server) throws NoMatchingAssessment, UnauthorizedAccess, RemoteException {
        UIManager.put("OptionPane.cancelButtonText", "Exit");

        while(true) {
            this.server = server;
            login();
            System.out.println("Available Assessments:");
            ArrayList<String> assessmentTitles = (ArrayList<String>) server.getAvailableSummary(this.sessToken, this.user);

            if(assessmentTitles.isEmpty()){
                JOptionPane.showMessageDialog(
                        null,
                        "Lucky you! No assignments available.",
                        "No Assignments!",
                        JOptionPane.INFORMATION_MESSAGE,
                        null);
                continue;
            }

            String[] strArray = (String[]) assessmentTitles.toArray(new String[0]);

            String assessChoice = (String) JOptionPane.showInputDialog(null, "Choose an assessment?",
                    "Assessment Choice", JOptionPane.QUESTION_MESSAGE, null, // Use
                    // default
                    // icon
                    strArray, // Array of choices
                    strArray[0]); // Initial choice

            while (assessChoice == null) {
                areYouSure();
                assessChoice = (String) JOptionPane.showInputDialog(null, "Choose an assessment?",
                        "Assessment Choice", JOptionPane.QUESTION_MESSAGE, null, // Use
                        // default
                        // icon
                        strArray, // Array of choices
                        strArray[0]); // Initial choice
            }
            System.out.println(assessChoice);
            String courseCode = assessChoice.split(":")[0];
            System.out.println(courseCode);
            Assessment assessment = null;
            assessment = server.getAssessment(this.sessToken, this.user, courseCode);

            if(assessment == null) {
                JOptionPane.showMessageDialog(null,
                        "Assignment not found on server.",
                        "Assignment Not Found!",
                        JOptionPane.ERROR_MESSAGE);
                continue;
            }

            Date today = new Date();
            Date closingDate = assessment.getClosingDate();
            int numBetween = daysBetween(closingDate, today);

            if (closingDate.after(today)) {

                JOptionPane.showMessageDialog(null, "Assignment due in " + String.valueOf(numBetween) +" days on: " + closingDate.toString());

                ArrayList<Question> questions = (ArrayList<Question>) assessment.getQuestions();

                int index = 0;
                for (Question q : questions) {
                    System.out.println("Q" + q.getQuestionNumber() + " - " + q.getQuestionDetail());

                    strArray = q.getAnswerOptions();

                    String qChoice = (String) JOptionPane.showInputDialog(null, q.getQuestionDetail(),
                            "Question " + q.getQuestionNumber(), JOptionPane.QUESTION_MESSAGE, null, strArray, strArray[0]);

                    while (qChoice == null) {
                        areYouSure();
                        qChoice = (String) JOptionPane.showInputDialog(null, q.getQuestionDetail(),
                                "Question " + q.getQuestionNumber(), JOptionPane.QUESTION_MESSAGE, null, strArray, strArray[0]);
                    }

                    try {
                        assessment.selectAnswer(q.getQuestionNumber(), java.util.Arrays.asList(strArray).indexOf(qChoice));
                    } catch (InvalidQuestionNumber invalidQuestionNumber) {
                        invalidQuestionNumber.printStackTrace();
                    } catch (InvalidOptionNumber invalidOptionNumber) {
                        invalidOptionNumber.printStackTrace();
                    }
                }

                this.server.submitAssessment(this.sessToken, this.user, assessment);
                JOptionPane.showMessageDialog(
                        null,
                        "Assignment Submitted!",
                        "Assignment Submitted!",
                        JOptionPane.INFORMATION_MESSAGE,
                        null);

            } else {
                JOptionPane.showMessageDialog(null,
                        "Assignment expired " + String.valueOf(numBetween) +" days ago! You can no longer submit this assignment.",
                        "Assignment Has Expired!",
                        JOptionPane.ERROR_MESSAGE);

            }

        }
    }

    private void loginCreds() throws UnauthorizedAccess, RemoteException {
        String userString = JOptionPane.showInputDialog("Please input ID: ");
        while (userString == null) {
            areYouSure();
            userString = JOptionPane.showInputDialog("Please input ID: ");
        }
        int user = Integer.parseInt(userString);
        String pass = JOptionPane.showInputDialog("Please input password: ");
        while (pass == null) {
            areYouSure();
            pass = JOptionPane.showInputDialog("Please input password: ");
        }
        this.sessToken = this.server.login(user, pass);
        this.user = user;
    }

    private void login(){
        try {
            loginCreds();
            if(this.sessToken==0){throw new RemoteException("Login Failed");}
        } catch (UnauthorizedAccess e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null,
                    "Incorrect Details! Try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            login();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Incorrect Details! Try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            login();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect Details! ID must be an integer.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            login();
        }
    }

    private void areYouSure() {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Are you sure want to exit?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private int daysBetween(Date d1, Date d2){
        float temp = ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        return (int) Math.abs(temp);
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


