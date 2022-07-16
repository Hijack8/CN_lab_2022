## JAVA网络编程学习

网络编程是指编写运行在多个设备的程序，这些设备都通过网络连接起来。

java.net包中J2SE的API包含有类和接口，他们提供低层次的通信细节，可以直接使用这些类和接口，来轻松实现网络编程。

### Socket编程

两台计算机之间使用套接字建立TCP连接时会出现：

* 服务器实例化一个ServerSocket对象，表示通过服务器上的端口通信。
* 服务器调用ServerSocket类的accept()方法，该方法将一直等待，直到客户端连接到服务器上给定的端口。
* 服务器正等待的时候，一个客户端实例化一个Socket对象，指定服务器名称和端口号来请求连接。
* Socket类的构造函数试图将客户端连接到指定的服务器和端口号，如果通信被建立，则在客户端创建一个Socket对象能够与服务器进行通信。
* 在服务器，accept()方法返回服务器上一个新的socket引用，该socket连接到客户端的socket。

连接建立后，通过使用 I/O 流在进行通信，每一个socket都有一个输出流和一个输入流，客户端的输出流连接到服务器端的输入流，而客户端的输入流连接到服务器端的输出流。

TCP 是一个双向的通信协议，因此数据可以通过两个数据流在同一时间发送.以下是一些类提供的一套完整的有用的方法来实现 socket。

**聊天程序工作方式**

1. 客户端连接到服务器
2. 服务器建立链接并把客户端加到来宾清单中
3. 另一个用户链接上来
4. 用户A送出信息到聊天服务器上
5. 服务器将信息送给所有的来宾

#### 连接、传送与接收

连接

通过建立socket连接来连接服务器，只需要知道IP地址和端口号即可。

```java
Socket chatSocket = new Socket("192.168.1.1",5000);
```

传送

用户送出信息给服务器

接收

用户从服务器接收信息

#### 使用BufferedReader从Socket上读取数据

```java
Socket chatSocket = new Socket("127.0.0.1",5000);

//建立了解到Socket上低层输入串流的InputStreamReader
InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream());

//建立Buffer来读取
BufferedReader reader = new BufferedReader(stream);
String message = reader.readLine();
```

#### 用PrintWriter写数据到Socket上

```java
Socket chatSocket = new Socket("127.0.0.1",5000);
PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
writer.println("message to send");
writer.print("another message");

```



