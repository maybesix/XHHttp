# XHHttp
[![](https://jitpack.io/v/maybesix/XHHttp.svg)](https://jitpack.io/#maybesix/XHHttp)

#### 介绍
一个简单好用http请求框架

#### 软件架构
采用接口+注解的方式定义请求
原理：反射+动态代理
http实现：OkHttp3

#### 使用说明
```
添加
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
	dependencies {
            //需要添加反射库
            implementation 'org.jetbrains.kotlin:kotlin-reflect:1.3.71'

	        implementation 'com.github.maybesix:XHHttp:Tag'
	}

```
1.定义一个接口
```kotlin
interface HttpRequest {
    companion object {
        fun instance(config: XHHttpConfig? = null): HttpRequest {
            return XHHttp.getInstance(HttpRequest::class.java, config)
        }
    }  
    @GET("wxarticle/chapters/json")
    fun getChapters(_callback: ObserverCallBack?)

    @POST("article/query/0/json")
    fun search(_callback: ObserverCallBack?, k: String = "kotlin")

}
```
2.发起请求
```kotlin
HttpRequest.instance().getChapters(
callbackOf<String> {
    success {
    }
},"0")
```

3.注解

|注解|标注位置|描述|
|---|---|---|
|@GET|接口方法|表明该方法是get请求|
|@POST|接口方法|表明该方法是post请求|

复杂参数的注解有
|注解|标注位置|描述|
|---|---|---|
|@Path                      | 接口的参数   |用于动态替换接口方法上注解的url中的字段|
|@Param                  |接口的参数    |用于标注改参数为类|
|@ParamRename   |参数的类内部|用于标注参数类的某个属性的在构建url的时候的真实字段|
|@ParamIgnore      |参数的类内部|用于忽略某个字段|

文档详情
>https://www.jianshu.com/p/024994e8dcf2
>https://juejin.im/post/5eae55afe51d454dd940636a