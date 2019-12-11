
# Netty 服务端的说明

1.Netty服务端结合Web应用使用.因此,Web应用本身拥有一个端口.

此端口由 application.properties中进行配置, server.port=9000

Netty服务监听ws(Websocket)端口. 是另外一个新端口.

netty服务器监听通信端口,由netty启动时绑定的端口. bootstrap.bind(9001)

因此,前台访问后台web服务时候,是由9000端口提供服务.登录,注册等...需要和数据库打交道的数据.

前台间进行聊天通信时,聊天数据的交互,是由netty完成,端口为 9001.


注意区分:此处极易混淆．

