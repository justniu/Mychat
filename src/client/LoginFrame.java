package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private MyPanel contentPanel;
    private JTextField userTxt;
    private JPasswordField passwordTxt;
    //private JLabel usernameLb, passwdLb;
    private JButton loginBtn, cancelBtn, signUpBtn;
    LoginFrame(){
        this.setTitle("登陆");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        contentPanel = new MyPanel();
        // contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPanel);

        contentPanel.setLayout(null);
        contentPanel.setOpaque(true);
        contentPanel.setFocusable(true);//设置初始光标


        userTxt = new JTextField("用户名或邮箱");
        userTxt.setBounds(89, 80, 161, 25);
        userTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(userTxt.getText().equals("用户名或邮箱")){
                    userTxt.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(userTxt.getText().equals("")){
                    userTxt.setText("用户名或邮箱");
                }
            }
        });
        contentPanel.add(userTxt);

        passwordTxt = new JPasswordField("密码");
        passwordTxt.setEchoChar('\0');
        passwordTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(String.valueOf(passwordTxt.getPassword()).equals("密码")){
                    passwordTxt.setText("");
                    passwordTxt.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(String.valueOf(passwordTxt.getPassword()).equals("")){
                    passwordTxt.setEchoChar('\0');
                    passwordTxt.setText("密码");
                }
            }
        });
        passwordTxt.setBounds(89, 140, 161, 25);
        contentPanel.add(passwordTxt);

        //登陆按钮
        loginBtn = new JButton("登陆");
        loginBtn.setBounds(95, 210, 80, 23);
        loginBtn.setBackground(Color.RED);
        loginBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        contentPanel.add(loginBtn);

        //取消按钮
        cancelBtn = new JButton("取消");
        cancelBtn.setBounds(210, 210, 80, 23);
        cancelBtn.setBackground(Color.GREEN);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    userTxt.setText("用户名或邮箱");
                    passwordTxt.setEchoChar('\0');
                    passwordTxt.setText("密码");
            }
        });

        signUpBtn = new JButton("signup");
        signUpBtn.setBounds(220, 250, 100, 20);
        signUpBtn.setOpaque(true);
        signUpBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
        signUpBtn.setBackground(Color.WHITE);
        contentPanel.add(signUpBtn);

        contentPanel.add(cancelBtn);
        this.setLocationRelativeTo(null);
        this.setVisible(true);



    }

    public static void main(String[] args) {
        new LoginFrame();
    }
// override paintComponent方法,设置panel的背景图片
    private class MyPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //相对路径从src开始
            Image image = new ImageIcon("src/download.jpg").getImage();

            // 跟随窗口调整背景图片大小
            g.drawImage(image,0, 0, this.getWidth(), this.getHeight(),this);
        }
    }
}
