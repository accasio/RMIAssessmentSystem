package ct414;

public class Student {

    private int studentID;
    private String password;
    private int token;

    public Student(int studentID, String password){
        this.studentID = studentID;
        this.password = password;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getPassword() {
        return password;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getToken() {
        return token;
    }
}

