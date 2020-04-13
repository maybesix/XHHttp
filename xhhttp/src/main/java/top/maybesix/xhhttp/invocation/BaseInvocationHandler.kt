package top.maybesix.xhhttp.invocation

import android.os.Handler
import android.os.Looper
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.exception.HttpException
import top.maybesix.xhhttp.request.GET
import top.maybesix.xhhttp.request.POST
import top.maybesix.xhhttp.request.Path
import top.maybesix.xhhttp.util.XHHttpUtils
import top.maybesix.xhhttp.util.XHHttpUtils.logD
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.net.UnknownHostException
import kotlin.concurrent.thread
import kotlin.reflect.jvm.kotlinFunction


/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc 具体的代理实现.
 */
class BaseInvocationHandler : InvocationHandler {
    val handler = Handler(Looper.getMainLooper())

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        //ps:这里为了方便就直接new Thread了,如果是使用的话可以使用线程池或者kt协程,消耗会低很多,一般项目中是不允许直接new Thread的
        thread {
            if (XHHttp.baseUrl.isEmpty()) {
                throw HttpException("baseUrl不能为空")
            }
            if (
                !(XHHttp.baseUrl.startsWith("http://") || XHHttp.baseUrl.startsWith("https://"))
                || !XHHttpUtils.checkUrl(XHHttp.baseUrl)
            ) {
                throw HttpException("baseUrl不合法")
            }

            //获取方法的注解,先获取get注解,如果为空就获取post注解;
            // ps:自己用的时候可以先获取常用的注解,这样就不用判断两次了,比如项目里大部分都是post请求,那就先获取POST
            val annotation =
                method?.getAnnotation(GET::class.java)
                    ?: method?.getAnnotation(POST::class.java)
            //这个地方区分函数上的注解
            when (annotation) {
                is GET -> startGet(proxy, method, args, annotation)
                is POST -> startPost(proxy, method, args, annotation)
                else -> throw RuntimeException("亲,${method?.name}方法是不是忘加注解了?")
            }

        }
        return Unit
    }

    //post请求
    private fun startPost(proxy: Any?, method: Method?, args: Array<out Any>?, post: POST) {
        //post就不写了,大家可以在这里二次封装网络请求,比如使用okhttp,或者使用Socket,甚至可以用别人二次或者三次封装好的网络请求
    }

    //get请求
    private fun startGet(proxy: Any?, method: Method?, args: Array<out Any>?, get: GET) {
        //获取url并拼接
        val callbackName = get.callbackName
        var callback: ObserverCallBack? = null
        //是否追加了'?'
        var isAddQuestionMark = false
        var urlPath = get.url

        //此处用反射遍历参数
        method?.kotlinFunction?.parameters?.forEachIndexed { index, kParameter ->
            when (kParameter.name) {
                null -> {
                    //接口本身的对象,我们不需要
                }
                callbackName -> {//回调对象,ps:index-1是因为parameters的第0位置是代理类对象
                    callback = args?.get(index - 1) as? ObserverCallBack
                }
                else -> {
                    //如果被标注了，则替换路径响应字符串，否则在路径后边拼接
                    if (kParameter.annotations.isNotEmpty()) {
                        kParameter.annotations.forEach {
                            if (it is Path) {
                                val oldStr = it.value
                                val newStr = args?.get(index - 1).toString()
                                urlPath = urlPath.replace("{$oldStr}", newStr)
                            }
                        }
                    } else {
                        //进行拼接url
                        if (!isAddQuestionMark) {
                            urlPath = "$urlPath?"
                            isAddQuestionMark = true
                        }
                        //依次将参数添加到url上
                        urlPath = "$urlPath${kParameter.name}=${args?.get(index - 1)}&"
                    }

                }
            }
        }
        //清除最后一个&
        if (urlPath.last() == '&') {
            urlPath = urlPath.substring(0, urlPath.length - 1);
        }
        //添加处理过的url的路径
        val url = XHHttp.baseUrl + urlPath

        //默认去除空格
        url.trim()
        "Get请求的地址：${url}".logD()
        //url不能有中文
        handler.post {
            callback?.onStart()
        }
        val client = OkHttpClient()
        val request: Request = Request.Builder().url(url).get()
            .build()
        val call: Call = client.newCall(request)
        try {
            val response: Response = call.execute()
            val data = response.body()?.string() ?: ""
            "请求的结果：$data".logD()
            //在主线程回调
            callback?.onHandle(data, 0, 0)
            callback?.onComplete()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        try {
//            //此处请求网络
//            val data = URL(url.toString()).readText()
//            data.logD()
//            handler.post {
//                //在主线程回调
//                callback?.onHandle(data, 0, 0)
//                callback?.onComplete()
//            }
//        } catch (e: UnknownHostException) {
//            e.printStackTrace()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


    }

}