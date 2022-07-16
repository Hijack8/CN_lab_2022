import java.io.*;
import java.net.*;
import java.util.*;

// tcp服务器
public class tcp_server
{
    //建立TCP连接的client输出的writer列表
    ArrayList<client_output> clientOutputStreams;
    ArrayList<String> name_list;//用户名列表 存储用户名
    ArrayList<String> password_list;//密码列表 存储密码
    ArrayList<String> user_online;

    String uname_send;
    static class client_output{
        int port;
        PrintWriter writer;
        Socket sock;
        OutputStream os;
        public client_output(int _port,PrintWriter _writer,Socket _sock,OutputStream _os){
            port = _port;
            writer = _writer;
            sock = _sock;
            os = _os;
        }
    }



    public tcp_server(){
        name_list = new ArrayList<String>();
        password_list = new ArrayList<String>();
    }
    //实现Runnable接口 为了给线程分配任务
    public class ClientHandler implements Runnable {
        //使用BufferedReader从Socket上读取数据
        BufferedReader reader;
        Socket sock;
        InputStreamReader isReader;
        InputStream is;
        //处理客户端信息的线程  主要应该是从这里修改
        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;

                is = sock.getInputStream();
                //连接到低层输入串流的InputStreamReader
                isReader = new InputStreamReader(is);
                //新建一个BufferReader用来读取clientSocket数据
                reader = new BufferedReader(isReader);
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        //线程具体的任务 这实际上式Runnable接口的唯一的抽象方法
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    int message_id = get_ID(message);
                    if(message_id == 1){
                        System.out.println("register message");
                        //注册
                        String uname = getUname(message);
                        String password = getPassword(message);
                        int flg = name_list.indexOf(uname);
                        if(flg == -1){
                            name_list.add(uname);
                            password_list.add(password);
                            //回复一个注册成功？
                            registersuccess(uname);

                        }
                        else{
                            registerfailed(uname);
                        }

                    }
                    else if(message_id == 2){
                        System.out.println("log_in message");
                        //登录
                        String uname = getUname(message);
                        String password = getPassword(message);
                        int index = name_list.indexOf(uname);
                        if(index == -1){
                            //登录失败
                            loginfailed(uname);
                        }
                        else if(password_list.get(index).equals(password)){
                            //登录成功
                            loginsuccess(uname);
                        }
                        else{
                            //密码错误
                            loginfailed(uname);
                        }
                    }
                    else if(message_id == 4){
                        System.out.println(message);
                        System.out.println("want to send file");
                        String uname_receiver = getUname(message);
                        uname_send = getSender(message);
                        String file_name = getFileName(message);
                        //询问接收方 是否接收
                        accept_or_not(uname_receiver,file_name);
                        //getfile();
                        //System.out.println("下载好了");
                    }
                    else if(message_id == 5){
                        if(message.substring(message.length() - 2).equals("NO")){
                            //停止发送
                            System.out.println("对方拒绝接收文件，请停止发送");
                            String message_n = "{ID:4,NONONO";
                            tellSomeone(message_n,uname_send);
                        }
                        else{
                            System.out.println("开始传输文件");
                            String message_y = "{ID:4}";
                            String uname = getUname(message);

                            tellSomeone(message_y,uname_send);
                        }

                    }
                    else if(message_id == 6){
                        getfile("lilei");
                    }
                    else if(message_id == 3){
                        //正常消息 发送到所有用户
                        System.out.println("normal message!!");
                        System.out.println(message);
                        String msg = message.substring(5);
                        msg = "{ID:3" + msg;
                        tellEveryone(msg);
                    }
                    //System.out.println("read " + message);
                    //把消息分发到每个tcp连接
                    //tellEveryone(message);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        public void accept_or_not(String uname,String fileName){
            String message = "{ID:2," + "Uname" + uname_send + ",FileName:" + fileName;
            tellSomeone(message,uname);
        }
        public String getfile(String uname){
            try{
                int index = user_online.indexOf(uname);
                if(index == -1)return "error";
                OutputStream os = clientOutputStreams.get(index).os;
                PrintWriter writer = clientOutputStreams.get(index).writer;
                System.out.println("#test1#");
                //isReader.close();
                //BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
                //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D://"+System.currentTimeMillis()+".png"));
                //OutputStream os = clientOutputStreams.get(index).sock.getOutputStream();
                //BufferedOutputStream bos = new BufferedOutputStream(os);
                System.out.println("#test2#");
                byte[] buffer = new byte[10];
                System.out.println("#test2.5#");
                //System.out.println(bis.read(buffer));
                int len;
                BufferedInputStream bis = new BufferedInputStream(is);
                while ((len = bis.read(buffer)) == 10)
                {
                    System.out.println("#test3#");
                    System.out.println(len);
                    os.write(buffer);
                    os.flush();
                }
                //os.write(buffer);
                os.write(buffer);
                os.flush();
                System.out.println("#test4#");
                //bis.close();
                writer.write("        ");
                writer.flush();
                //isReader = new InputStreamReader(sock.getInputStream());
                System.out.println("文件转发完成");
                //下载完后传给相应的客户端s
            }catch (Exception ex){
                System.out.println("error");
                ex.printStackTrace();
            }
            return "a.txt";
        }
        //从消息中提取到Uname
        public String getUname(String message){
            try{
                for (String e : message.split(",")){
                    System.out.println(e.substring(0,5));
                    if(e.substring(0,5).equals("Uname")){
                        System.out.println(e.substring(6));
                        return e.substring(6);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "error";
        }
        //从请求发送的消息中提取到发送方
        public String getSender(String message){
            try{
                for(String e : message.split(",")){
                    System.out.println(e.substring(0,5));
                    if(e.length() >= 7){
                        if(e.substring(0,6).equals("Uname0")){
                            return e.substring(7);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "error";
        }
        //从消息中提取到FileName
        public String getFileName(String message){
            try{
                for(String e : message.split(",")){
                    if(e.length() >= 9 && e.substring(0,8).equals("FileName")){
                        return e.substring(9);
                    }
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return "error";
        }
        //从消息中提取到Password
        public String getPassword(String message){
            System.out.println(message);
            try{
                for (String e : message.split(",")){
                    System.out.println(e.substring(0,5));
                    if(e.substring(0,4).equals("Pass")){
                        System.out.println(e.substring(5));
                        int len = e.length() ;
                        return e.substring(5,len - 1);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "error";
        }
        //注册成功 向套接字发送 ID:0 Instr:registersuccess Uname: uname 告诉这个用户注册成功了
        public void registersuccess(String Uname){
            String message = "{ID:0,Instr:registersuccess,Uname:" + Uname + "}";
            tellEveryone(message);
        }
        //登录成功 向套接字发送 ID:0 Instr:logsuccess Uname: uname 告诉这个用户注册成功了
        public void loginsuccess(String Uname){
            user_online.add(Uname);
            String message = "{ID:0,Instr:logsuccess,Uname:" + Uname + "}";
            tellEveryone(message);
            int num = clientOutputStreams.size();
            String friends_message = "{ID:1," + "Num:" + Integer.toString(num) + ",";
            for(int i  = 0;i < user_online.size();i++){
                friends_message = friends_message + user_online.get(i) + ",";
            }
            friends_message = friends_message + "}";
            try{
                Thread.sleep(100);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            tellEveryone(friends_message);
        }
        //登录失败 向套接字发送 ID:0 Instr:logfailed Uname: uname 告诉这个用户注册成功了
        public void loginfailed(String Uname){
            String message = "{ID:0,Instr:logfailed,Uname:" + Uname + "}";
            tellEveryone(message);
        }
        //注册失败 向套接字发送 ID:0 Instr:registerfailed Uname: uname 告诉这个用户注册成功了
        public void registerfailed(String Uname){
            String message = "{ID:0,Instr:registerfailed,Uname:" + Uname + "}";
            tellEveryone(message);
        }
    }

    //主函数
    public static void main(String[] args) {
        new tcp_server().go();
    }


    //服务器运行的主要代码
    public void go() {
        clientOutputStreams = new ArrayList<client_output>();
        user_online = new ArrayList<String>();
        name_list.add("lilei");
        password_list.add("123");
        try {
            //服务器会专门开一个端口用来监听请求 这里这个端口是5000 客户端想要请求连接必须要请求5000端口
            ServerSocket serverSock = new ServerSocket(5000);
            // 这里为了实现群聊的功能，允许多个TCP连接，所以这里使用了while循环
            while(true) {
                //等待客户端的TCP连接   等待的时候是闲置的，用户连接上之后就会返回一个Socket 并且这个socket的端口并不是5000
                Socket clientSocket = serverSock.accept();
                //字符数据和字节之间的连接桥梁 衔接string 和 socket
                int port = clientSocket.getLocalPort();
                OutputStream os = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(os);
                client_output u_w = new client_output(port,writer,clientSocket,os);
                clientOutputStreams.add(u_w);
                //新建一个Thread 并且给这个Thread分配一个任务，也就是这个ClientHandler
                Thread t = new Thread(new ClientHandler(clientSocket));
                //启动线程，这个线程才会正常工作，并且任务ClientHeadler会被分配到执行空间中
                t.start();
                System.out.println("got a connection");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    //得到消息的ID号
    public int get_ID(String message){
        return message.charAt(4) - '0';
    }
    public void tellEveryone(String message) {
        //使用迭代器遍历ArrayList
        Iterator it = clientOutputStreams.iterator();
        //迭代将clientOutputStream的东西全部输出
        while (it.hasNext()) {
            try {
                client_output co = (client_output) it.next();
                PrintWriter writer = co.writer;
                //PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                //刷新这个流 将缓冲区中的数据强行发送到目的地
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
    public void tellSomeone(String message,String uname){
        int x = user_online.indexOf(uname);
        if(x == -1){
            System.out.println("no user");
            System.out.println(uname);
            return;
        }
        System.out.println(x);
        System.out.println("tellSomeone:");
        try{
            client_output co = (client_output) clientOutputStreams.get(x);
            PrintWriter writer = co.writer;
            writer.println(message);
            writer.flush();
        }catch (Exception ex){ex.printStackTrace();}

    }
}