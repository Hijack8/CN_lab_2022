# <cneter>基于JAVA Socket编程的聊天程序</center>

<center>计算机93 李云广</center>

## 1 实验原理

1. 使用TCP连接服务器和客户端，使用Java Socket实现客户端和服务器的信息传输。
2. Java把Socket作为一个普通的文件来看待，使用标准输入输出流来进行Socket的读取和写入。
3. 服务器开放一个5000端口等待客户端的连接，每有一个客户端连接上服务器，服务器都会新开一个线程来处理这个TCP连接。
4. 对于文件的处理Java也是使用的标准输入输出流，服务器仅仅用来转发收到的文件。

## 2 实验完成情况

1. 实现了客户端和服务器两端的聊天程序。
2. 实现了客户端的UI界面的设计。
3. 在客户端实现了登录和注册的功能。
4. 实现了客户端之间的文件传输。
5. 实现了客户端之间的群聊、私聊的功能。
6. 实现了一个在线的好友列表，在有新的好友上线的时候会更新好友列表。

## 3 实验结果演示

### 3.1 登录和注册

首先是运行程序后的第一个界面。

![image-20220517100842766](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517100842766.png)

输入用户名密码点击注册即可完成注册。

![image-20220517100934238](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517100934238.png)

![image-20220517100941375](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517100941375.png)

如果注册的用户名与服务器存储的用户名重复会返回错误：

![image-20220517103409074](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517103409074.png)

注册之后用户名就存储在了服务器中，下一次就可以直接使用这个用户名进行登录，但是用户名或密码输入错误也会返回错误：

![image-20220517103607906](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517103607906.png)

如果是正常登录会出现以下界面：

![image-20220517101019644](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517101019644.png)

登录后的界面是这样的，有一个在线的好友列表，可以显示当前的在线好友。

### 3.2 群聊和私聊

分别注册李雷和丽丽两个账号，进行登录，显示结果如下:

![image-20220517101421966](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517101421966.png)

分别使用lilei和lili发送消息：

![image-20220517101639991](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517101639991.png)

这样就实现了私聊的功能。

同时登录多个账户进行共同聊天，就实现了群聊的功能：


![image-20220517101942887](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517101942887.png)

### 3.3 文件传输

在线好友列表的每一个好友都是可以进行双击的，双击效果如下：

![image-20220517102111043](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517102111043.png)

由于时间原因，语音聊天我仅仅写出了UI框架但是没有实现其功能，所以这里仅仅演示文件发送：

点击文件发送会出现一个文件选择框：

![image-20220517102226635](C:/Users/dell/AppData/Roaming/Typora/typora-user-images/image-20220517102226635.png)

即可选择文件进行发送：

选择后我设置的接收端需要确定是否可以接收文件，如果不能接收文件就会在发送端返回错误。

接收端：（点击取消）

![image-20220517102349098](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517102349098.png)

发送端：

![image-20220517102424681](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517102424681.png)

如果接收端可以正常接收文件，则接收端点击确定，文件直接发送至接收端，在这里我默认发送到接收端的D盘根目录下：

![image-20220517102601010](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517102601010.png)

可以看到文件正常传输了。

## 4 实验关键思路以及代码解释

我设计的Socket编程传输的每个包的格式基本是这样的：

```
{ID:X,....}
```

通过开始一个ID号来标识这是一个什么包。

### 4.1 登录和注册

对于登录和注册，发送的消息是有一个专门的ID号的：

| 客户端发送的ID | 类型     |
| -------------- | -------- |
| 1              | 注册消息 |
| 2              | 登录消息 |

| 服务器发送的ID | 类型             |
| -------------- | ---------------- |
| 0              | 登录和注册的指令 |

注册：

![image-20220517104900363](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517104900363.png)

登录：

![image-20220517105019070](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517105019070.png)

#### 抓包示例

![image-20220517153108835](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153108835.png)

![image-20220517153126845](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153126845.png)

![image-20220517153229665](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153229665.png)



