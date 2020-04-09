package top.maybesix.xhhttpdemo.test

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */
class StartInvocationHandler(private val start:StarInterface) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method, args: Array<Any?>?): Any {
        if (method.name=="sing"){
            println("唱歌需要找场地")
            method.invoke(start)
            println("唱歌后的善后")
        }else{
            println("其他活动")
            method.invoke(start)
            println("其他活动的善后")
        }
        return Unit
    }

}