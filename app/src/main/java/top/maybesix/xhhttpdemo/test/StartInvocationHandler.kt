package top.maybesix.xhhttpdemo.test

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */
class StartInvocationHandler() : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method, args: Array<Any?>?): Any {
        if (method.name==""){

        }
        return Unit
    }

}