#### 服务器代码

* 注册

```java
if(message_id == 1){
    System.out.println("register message");
    //注册
    String uname = getUname(message);
    String password = getPassword(message);
    //找找看用户名是不是已经存在
    int flg = name_list.indexOf(uname);
    if(flg == -1){
        name_list.add(uname);
        password_list.add(password);
        //回复一个注册成功
        registersuccess(uname);

    }
    else{
        //注册失败
        registerfailed(uname);
    }

}
```

```java
//注册成功 向套接字发送 ID:0 Instr:registersuccess Uname: uname 告诉这个用户注册成功了
public void registersuccess(String Uname){
    String message = "{ID:0,Instr:registersuccess,Uname:" + Uname + "}";
    tellEveryone(message);
}
//注册失败 向套接字发送 ID:0 Instr:registerfailed Uname: uname 告诉这个用户注册成功了
public void registerfailed(String Uname){
    String message = "{ID:0,Instr:registerfailed,Uname:" + Uname + "}";
    tellEveryone(message);
}
```

* 登录

```java
else if(message_id == 2){
    System.out.println("log_in message");
    //登录
    String uname = getUname(message);
    String password = getPassword(message);
    int index = name_list.indexOf(uname);
    if(index == -1){
        //用户名错误 登录失败
        loginfailed(uname);
    }
    else if(password_list.get(index).equals(password)){
        //登录成功
        loginsuccess(uname);
    }
    else{
        //密码错误 登录失败
        loginfailed(uname);
    }
}
```

```java
//登录成功 向套接字发送 ID:0 Instr:logsuccess Uname: uname 告诉这个用户注册成功了
public void loginsuccess(String Uname){
    user_online.add(Uname);
    String message = "{ID:0,Instr:logsuccess,Uname:" + Uname + "}";
    tellEveryone(message);
    //发送 更新好友列表的信息
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
```

#### 客户端代码：

```java
int ID = get_ID(message);
if(ID == 0){
    //ID为0的消息是有关登录的指令
    String instr = get_Instr(message);
    
    if(instr.equals("logsuccess")){
        //成功登录
        //关闭登录界面
        //显示主界面
        System.out.println("log_success!!!!");
        my_uname = login_m.log_success();
        break;
    }
    else if(instr.equals("logfailed")){
        //如果不成功登录，显示用户名或密码错误
        login_m.log_failed();
    }
    else if(instr.equals("registersuccess")){
        //如果成功注册，显示成功注册
        login_m.register_success();
    }
    else if(instr.equals("registerfailed")){
        //如果不成功注册，显示用户名或密码已注册
        login_m.register_failed();
    }
}
```

```java
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
```

### 4.2 群聊私聊

本次实验实现的群聊非常简单，直接使用服务器对于接收到的所有聊天消息拷贝一份发送给每个用户：

```java
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
```

当然也有发送给某一个用户消息的方法：

```java
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
```

实现聊天需要一定的格式：

| 客户端发送的ID | 类型     |
| -------------- | -------- |
| 3              | 普通消息 |

| 服务器发送的ID | 类型     |
| -------------- | -------- |
| 3              | 普通消息 |

![image-20220517110733933](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517110733933.png)

#### 抓包分析

![image-20220517153555878](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153555878.png)

![image-20220517153642987](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153642987.png)

#### 服务器代码

```java
else if(message_id == 3){
    //正常消息 发送到所有用户
    System.out.println("normal message!!");
    System.out.println(message);
    String msg = message.substring(5);
    msg = "{ID:3" + msg;
    tellEveryone(msg);
}
```

#### 客户端代码

```java
else if(ID == 3){
    //更新聊天框
    textArea1.append(message.substring(5) + "\n");
}
```

### 4.3 文件发送

本实验实现的文件发送非常复杂，需要两端类似握手的操作：

| 客户端发送的ID | 类型         |
| -------------- | ------------ |
| 4              | 请求发送文件 |
| 5              | 可以接收文件 |

