package top.maybesix.xhhttpdemo

import android.os.Handler
import android.os.Looper
import top.maybesix.xhhttpdemo.CommonExt.print
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.net.URL
import kotlin.concurrent.thread
import kotlin.reflect.jvm.kotlinFunction

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */
class HttpFunctionsHandler : InvocationHandler {
    //    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
//        method?.name.print()//打印方法名
//        args?.forEach { it.print() }//打印参数值
//        return Unit
//    }
//    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
//        method?.name.print()//打印方法名
//        args?.forEach { it.print() }//打印参数值
//        method?.parameters?.forEach { it.name.print() }//打印参数名
//        return Unit
//    }
//    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
////        method?.name.print()//打印方法名
////        args?.forEach { it.print() }//打印参数值
//        method?.kotlinFunction?.parameters?.forEach {
//            "${it.type} - ${it.name}".print()//打印参数类型和参数名
//        }
//        return Unit
//    }
    val handler = Handler(Looper.getMainLooper())

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        thread {
            val kotlinFunction = method?.kotlinFunction//获取到KFunction对象
            val url = StringBuilder(HttpConfig.ROOT_URL)
                .append("article/list/0/json?")
            var callback: ObserverCallBack? = null
            kotlinFunction?.parameters?.forEachIndexed { index, kParameter ->
                when (kParameter.name) {
                    null -> {//HttpFunctions对象,我们不需要
                    }
                    "_callback" -> {//回调对象,ps:index-1是因为parameters的第0位置是代理类对象
                        callback = args?.get(index - 1) as? ObserverCallBack
                    }
                    else -> {//其他的就是参数了
                        //进行拼接url
                        url.append(kParameter.name)
                            .append('=')
                            .append(args?.get(index - 1))
                            .append('&')
                    }
                }
            }
            if (url.endsWith('&'))
                url.deleteCharAt(url.length - 1)//清除最后一个&
            url.print()
            val data = URL(url.toString()).readText()//请求网络
            handler.post {
                callback?.handleResult(data, 0, 0)//在主线程回调
            }
        }
        return Unit
    }
}