import Handler.Handler;
import UIIG.UIwithHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
public class login extends JFrame {
    private lgpanel panel;
    private JLabel lab_account = new JLabel("账号:");
    private JTextField jta_text = new JTextField();
    private Font lab = new Font("宋体", Font.BOLD,30);
    private JLabel lat_password = new JLabel("密码:");
    private Font lat  = new Font("宋体", Font.BOLD,30);
    private JPasswordField jtb_text = new JPasswordField();
    private JButton btn_register = new JButton("注册");
    private JButton btn_land = new JButton("登陆");
    private Font btn = new Font("黑体",2,30);
    private static String ak1;
    private static String ak2;
    login() {
        this.setSize(400,260);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("登陆");
        this.setResizable(false);
        btn_register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ak1 = jta_text .getText();
                ak2 = String.valueOf(jtb_text .getPassword());
                String fileName = "users.dat";
                try {
                    FileWriter writer = new FileWriter(fileName,true);
                    BufferedWriter outWriter=new BufferedWriter(writer);
                    outWriter.write("ID:"+ak1+"&&"+"Password:"+ak2);
                    outWriter.newLine();
                    outWriter.close();
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                jta_text.setText("");
                jtb_text.setText("");
                JOptionPane.showMessageDialog(null,"注册成功！ 您的用户名是："+ak1+"密码是："+ ak2);
            }
        });
        btn_land.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File fileName = new File("users.dat");
                try {
                    FileReader inOne = new FileReader(fileName);
                    BufferedReader inTwo = new BufferedReader(inOne);
                    String s;
                    int judge = 1;
                    while ((s = inTwo.readLine()) != null) {
                        String ak = "ID:" + jta_text.getText() + "&&" + "Password:" + String.valueOf(jtb_text.getPassword());
                        if (s.equals(ak)) {
                            JOptionPane.showMessageDialog(null, "登录成功！");
                            jta_text.setText("");
                            jtb_text.setText("");
                            judge = 0;
                            dispose();
                            UIwithHandler ui= new UIwithHandler();
                            Handler handler=new Handler(ui);
                        }
                    }
                    inTwo.close();
                    inOne.close();
                    if (judge == 1) {
                        JOptionPane.showMessageDialog(null, "登陆失败！检查账号密码是否正确或请注册后登录", null, JOptionPane.ERROR_MESSAGE, null);
                        jta_text.setText("");
                        jtb_text.setText("");
                    }
                } catch (IOException event) {
                    JOptionPane.showMessageDialog(null, "系统错误" + event, null, JOptionPane.ERROR_MESSAGE, null);
                }
            }
        });
        init();
        panel = new lgpanel();   //添加面板
        panel.add(lab_account);
        panel.add(lat_password);
        panel.add(jta_text);
        panel.add(jtb_text);
        panel.add(btn_register);
        panel.add(btn_land);
        panel.setLayout(null);

        getContentPane().add(panel);
    }
    private void init() {
        lab_account.setSize(200,100);
        lab_account. setLocation(50,20);
        lab_account.setFont(lab);
        lat_password.setSize(200,100);
        lat_password.setLocation(50,80);
        lat_password.setFont(lat);
        jta_text.setSize(190,30);
        jta_text.setLocation(160,55);
        jtb_text.setSize(190,30);
        jtb_text.setLocation(160,115);
        btn_register.setSize(120, 40);
        btn_register.setLocation(50, 165);
        btn_land.setSize(120, 40);
        btn_land.setLocation(220, 165);
        btn_register.setFont(btn);
        btn_land.setFont(btn);
    }

    public static void main(String[] args) {
        new login().setVisible(true);
    }
}