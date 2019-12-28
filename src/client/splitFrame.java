package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class splitFrame extends JFrame implements ActionListener{
    JPanel lTree, c, r, leftPane;
    JButton jb;
    int count = 0;

    public splitFrame() {
        this.setSize(new Dimension(400, 200));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container con = this.getContentPane();
        con.setLayout(new BorderLayout());

        this.leftPane = new JPanel(new FlowLayout(0, 0,FlowLayout.LEFT));
        this.lTree = new JPanel(new BorderLayout(0, 0));
        this.lTree.setBackground(Color.white);
        this.lTree.setPreferredSize(new Dimension(100,this.getHeight()));
        this.leftPane.add(this.lTree, BorderLayout.WEST);

        this.c = new JPanel();
        this.c.setBackground(Color.white);
        this.c.setPreferredSize(new Dimension(20,this.getHeight()));
        this.c.setLayout(new FlowLayout(2, 0,FlowLayout.RIGHT));
        jb = new JButton("<");
        jb.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
        this.c.add(jb);
        this.jb.addActionListener(this);
        this.jb.setFocusable(false);
        this.leftPane.add(this.c, BorderLayout.EAST);

        con.add(leftPane, BorderLayout.WEST);

        this.r = new JPanel();
        this.r.setBackground(Color.orange);
        this.r.setPreferredSize(new Dimension(200,this.getHeight()));
        con.add(this.r, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//其中org.jvnet.substance.skin为包名，SubstanceSaharaLookAndFeel为皮肤类名
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new splitFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub
        if (this.count == 0) {
            this.leftPane.remove(this.lTree);
            this.leftPane.repaint();
            this.validate();
            this.jb.setText(">");
            this.count = 1;
        } else {
            this.leftPane.removeAll();
            this.leftPane.add(this.lTree,BorderLayout.WEST);
            this.leftPane.add(this.c,BorderLayout.EAST);
            this.leftPane.repaint();
            this.validate();
            this.jb.setText("<");
            this.count = 0;
        }
    }
}