| 服务器发送的ID | 类型             |
| -------------- | ---------------- |
| 2              | 询问是否接收文件 |
| 4              | 允许发送文件     |

* 正常发送文件 lili -> lilei

![image-20220517112022438](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517112022438.png)

* 拒接接收文件

![image-20220517112122678](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517112122678.png)

#### 抓包分析

* jack想给lilei发送test.jpg

![image-20220517153736124](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153736124.png)

* 服务器询问lilei

![image-20220517153831943](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153831943.png)

* lilei回复服务器 可以接收

![image-20220517153913530](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153913530.png)

* 服务器告诉jack可以发送

![image-20220517153945219](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517153945219.png)

#### 服务器代码

```java
else if(message_id == 4){
    System.out.println(message);
    System.out.println("want to send file");
    String uname_receiver = getUname(message);
    uname_send = getSender(message);
    String file_name = getFileName(message);
    //询问接收方 是否接收
    accept_or_not(uname_receiver,file_name);
}
```

```java
public void accept_or_not(String uname,String fileName){
    String message = "{ID:2," + "Uname" + uname_send + ",FileName:" + fileName;
    tellSomeone(message,uname);
}
```

```java
else if(message_id == 5){
    
    if(message.substring(message.length() - 2).equals("NO")){
        //对方拒接 要求停止发送
        System.out.println("对方拒绝接收文件，请停止发送");
        String message_n = "{ID:4,NONONO";
        tellSomeone(message_n,uname_send);
    }
    else{
        //对方同意接收
        System.out.println("开始传输文件");
        String message_y = "{ID:4}";
        String uname = getUname(message);
        tellSomeone(message_y,uname_send);
    }

}
```

```java
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
```

#### 客户端代码

* 接收端

```java
else if(ID == 2){
    //询问接收文件
    String uname_sender = getUname(message);
    fileName_rev = getFileName(message);
    //弹出确认窗口
    int rst = JOptionPane.showConfirmDialog(null, uname_sender + "想要给你传输文件" + fileName_rev, "是否接收", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
    if(rst == JOptionPane.OK_OPTION){
        //点击确定 ---- 正常接收文件
        String message_yes = "{ID:5,Uname:" + my_uname;
        System.out.println("yes");
        writer.println(message_yes);
        writer.flush();
        getfile();
    }
    else{
        //点击取消 ---- 拒绝接收文件
        String message_no = "{ID:5,NONONO";
        System.out.println("no");
        writer.println(message_no);
        writer.flush();
    }
}
```

```java
public void getfile(){
    try{
        //直接存到D盘根目录
        FileOutputStream fos = new FileOutputStream("D://"+ fileName_rev);
        byte[] buffer = new byte[10];
        int len;
        while ((len = is.read(buffer)) == 10)
        {
            fos.write(buffer);
            fos.flush();
        }
        fos.write(buffer);
        fos.flush();
        fos.close();
        System.out.println("文件接收完成");
    }catch (Exception ex){
        ex.printStackTrace();
    }
}
```

* 发送端

```java
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
```

```java
void send_file(){
    System.out.println(file.getPath());
    //发送文件
    try{
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] b = new byte[10];
        int i = -1;
        while((i = bis.read(b)) == 10){
            System.out.println("len = ");
            System.out.println(i);
            os.write(b,0,i);
            os.flush();
        }
        os.write(b);
        os.flush();
        writer.write("   ");
        writer.flush();
        System.out.println("文件发送完毕");
        //关闭流
        bis.close();
    }catch (Exception ex){
        ex.printStackTrace();
    }
}
```

这里接收输入文件都是使用的java标准输入输出流，使用字节流进行传输的，同时在这里规定的每个传输单位为10个字节，可以有效防止传输过大导致失败。

### 4.4 好友列表动态更新

本次实验除了进行规定的几个要求之外，设置了好友列表动态更新的功能，每上线一个好友，好友列表都会更新一下，这样就实现了进行私聊文件传输的功能。

