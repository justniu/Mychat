package client;

import common.Response;
import common.ResponseStatus;
import common.User;
import common.UserStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


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


        //username label
        usernameLb = new JLabel("username");
        usernameLb.setBounds(20, 46, 74, 28);
        contentPanel.add(usernameLb);

        //password label
        passwdLb = new JLabel("password");
        passwdLb.setBounds(20, 86, 70, 28);
        contentPanel.add(passwdLb);

        confirmLb = new JLabel("confirmed");
        confirmLb.setBounds(18, 137, 80, 28);
        contentPanel.add(confirmLb);

        userTxt = new JTextField();
        userTxt.setBounds(99, 50, 161, 25);
        userTxt.setOpaque(false);
        userTxt.setFont(new Font("Times New Roman", Font.BOLD, 12));
        contentPanel.add(userTxt);

        passwordTxt = new JPasswordField();
        passwordTxt.setBounds(99, 90, 161, 25);
        passwordTxt.setOpaque(false);
        contentPanel.add(passwordTxt);

        confirmTxt = new JPasswordField();
        confirmTxt.setBounds(99, 140, 161, 25);
        confirmTxt.setOpaque(false);
        contentPanel.add(confirmTxt);

        // OK button
        yesBtn = new JButton("OK");
        yesBtn.setBounds(95, 210, 80, 23);
        yesBtn.setBackground(Color.RED);
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(userTxt.getText().length() < 3){
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Username is not proper",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }else if(new String(passwordTxt.getPassword()).length() < 5){
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Password is not safe",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }else if(!(new String(passwordTxt.getPassword()).equals(new String(confirmTxt.getPassword())))){
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Two inputs are not equal",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    User user = new User(userTxt.getText(), new String(passwordTxt.getPassword()));
                    try {
                        registe(user);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
            Image image = new ImageIcon("src/register.jpg").getImage();

            // 跟随窗口调整背景图片大小
            g.drawImage(image,0, 0, this.getWidth(), this.getHeight(),this);
        }
    }


    private void registe(User user) throws ClassNotFoundException, IOException {
        RequestBody request = new RequestBody();
        request.setAction("userRegiste");
        request.setAttribute("user", user);

        //获取响应
        Response response = Requests.sendTextRequest(request);
        System.out.println(response);
        if( response.getStatus() == ResponseStatus.OK){
            UserStatus status = (UserStatus) response.getData("status");
            System.out.println("status"+status);
            switch(status){
                case REGISTE_SUCCESS:
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "恭喜您，您的账号注册成功,请牢记!!!",
                            "注册成功",JOptionPane.INFORMATION_MESSAGE);
                    this.setVisible(false);
                    break;
                default:
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "注册失败，请稍后再试！！！","服务器内部错误！",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(RegisterFrame.this,
                    "服务器内部错误，请稍后再试！！！", "注册失败", JOptionPane.ERROR_MESSAGE);
        }

    }

}


