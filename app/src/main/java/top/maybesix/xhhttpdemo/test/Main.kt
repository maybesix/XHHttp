package top.maybesix.xhhttpdemo.test

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


        StartDynamicProxy.instance
    }
}