| 服务器发送的ID | 类型         |
| -------------- | ------------ |
| 1              | 更新好友列表 |

每次有新的用户登录，服务器都会发送一个ID为1的包，用于更新好友列表，客户端处理这个消息，动态更新自己的列表并显示出来。

#### 抓包分析

![image-20220517154223904](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517154223904.png)

![image-20220517154325298](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220517154325298.png)

#### 服务器代码

```java
//登录成功 向套接字发送 ID:0 Instr:logsuccess Uname: uname 告诉这个用户注册成功了
public void loginsuccess(String Uname){
    user_online.add(Uname);
    String message = "{ID:0,Instr:logsuccess,Uname:" + Uname + "}";
    tellEveryone(message);
    int num = clientOutputStreams.size();
    //在这里获取了存储的好友列表并发送给所有客户端
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
```

#### 客户端代码

```java
else if(ID == 1){
    //更新好友列表
    update_onlinelist(message);
    System.out.println(online_users);
    top.removeAllChildren();
    for(int i = 0;i < online_num;i++){
        DefaultMutableTreeNode node;
        if(online_users.get(i).equals(my_uname)){
            //这是用户自己
            node = new DefaultMutableTreeNode(online_users.get(i) + "(me)");
            if(node.isNodeChild(top))continue;
            model.insertNodeInto(node,top,0);
        }
        else{
            //这是其他用户
            node = new DefaultMutableTreeNode(online_users.get(i));
            if(node.isNodeChild(top))continue;
            model.insertNodeInto(node,top,0);
        }
    }
}
```

好友列表这里是使用的java swing组件中的一个动态树JTree结构，可以实现动态更新的效果。

## 5 实验总结

### 5.1 学到的知识

1. 从头学习的Java语言，Java语言把面向对象的特征发挥的淋漓尽致，每个class间都可以相互调用，每个函数均定义在类之中，本次实验我在服务器端仅写了一个类，而在客户端一共写了3个类，分别是登录、主界面、聊天程序，三个类的相互联系非常复杂，在摸索中才能勉强运行起来。
2. Java对于Socket的处理比较好，由于是使用标准输入输出流进行输入输出的，因此有许多封装好的方法或类，例如：
   * `Socket.getInputStream()`可以获得标准的输入流，`InputStream`超类，我们一般可以使用这个类的子类。
   * `InputStreamReader`是字节流转化为字符流的桥梁，也就是将字节流转化为字符流进行处理。
   * `BufferedReader`以缓冲的方式读取文本，提供了许多实用的方法。
   * `PrintWriter`将标准输出流`OutputStream`（字节流）转化为字符流进行输出，也封装了许多实用的方法。
3. Java图形化设计也比较有挑战性，有许多实验中的难点：
   * 使用`JPopupMenu`实现好友按钮双击出现菜单。
   * 使用`JFileChooser`实现选择文件的框。
   * 使用`idea`的`Swing UI Designer`自动生成`UI`的代码大大提高工作效率。
   * 使用`JTree`结构实现一个动态的好友列表，并且每个好友可以作为一个组件进行双击。

### 5.2 实验中的不足

实验中的不足之处非常多：

1. 刚开始自己写代码并没有考虑到自顶向下设计，没有从UI先写出框架，于是刚开始写底层的代码很多都是无用功，实际在UI中根本用不上，所以说一定要先写UI，或者先形成一个大体框架，然后再慢慢向框架中补充内容，形成整个项目。
2. 对于文件传输，我在顶层进行设计时就已经设计的是服务器对于每个客户端仅仅开一个线程进行工作，于是在我实际补充文件传输的代码的时候，发现如果文件传输可以新建一个线程的话会好写很多，但是当时的代码已经不允许这样大幅度的修改了，所以说进行顶层设计的时候要考虑到这些小问题，最终我实现了单个线程的文件传输，这样的话在进行大文件的传输的时候是不允许消息的发送的。
3. 写了语音聊天的框架，但是没有时间去加上这些内容。
