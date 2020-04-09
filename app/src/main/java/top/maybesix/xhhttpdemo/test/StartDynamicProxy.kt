package top.maybesix.xhhttpdemo.test

import java.lang.reflect.Proxy

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */

class StartDynamicProxy {
    companion object{
        val clazz = StartDynamicProxy::class.java//拿到我们被代理接口的class对象
        val instance = Proxy.newProxyInstance(//调用动态代理生成的方法来生成动态代理
            clazz.classLoader,//类加载器对象
            arrayOf(clazz),//因为我们的接口不需要继承别的接口,所以直接传入接口的class就行
            StartInvocationHandler())
    }
}