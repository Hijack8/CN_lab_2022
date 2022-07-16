import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class login {
    Socket socket;
    JFrame frame;
    JTextField name;//用户名
    JTextField password;//密码
    JButton log_in_button;//登录
    JButton register_button;//注册
    JLabel name_label;
    JLabel password_label;
    JPanel jp_1;
    JPanel jp_2;
    BufferedReader reader;
    PrintWriter writer;
    //构造函数

    //自己的用户名
    String my_uname;
    public login(Socket sock){
        frame = new JFrame("WeChat登录");
        name = new JTextField(20);
        password = new JTextField(20);
        log_in_button = new JButton("登录");
        register_button = new JButton("注册");
        log_in_button.addActionListener(new login_Listener());
        register_button.addActionListener(new register_Listener());
        name_label = new JLabel("用户名");
        password_label = new JLabel("密码");
        jp_1 = new JPanel(new GridLayout(2,3));
        jp_2 = new JPanel();
        //这个socket复制尽量放在构造函数中
        socket = sock;
        // tcp请求连接 服务器监听的端口  5000
        try{
            System.out.println("hello?");
            System.out.println(socket);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            //读取数据
            reader = new BufferedReader(streamReader);
            //写入数据
            writer = new PrintWriter(socket.getOutputStream());
            // System.out.println("port:6000 linked!!!!!!!!!!!");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //显示页面
    public void show(){
        jp_1.add(name_label);
        jp_1.add(name);
        jp_1.add(new Label());
        jp_1.add(password_label);
        jp_1.add(password);
        jp_1.add(new Label());
        jp_2.add(log_in_button);
        jp_2.add(register_button);
        frame.getContentPane().add(BorderLayout.CENTER,jp_1);
        frame.getContentPane().add(BorderLayout.SOUTH,jp_2);

        frame.setSize(650, 300);
        frame.setVisible(true);
    }
    public void go(){
        //socket = sock;
        show();
    }

    //登录按钮
    public class login_Listener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            //尝试登录，建立连接并发送用户名密码
            try{
                System.out.println("log_in");
                String username_text = name.getText();
                String password_text = password.getText();
                //构造登录包
                String login_text = "{ID:2,Uname:" + username_text + ',' + "Pass:" + password_text + "}";
                my_uname = username_text;
                writer.println(login_text);
                writer.flush();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }


        }
    }
    //注册按钮
    public class register_Listener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            //尝试注册，建立连接并发送用户名密码
            try{
                System.out.println("register");
                String username_text = name.getText();
                String password_text = password.getText();
                //构造注册包
                String register_text = "{ID:1,Uname:" + username_text + ',' + "Pass:" + password_text + "}";
                writer.println(register_text);
                writer.flush();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //登录成功
    public String log_success(){
        System.out.println("log_success");
        //关闭登录界面
        frame.dispose();
        return my_uname;
    }
    //登录失败，用户名或密码错误
    public void log_failed(){
        JOptionPane.showMessageDialog(null, "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
        System.out.println("log_failed");
    }
    //注册成功
    public void register_success(){
        JOptionPane.showMessageDialog(null, "恭喜你获得账户，请记住您的用户名和密码", "注册成功", JOptionPane.YES_NO_OPTION);
        System.out.println("register_success");
    }
    //注册失败
    public void register_failed(){
        JOptionPane.showMessageDialog(null, "用户名已经存在，请更换用户名", "注册失败", JOptionPane.ERROR_MESSAGE);
        System.out.println("register_failed");

    }

}
