package client;

import common.Response;
import common.ResponseStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class LoginFrame extends JFrame {
    private MyPanel contentPanel;
    private JTextField userTxt;
    private JPasswordField passwordTxt;
    //private JLabel usernameLb, passwdLb;
    private JButton loginBtn, cancelBtn, signUpBtn;

    LoginFrame() {
        this.setTitle("Login");
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


        userTxt = new JTextField("username");
        userTxt.setBounds(89, 80, 161, 25);
        userTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (userTxt.getText().equals("username")) {
                    userTxt.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (userTxt.getText().equals("")) {
                    userTxt.setText("username");
                }
            }
        });
        contentPanel.add(userTxt);

        passwordTxt = new JPasswordField("password");
        passwordTxt.setEchoChar('\0');
        passwordTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (String.valueOf(passwordTxt.getPassword()).equals("password")) {
                    passwordTxt.setText("");
                    passwordTxt.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (String.valueOf(passwordTxt.getPassword()).equals("")) {
                    passwordTxt.setEchoChar('\0');
                    passwordTxt.setText("password");
                }
            }
        });
        passwordTxt.setBounds(89, 140, 161, 25);
        contentPanel.add(passwordTxt);

        //login按钮
        loginBtn = new JButton("Login");
        loginBtn.setBounds(95, 210, 80, 23);
        loginBtn.setBackground(Color.RED);
        loginBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });
        contentPanel.add(loginBtn);

        //取消按钮
        cancelBtn = new JButton("Reset");
        cancelBtn.setBounds(210, 210, 80, 23);
        cancelBtn.setBackground(Color.GREEN);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                userTxt.setText("username");
                passwordTxt.setEchoChar('\0');
                passwordTxt.setText("password");
            }
        });

        signUpBtn = new JButton("<html><u>signup</u></html>");
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
    private class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //相对路径从src开始
            Image image = new ImageIcon("src/download.jpg").getImage();

            // 跟随窗口调整背景图片大小
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
    /**
     * 登录
     */
    @SuppressWarnings("unchecked")
    private void login() {
        if (userTxt.getText().length() == 0
                || passwordTxt.getPassword().length == 0) {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "账号和password是必填的",
                    "输入有误", JOptionPane.ERROR_MESSAGE);
            userTxt.requestFocusInWindow();
            return;
        }

        if (!userTxt.getText().matches("^\\w*$")) {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "账号必须是数字",
                    "输入有误", JOptionPane.ERROR_MESSAGE);
            userTxt.requestFocusInWindow();
            return;
        }

        RequestBody req = new RequestBody();
        req.setAction("userLogin");
        req.setAttribute("username", userTxt.getText());
        req.setAttribute("password", new String(passwordTxt.getPassword()));

        //获取响应
        Response response = null;
        try {
            response = Requests.sendTextRequest(req);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (response.getStatus() == ResponseStatus.OK) {
            //获取当前用户
            User user2 = (User) response.getData("user");
            if (user2 != null) { //登录成功
                ConManager.currentUser = user2;
                //获取当前在线用户列表
                ConManager.onlineUsers = (List<User>) response.getData("onlineUsers");

                LoginFrame.this.dispose();
                new ClientChatFrame();  //打开聊天窗体
            } else { //登录失败
                String str = (String) response.getData("msg");
                JOptionPane.showMessageDialog(LoginFrame.this,
                        str,
                        "login failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "服务器内部错误，请稍后再试！！！", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }

}



