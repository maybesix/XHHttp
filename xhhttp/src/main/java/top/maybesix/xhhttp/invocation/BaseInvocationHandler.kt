package top.maybesix.xhhttp.invocation

import android.os.Handler
import android.os.Looper
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.exception.HttpException
import top.maybesix.xhhttp.request.GET
import top.maybesix.xhhttp.request.POST
import top.maybesix.xhhttp.util.XHHttpUtils
import top.maybesix.xhhttp.util.XHHttpUtils.logD
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.net.URL
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

            //获取方法的注解,先获取get注解,如果为空就获取post注解; ps:自己用的时候可以先获取常用的注解,这样就不用判断两次了,比如项目里大部分都是post请求,那就先获取POST
            val annotation =
                method?.getAnnotation(GET::class.java)
                    ?: method?.getAnnotation(POST::class.java)
            //代码不要都堆到一块,而是应该拆成方法或者类,这样调用的时候只调用一个方法,逻辑会清晰很多; ps:这里其实也可以先判断常用的,因为kt的when函数的字节码其实也是if else
            when (annotation) {
                is GET -> startGet(proxy, method, args, annotation)
                is POST -> startPost(proxy, method, args, annotation)
                else -> throw RuntimeException("亲,${method?.name}方法是不是忘加注解了?")//如果出现异常情况,最好不要藏着,及时告诉开发人员,不然出了问题也不知道是怎么回事,得找好长时间
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
        val url = StringBuilder(XHHttp.baseUrl).append(get.url)
        val callbackName = get.callbackName
        var callback: ObserverCallBack? = null
        //是否追加了'?'
        var isAddQuestionMark = false
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
                    //进行拼接url
                    if (!isAddQuestionMark) {
                        url.append('?')
                        isAddQuestionMark = true
                    }
                    //依次将参数添加到url上
                    url.append(kParameter.name)
                        .append('=')
                        .append(args?.get(index - 1))
                        .append('&')
                }
            }
        }
        //清除最后一个&
        if (url.endsWith('&')) {
            url.deleteCharAt(url.length - 1)
        }
        //默认去除空格
        url.trim()
        url.logD()
        //url不能有中文
        handler.post {
            callback?.onStart()
        }
        try {
            //此处请求网络
            val data = URL(url.toString()).readText()
            data.logD()
            handler.post {
                //在主线程回调
                callback?.onHandle(data, 0, 0)
                callback?.onComplete()
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}