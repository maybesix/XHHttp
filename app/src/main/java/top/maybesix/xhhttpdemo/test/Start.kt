package top.maybesix.xhhttpdemo.test

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */

class Start(private var name: String) :StarInterface{

    override fun sing() {
        println(name+"进行了唱")
    }

    override fun dance() {
        println(name+"进行了跳")
    }

    override fun rap() {
        println(name+"进行了rap")
    }

    override fun playBasketball() {
        println(name+"进行了打篮球")
    }

}