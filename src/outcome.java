import Handler.Handler;
import UIIG.UIwithHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class outcome extends JFrame{
    private lgpanel opanel;
    private JLabel winner = new JLabel("获胜的是：");
    private JTextField idtext = new JTextField();
    private Font font = new Font("宋体", Font.BOLD,25);
    private JButton again = new JButton("再来一局");
    private JButton exit = new JButton("退出");
    private Font fbtn = new Font("黑体", Font.BOLD,18);
    outcome(){
        this.setSize(400,260);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        setTitle("结算");
        this.setResizable(false);
        opanel = new lgpanel();
        opanel.add(winner);
        opanel.add(idtext);
        opanel.add(again);
        opanel.add(exit);
        opanel.setLayout(null);

        getContentPane().add(opanel);

        again.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                dispose();
                UIwithHandler ui= new UIwithHandler();
                Handler handler=new Handler(ui);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                dispose();
                new login().setVisible(true);
            }
        });
        init();
    }

    private void result(int id) {
        if (id == 0)
            idtext.setText("获胜的是地主");
        else
            idtext.setText("获胜的是农民");
    }

    private void init(){
        winner.setSize(150,50);
        winner.setLocation(80,40);
        winner.setFont(font);
        again.setSize(120,50);
        again.setLocation(60,140);
        again.setFont(fbtn);
        exit.setSize(120,50);
        exit.setLocation(230,140);
        exit.setFont(fbtn);
    }

    public static void main(String[] args) {
        new outcome().setVisible(true);
    }
}
