package client;

import com.mysql.cj.log.Log;
import common.Response;
import common.ResponseStatus;
import common.UserStatus;

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
    private JButton loginBtn, cancelBtn;
    private JLabel signupLb;

    LoginFrame() {
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


        userTxt = new JTextField("用户名");
        userTxt.setBounds(89, 80, 161, 25);
        userTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (userTxt.getText().equals("用户名")) {
                    userTxt.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (userTxt.getText().equals("")) {
                    userTxt.setText("用户名");
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
                if (String.valueOf(passwordTxt.getPassword()).equals("密码")) {
                    passwordTxt.setText("");
                    passwordTxt.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (String.valueOf(passwordTxt.getPassword()).equals("")) {
                    passwordTxt.setEchoChar('\0');
                    passwordTxt.setText("密码");
                }
            }
        });
        passwordTxt.setBounds(89, 140, 161, 25);
        contentPanel.add(passwordTxt);

        //login按钮
        loginBtn = new JButton("登陆");
        loginBtn.setBounds(95, 210, 80, 23);
        loginBtn.setBackground(Color.RED);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });
        contentPanel.add(loginBtn);

        //取消按钮
        cancelBtn = new JButton("重置");
        cancelBtn.setBounds(210, 210, 80, 23);
        cancelBtn.setBackground(Color.GREEN);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                userTxt.setText("用户名");
                passwordTxt.setEchoChar('\0');
                passwordTxt.setText("密码");
            }
        });

        signupLb = new JLabel("<html><u>signup</u></html>");
        signupLb.setBounds(243, 248, 100, 20);
        signupLb.setOpaque(true);
        signupLb.setBorder(new EmptyBorder(0, 0, 0, 0));
        signupLb.setForeground(Color.BLUE);
        signupLb.setBackground(Color.WHITE);
        signupLb.setFont(new Font("宋体", Font.PLAIN, 12));
        signupLb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
                new RegisterFrame();
            }
        });
        contentPanel.add(signupLb);

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
            Image image = new ImageIcon("images/login.jpg").getImage();

            // 跟随窗口调整背景图片大小
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
    /**
     * 登录
     */
    @SuppressWarnings("unchecked")
    private void login() {
        if (userTxt.getText().equals("username")
                || passwordTxt.getPassword().toString().equals("password")) {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "用户名或密码为空",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
            userTxt.requestFocusInWindow();
            return;
        }

        if (!userTxt.getText().matches("^\\w*$")) {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "只能输入数字、字母、下划线",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
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

            UserStatus status = (UserStatus) response.getData("status");
            switch (status){
                case LOGIN_SUCCESS:
                    ConManager.currentUser = userTxt.getText();
                    //获取当前在线用户列表
                    ConManager.onlineUsers = (List<String>) response.getData("onlineUsers");

                    LoginFrame.this.dispose();
                    new ClientChatFrame();  //打开聊天窗体
                    break;
                case NO_EXIST:
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "用户名或密码不正确",
                            "登陆错误", JOptionPane.ERROR_MESSAGE);
                    break;
                case HAS_LOGGED_IN:
                    JOptionPane.showMessageDialog(LoginFrame.this, "This user has logged in", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "服务器内部错误，请稍后再试！！！", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }

}



