package top.maybesix.xhhttpdemo.test

import java.lang.reflect.Proxy

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        //正常的模式
        val kun = Start("cxk")
        kun.sing()
        kun.dance()
        kun.rap()
        kun.playBasketball()

        println()
        println("有了经纪人以后")
        println()

        //现在需要有经济人处理一些事宜
        val agent = StartProxy(kun)
        agent.sing()
        agent.dance()
        agent.rap()
        agent.playBasketball()

        println()
        println("使用动态经纪人以后")
        println()
       val agentDynamic =  Proxy.newProxyInstance(//调用动态代理生成的方法来生成动态代理
           kun.javaClass.classLoader,//类加载器对象
           kun.javaClass.interfaces,//因为我们的接口不需要继承别的接口,所以直接传入接口的class就行
           StartInvocationHandler(kun))as StarInterface
        agentDynamic.sing()
        agentDynamic.dance()
        agentDynamic.rap()
        agentDynamic.playBasketball()
    }
}