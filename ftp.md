ftp



telnet ncat 都可

分析ftp工作过程

请求应答  

两种传输模式 **NAT**

两层NAT可以工作 需要设置好云服务器端才能工作





![image-20220320141022752](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220320141022752.png)



服务器上

```
netstat -net
```



![image-20220320141112924](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220320141112924.png)









http

工作过程

与TCP关系

**资源缓存机制** 

请求应答机制

对多个资源对象的传输



HTTP与FTP二选一

FTP SMTP HTTP 一脉相承

## http分析

1 清空缓存

![image-20220320142208443](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220320142208443.png)

![image-20220320142332974](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220320142332974.png)

```
ncat -C [IP] 80
```



![image-20220320142710783](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220320142710783.png)







## 实验八

Socket网络编程

![image-20220320142851709](C:/Users/dell/AppData/Roaming/Typora/typora-user-images/image-20220320142851709.png)

![image-20220320143035283](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143035283.png)

![image-20220320143217420](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143217420.png)

![image-20220320143333259](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143333259.png)









![image-20220320143430016](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143430016.png)

![image-20220320143528144](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143528144.png)





![image-20220320143538293](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143538293.png)







![image-20220320143620230](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143620230.png)



主动连接 被动连接是相对于服务器的

![image-20220320143719730](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143719730.png)

![image-20220320143753016](https://hijack.oss-cn-chengdu.aliyuncs.com/typoraimg/image-20220320143753016.png)





