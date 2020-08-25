# MyKotlinDemo
练习项目

gradle 集成方式

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  dependencies {
	        implementation 'com.github.joinYejianping:MyKotlinDemo:Tag'
	}
