package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;


public class RegisterFrame extends JFrame {
    private MyPanel contentPanel;
    private JTextField userTxt;
    private JPasswordField passwordTxt, confirmTxt;
    private JLabel usernameLb, passwdLb, confirmLb;
    private JButton yesBtn;
    RegisterFrame(){
        this.setTitle("注册");
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


        //账号标签
        usernameLb = new JLabel("user");
        usernameLb.setBounds(40, 46, 54, 28);
        contentPanel.add(usernameLb);

        //密码标签
        passwdLb = new JLabel("password");
        passwdLb.setBounds(20, 86, 70, 28);
        contentPanel.add(passwdLb);

        confirmLb = new JLabel("confirmed");
        confirmLb.setBounds(18, 137, 80, 28);
        contentPanel.add(confirmLb);

        userTxt = new JTextField();
        userTxt.setBounds(99, 50, 161, 25);
        contentPanel.add(userTxt);

        passwordTxt = new JPasswordField();
        passwordTxt.setBounds(99, 90, 161, 25);
        contentPanel.add(passwordTxt);

        confirmTxt = new JPasswordField();
        confirmTxt.setBounds(99, 140, 161, 25);
        contentPanel.add(confirmTxt);

        //注册按钮
        yesBtn = new JButton("注册");
        yesBtn.setBounds(95, 210, 80, 23);
        yesBtn.setBackground(Color.RED);
        yesBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        contentPanel.add(yesBtn);

        this.setLocationRelativeTo(null);
        this.setVisible(true);



    }

    public static void main(String[] args) {
        new RegisterFrame();
    }
    // override paintComponent方法,设置panel的背景图片
    private class MyPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //相对路径从src开始
            Image image = new ImageIcon("src/images.jpeg").getImage();

            // 跟随窗口调整背景图片大小
            g.drawImage(image,0, 0, this.getWidth(), this.getHeight(),this);
        }
    }
}


