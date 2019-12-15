
# 手机链接模拟器联调

建议使用nox模拟器.下载地址: [夜神模拟器](https://www.yeshen.com/)

刚刚测试了一下,mumu模拟器好像无法实现多开.[程序多开,不是内容多开]

打开nox模拟器的安装路径.

找到bin文件夹.

我这里是 `D:\Program Files\Nox\bin`

打开命令行. [Ctrl键不放,右键点击空白.]

执行命令:

`adb devices`

```
输出: list of devices的信息. 
可以查看对应模拟器的端口.
```

HbuilderX 进行配置对应的端口即可.
运行 -- 运行到手机或模拟器 -- Android模拟器端口设置. 填写对应的端口即可.


参考: [HBuilder X 中使用模拟器进行App开发](https://www.cnblogs.com/oukele/p/9967291.html)





