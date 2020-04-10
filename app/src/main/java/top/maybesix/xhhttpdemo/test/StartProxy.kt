package top.maybesix.xhhttpdemo.test

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */
class StartProxy(private var start: StarInterface) :StarInterface {

    override fun sing() {
        println("经纪人提前准备唱歌场地")
        start.sing()
        println("经纪人进行唱歌后的善后工作")
    }

    override fun dance() {
        println("经纪人提前准备跳舞场地")
        start.dance()
        println("经纪人进行跳舞后的善后工作")
    }

    override fun rap() {
        println("经纪人提前准备rap场地")
        start.rap()
        println("经纪人进行rap后的善后工作")
    }

    override fun playBasketball() {
        println("经纪人提前准备篮球场")
        start.playBasketball()
        println("经纪人进行打篮球后的善后工作")
    }
}