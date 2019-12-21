package client;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        new LoginFrame();
        Requests.connect();
    }

}
