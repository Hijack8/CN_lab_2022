import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class WeChat {
    Socket sock;
    BufferedReader reader;
    BufferedReader reader2;
    PrintWriter writer;
    //好友列表
    ArrayList<String> friends;

    String my_uname;

    public void go(){
        try{
            //连接服务器
            sock = new Socket("39.106.29.224", 5000);
            System.out.println(sock);
            //显示登录界面
            login login_m = new login(sock);
            login_m.go();
            //解析服务器返回的消息 看是否成功登录 或者是成功注册
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());

            while(true){
                reader = new BufferedReader(streamReader);
                String message = reader.readLine();
                System.out.println(message);
                int ID = get_ID(message);
                if(ID == 0){
                    //ID为0的消息是有关登录的指令
                    String instr = get_Instr(message);
                    if(instr.equals("logsuccess")){
                        //关闭登录界面
                        //显示主界面
                        System.out.println("log_success!!!!");
                        my_uname = login_m.log_success();

                        break;
                    }
                    else if(instr.equals("logfailed")){
                        login_m.log_failed();

                        //如果不成功登录，显示用户名或密码错误
                    }
                    else if(instr.equals("registersuccess")){
                        login_m.register_success();

                        //如果成功注册，显示成功注册
                    }
                    else if(instr.equals("registerfailed")){
                        //如果不成功注册，显示用户名或密码已注册
                        login_m.register_failed();
                    }
                }
                else{
                    //不管其他的消息
                }
            }
            //显示
            //main_win mainwin = new main_win();
            GUI_new gui = new GUI_new(sock,writer,my_uname);
            gui.show();

            //mainwin.go(friends);
        }
        catch (Exception ex){ex.printStackTrace();}


    }
    //得到消息的ID号
    public int get_ID(String message){
        return message.charAt(4) - '0';
    }
    //得到消息的指令号
    public String get_Instr(String message){
        try{
            for (String e : message.split(",")){
                System.out.println(e.substring(0,5));
                if(e.substring(0,5).equals("Instr")){
                    System.out.println(e.substring(6));
                    return e.substring(6);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }
    public static void main(String[] args){
        new WeChat().go();
    }
}
