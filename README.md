
# NettyChat

Netty仿微信的聊天的项目.

本项目划分为2个子项目,一个是NettyChat的服务端模块. 主要使用SpringBoot + Netty实现多客户端链接和聊天.使用Netty,可以实现基于实时的通信功能.

一个是NettyChat的前端模块,主要是使用mui完成移动端的开发.

## 前端使用技术:

JQuery + mui + h5.plus

使用HbuilderX 进行开发.

## 后端技术选型:

SpringBoot + Netty 

使用Idea进行开发. 


### 注意: 本项目需要移动端设备支持,如果没有移动端设备.可以采取模拟器来实现.建议使用夜游神模拟器 nox.


移动端代码见:  [u-chat](https://github.com/cynen/NettyChat/tree/master/u-chat)

Netty后端服务见:  [u-chat-server](https://github.com/cynen/NettyChat/tree/master/u-chat-server)


u-chat说明: 一个精简版的,拥有相对完整功能的基于mui的前端代码.

u-chat是基于mui框架快速搭建的一个客户端代码.

项目基本框架: ![架构图](https://cynen.oss-cn-shenzhen.aliyuncs.com/github/nettychat/archive.png)


# 效果展示

Notice:为实现2个客户端的通信,我们使用了2个模拟器.

主页面:

![主页面](https://cynen.oss-cn-shenzhen.aliyuncs.com/github/nettychat/me2.png)

联系人界面:

![联系人](https://cynen.oss-cn-shenzhen.aliyuncs.com/github/nettychat/contact.png)

发现页面:

![发现](https://cynen.oss-cn-shenzhen.aliyuncs.com/github/nettychat/discover.png)

聊天界面:

![](https://cynen.oss-cn-shenzhen.aliyuncs.com/github/nettychat/main.png)


2个客户端通信示意:

![](https://cynen.oss-cn-shenzhen.aliyuncs.com/github/nettychat/communication.png)