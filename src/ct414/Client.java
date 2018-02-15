package ct414;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private ExamServer server;
    private int sessToken;
    private int user;

    //    public Client(ExamServer server) {
    public Client() {
        login();

    }

    public void loginCreds() throws UnauthorizedAccess, RemoteException {
        int user = Integer.parseInt(JOptionPane.showInputDialog("Please input ID: "));
        String pass = JOptionPane.showInputDialog("Please input password: ");
        this.sessToken = this.server.login(user, pass);
        this.user = user;
    }

    public void login(){
        try {
            loginCreds();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect Details! Try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            login();
        }
    }

    public static void main(String[] args) {
        /*if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            System.setProperty("java.security.policy", "file:./global.policy");
            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry();
            ExamServer exam = (ExamServer) registry.lookup(name);
            System.out.println("Connected to server...");



        } catch (Exception e) {
            e.printStackTrace();
        }*/
        new Client();
    }

}


