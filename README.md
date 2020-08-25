# MyKotlinDemo
练习项目

Gradle 集成方式

allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
  
dependencies {
	implementation 'com.github.joinYejianping:MyKotlinDemo:Tag'
}


项目结构：
app DEMO
easytools 工具包
    src
        main
            java
                com.yjp.easytools
                    adapter     通用适配器
                    base        父类封装
                    bus         RxBus
                    databing    databing View事件封装
                    db          数据库操作
                    dialog      通用弹窗
                    generator   项目结构生成工具
                    http        网络请求封装
                    picture     第三方图片框架Picture使用封装
                    utils       工具包
                    view        自定义View
            res
                drawable                自定义图
                layout                  布局
                values                  通用资源
                xml                     文件权限
                AndroidManifest.xml     配置权限

引用第三方包：
config.gradle   统一版本管理