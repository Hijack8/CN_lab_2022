# 源代码说明

* tcp_server.java

这是服务器唯一的java文件。

* WeChat.java

是顶层的客户端java文件。

* login.java

其中主要定义了有关登录界面的东西。

* GUI_new.java

其中主要是聊天框的GUI设计，以及主要的聊天、文件传输的逻辑。

## 运行方法

### 客户端(Windows)

```shell
javac -encoding utf-8 WeChat.java
java WeChat.java
```

### 服务端(Centos)

````shell
javac tcp_server.java
java tcp_server
````

