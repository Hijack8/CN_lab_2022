import javax.swing.*;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.tree.TreePath;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.event.MenuKeyEvent;
import java.awt.event.*;

public class GUI_new {
    public JPanel panel1;
    public JTextField textField1;
    public JButton button1;
    public JList list1;
    public JScrollPane qScroller;
    public JTextArea textArea1;
    public JTree jTree;
    public DefaultMutableTreeNode top;
    //public DefaultTreeModel top;
    public DefaultTreeModel model;
    OutputStream os;
    Socket sock;
    PrintWriter writer;
    BufferedReader reader;
    ArrayList<String> online_users;
    InputStreamReader isr;
    //自己的用户名
    String my_uname;

    InputStream is;

    //右键菜单
    JPopupMenu menu;

    //发送文件
    File file;
    String fileName_rev;
    int flg = 1;
    int online_num;
    public GUI_new(Socket socket,PrintWriter _writer,String name){
        sock = socket;
        my_uname = name;
        try{
            os = sock.getOutputStream();
            writer = new PrintWriter(os);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("WeChat");
        //frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



    public class JPopmenuActionListener implements ActionListener
    {
        int mod;
        String uname;
        public JPopmenuActionListener(int _mod,String _name){
            mod = _mod;
            uname = _name;
        }
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(mod == 1){
                //语音聊天

            }
            else{
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setApproveButtonText("确定");
                fileChooser.setDialogTitle("打开文件");
                fileChooser.showOpenDialog(new JFrame());
                file = fileChooser.getSelectedFile();
                //传文件
                //请求发送文件
                try{
                    writer.println(("{ID:4," + "Uname:" + uname + ",Uname0:" + my_uname + ",FileName:" + file.getName()));
                    writer.flush();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                //send_file(uname);
            }
        }
    }

    void send_file(){
        //File file = null;

        System.out.println(file.getPath());

        //发送文件
        flg = 1;
        System.out.println("test_send_1");
        try{
            //OutputStream os = sock.getOutputStream();
            //BufferedOutputStream bos = new BufferedOutputStream(os);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //InputStreamReader isr = new InputStreamReader(bis);
            //BufferedReader Breader = new BufferedReader(isr);
            byte[] b = new byte[10];
            int i = -1;
            while((i = bis.read(b)) == 10){
                System.out.println("len = ");
                System.out.println(i);
                os.write(b,0,i);
                os.flush();
            }
            //bis.read(b);
            os.write(b);
            os.flush();
            writer.write("   ");
            writer.flush();
            System.out.println("文件发送完毕");
            //关闭流
            bis.close();
            //bos.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }



    }
    //右键菜单
    class popUpMenu extends JPopupMenu{
        JMenuItem op1;
        JMenuItem op2;
        public popUpMenu(String uname){
            op1 = new JMenuItem("语音聊天");
            op2 = new JMenuItem("文件发送");
            add(op1);
            add(op2);

            op1.addActionListener(new JPopmenuActionListener(1,uname));
            op2.addActionListener(new JPopmenuActionListener(2,uname));
            /*
            op1.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        System.out.println("listening");
                    }
                }
            });

            op2.addMenuKeyListener(new MenuKeyListener(){
                public void menuKeyTyped(MenuKeyEvent e) {
                    //
                }
                public void menuKeyPressed(MenuKeyEvent e) {
                    //
                }
                public void menuKeyReleased(MenuKeyEvent e) {
                    System.out.println("?????");
                    //System.out.println("KeyReleased");
                }});*/
        }
    }




    // 监听消息，更新好友列表和收到的信息
    public class incoming_Reader implements Runnable{
        public void run(){
            try{
                is = sock.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                while(true){

                    try{
                        reader = new BufferedReader(isr);
                        String message = reader.readLine();
                        System.out.println(message);
                        /*      测试jTree的自动更新     */
                    /*
                    Thread.sleep(5000);
                    System.out.println("creat a node");
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode("hello");
                    model.insertNodeInto(node,top,0);
                    */

                        int ID = get_ID(message);
                        if(ID == 0){
                            //登录有关消息  不管
                        }
                        else if(ID == 1){
                            //更新好友列表
                            update_onlinelist(message);
                            System.out.println(online_users);
                            top.removeAllChildren();

                            for(int i = 0;i < online_num;i++){
                                DefaultMutableTreeNode node;
                                if(online_users.get(i).equals(my_uname)){
                                    node = new DefaultMutableTreeNode(online_users.get(i) + "(me)");
                                    if(node.isNodeChild(top))continue;
                                    model.insertNodeInto(node,top,0);
                                }
                                else{
                                    node = new DefaultMutableTreeNode(online_users.get(i));
                                    if(node.isNodeChild(top))continue;
                                    model.insertNodeInto(node,top,0);
                                    //model.addTreeModelListener(node,myTreeModelListener);
                                }


                            }


                        }
                        else if(ID == 2){
                            //询问接收文件
                            String uname_sender = getUname(message);
                            fileName_rev = getFileName(message);
                            int rst = JOptionPane.showConfirmDialog(null, uname_sender + "想要给你传输文件" + fileName_rev, "是否接收", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                            if(rst == JOptionPane.OK_OPTION){
                                //正常接收文件
                                String message_yes = "{ID:5,Uname:" + my_uname;
                                System.out.println("yes");
                                writer.println(message_yes);
                                writer.flush();
                                getfile();
                            }
                            else{
                                //拒绝接收文件
                                String message_no = "{ID:5,NONONO";
                                System.out.println("no");
                                writer.println(message_no);
                                writer.flush();
                            }
                        }
                        else if(ID == 4){
                            if(message.substring(message.length() - 2).equals("NO")){
                                //对方拒绝接收文件
                                System.out.println("对方不接受的呀");
                                JOptionPane.showConfirmDialog(null, "对方拒绝接收文件", "拒绝接收", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
                            }
                            else{
                                //可以发送文件了
                                System.out.println("OK to send");
                                writer.println("{ID:6}");
                                writer.flush();
                                send_file();
                            }

                        }
                        else if(ID == 3){
                            textArea1.append(message.substring(5) + "\n");
                        }
                    }catch (Exception ex){
                        System.out.println("error");
                        ex.printStackTrace();
                    }

                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }
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
    public void getfile(){
        try{
            //BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
            FileOutputStream fos = new FileOutputStream("D://"+ fileName_rev);
            //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream();
            //PrintWriter Pwriter = new PrintWriter(bos);
            System.out.println("#test2#");
            byte[] buffer = new byte[10];
            System.out.println("#test2.5#");
            //System.out.println(bis.read(buffer));
            int len;
            while ((len = is.read(buffer)) == 10)
            {
                System.out.println("#test3#");
                fos.write(buffer);
                fos.flush();
            }
            fos.write(buffer);
            System.out.println("#test4#");
            //bis.close();
            fos.flush();
            fos.close();
            System.out.println("文件接收完成");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void update_onlinelist(String message){
        online_users = new ArrayList<String>();
        int i = 0;
        message = message.substring(0,message.length() - 1);
        for(String e:message.split(",")){
            i++;
            if(i == 2){
                online_num = Integer.parseInt(e.substring(4));
            }
            else if(i > 2) {
                online_users.add(e);
            }
        }
    }

    public void show(){
        JFrame frame = new JFrame("WeChat");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setPreferredSize(new Dimension(700, 600));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.CENTER);



        textArea1 = new JTextArea(20,30);
        //textArea1.setPreferredSize(new Dimension(10, 19));
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setEditable(false);

        final JScrollPane scrollPane1 = new JScrollPane(textArea1);
        //qScroller = new JScrollPane(incoming);
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//设置竖直滚动
        scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);//设置水平滚动
        //scrollPane1.setViewportView(textArea1);

        panel2.add(scrollPane1, BorderLayout.CENTER);

        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.SOUTH);
        textField1 = new JTextField();
        panel3.add(textField1, BorderLayout.CENTER);
        button1 = new JButton();
        button1.setText("发送");
        button1.addActionListener(new SendButtonListener());

        panel3.add(button1, BorderLayout.EAST);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel1.add(panel4, BorderLayout.EAST);
        list1 = new JList();
        list1.setDropMode(DropMode.ON);
        list1.setInheritsPopupMenu(false);
        list1.setLayoutOrientation(1);
        list1.setMinimumSize(new Dimension(5, 5));
        list1.setPreferredSize(new Dimension(100, 100));


        top = new DefaultMutableTreeNode("好友列表");
        //DefaultMutableTreeNode node = new DefaultMutableTreeNode("hello");
        //top.add(node);


        jTree = new JTree(top);
        model = (DefaultTreeModel) jTree.getModel();
        jTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
                    if (node1 == null) return;
                    Object nodeInfo = node1.getUserObject();
                    String node_message = (String)nodeInfo;
                    if(node_message.equals("好友列表") || node_message.substring(node_message.length() - 4).equals("(me)")){
                        //不做操作
                    }
                    else{
                        //弹出菜单
                        System.out.println("menu_out");
                        popUpMenu menu = new popUpMenu(node_message);
                        menu.show(e.getComponent(),e.getX(),e.getY());
                    }
                    System.out.println("2click");
                    // Cast nodeInfo to your object and do whatever you want
                }
            }
        });
        //新建一个线程更新好友列表
        Thread t = new Thread(new incoming_Reader());
        t.start();




        panel1.add(jTree, BorderLayout.WEST);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
    public void add_node(){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("hello");
        top.add(node);
    }
    //得到消息的ID号
    public int get_ID(String message){
        return message.charAt(4) - '0';
    }


    //发送聊天信息 监听send按钮
    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                //写入是哪个用户说的话：
                writer.println("{ID:3##" + my_uname + " says:");
                writer.flush();
                //取得对话框中的信息，写入
                writer.println("{ID:3##" + textField1.getText());
                writer.flush();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            textField1.setText("");
            textField1.requestFocus();
        }
    }
    //线程的任务 读取收到的数据
    class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("client read " + message);
                    textArea1.append(message + "\n");
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
