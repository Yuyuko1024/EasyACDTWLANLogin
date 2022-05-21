## **EasyACDTWLANLogin**

#### 这是什么？

这是一个适用于[哆点校园网络验证](https://www.drcom.com.cn/)的校园网登录前端，本体采用Java语言编写，使用了[okhttp](https://github.com/square/okhttp)作为post部分核心。



#### 它可以在哪些网络环境使用？

只可以在使用了[哆点校园网络验证](https://www.drcom.com.cn/)的WLAN校园网环境使用。



#### 它可以在哪些高校使用？

本人目前就读于[安徽国防科技职业学院 ](https://www.acdt.edu.cn/)，此App也是在该校的校园网认证环境下适配编写。对于其他使用了哆点校园网络的高校，您可以自行使用抓包工具抓取您本校的Web登录界面的POST请求URL并替换`app/src/main/res/values/strings.xml`中[L3](https://github.com/Yuyuko1024/EasyACDTWLANLogin/blob/main/app/src/main/res/values/strings.xml#L3)行的`postDomain`字符串。

`<string name="postDomain" translatable="false">http://<xliff:g id="authIP">%s</xliff:g>:<xliff:g id="authPort">%s</xliff:g>/eportal/?c=ACSetting&amp;a=Login&amp;protocol=http:&amp;hostname=<xliff:g id="authIP">%s</xliff:g>&amp;iTermType=1&amp;wlanuserip=<xliff:g id="myIpAddress">%s</xliff:g>&amp;wlanacip=null&amp;wlanacname=null&amp;mac=00-00-00-00-00-00&amp;ip=<xliff:g id="myIpAddress">%s</xliff:g>&amp;enAdvert=0&amp;queryACIP=0&amp;loginMethod=1</string>`

注意！其中的`authIP`为您所在高校使用的认证服务器IP地址，`authPort`为接收POST请求的端口号，`myIpAddress`为您设备当前的WLAN AP中DHCP获取到的IP地址。在替换该URL时也请务必替换掉这些！

若端口号与您高校的认证服务器的不一致，请打开`app/src/main/java/org/yuyu/easylogin/login/LoginCore.java`修改其中的[L37](https://github.com/Yuyuko1024/EasyACDTWLANLogin/blob/main/app/src/main/java/org/yuyu/easylogin/login/LoginCore.java#L37)中的String构造方法内的端口号。



#### 我发现了Bug！

欢迎提交 [Issues](https://github.com/Yuyuko1024/EasyACDTWLANLogin/issues) 和 [Pull requests](https://github.com/Yuyuko1024/EasyACDTWLANLogin/pulls)！



#### 感谢以下开源项目

[Toasty](https://github.com/GrenderG/Toasty)

[RetroMusic - appthemehelper](https://github.com/RetroMusicPlayer/RetroMusicPlayer)

[okhttp](https://github.com/square/okhttp)

[LicensesDialog](https://psdev.de/LicensesDialog)

[drcom-generic](https://github.com/drcoms/drcom-generic)
