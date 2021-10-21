# MinecraftPing插件

***一个基于Mirai 2.7.0的机器人插件，用以检测java版minecraft服务器在线情况，检测java版minecraft更新情况***

## 使用说明
* **插件项目地址 [github项目地址](https://github.com/Ycituss/MinecraftPing) [gitee项目地址](https://gitee.com/ycycycc123/MinecraftPing)**
* **插件基于[Mirai Clonsole](https://github.com/mamoe/mirai-clonsole) 2.7.0版本开发，请使用Mirai-Clonsole 2.7.0及以上版本使用本插件**
* **插件中服务器信息获取使用的是[MX233](https://github.com/MX233) 大佬的代码[MinecraftServerPing](https://github.com/MX233/MinecraftServerPing)**
* **使用时将`xxx.mirai.jar`文件放入plugins文件夹，重启Mirai**
* **配置文件位于`config\MinecraftPing\ `文件夹中**
* **第一次用在配置文件里手动更改botAdministrators项，改为自己的QQ号**
* **请选择适合自己系统的`phantomjs`放入 `data\MinecraftPing\ ` 文件夹中(下载地址为 [Download PhantomJS](https://phantomjs.org/download.html), Windows系统文件名为`phantomjs.exe`, linux系统文件名为`phantomjs`)**

## 指令说明

- ### **管理员指令**

>指令|说明
>---|---
> mcpingReload|重新加载配置文件|
> serverset `服务器地址`|设置当前群绑定服务器|
> setShowFavicon `true`/`false`|是否开启服务器图标展示|
> setCheckVersion `true`/`false`|是否开启minecraft新版本检测|

- ### **普通成员指令**

>指令|说明
>---|---
> mcping|当前群绑定服务器状态
> mcping `服务器地址`|指定服务器状态
> mcversion|当前最新java版minecraft版本
> mcversionjpg|当前最新java版minecraft版本更新日志

- ### **未开放指令**

*要使用此功能，请自行下载源码开启(~~或者找我帮忙~~)*

>指令|说明
>---|---
> mcplayer|当日服务器玩家在线状态
> setCheckMcping `true`/`false`|是否开启服务器状态监测