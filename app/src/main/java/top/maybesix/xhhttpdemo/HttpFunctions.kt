package top.maybesix.xhhttpdemo

import java.lang.reflect.Proxy

/**
 * @author MaybeSix
 * @date 2020/4/9
 * @desc TODO.
 */
interface HttpFunctions {
    companion object {
        /**

         * 动态代理单例对象

         */
        val instance: HttpFunctions = getHttpFunctions()

        //获取动态代理实例对象

        private fun getHttpFunctions(): HttpFunctions {
            val clazz = HttpFunctions::class.java//拿到我们被代理接口的class对象
            return Proxy.newProxyInstance(//调用动态代理生成的方法来生成动态代理
                clazz.classLoader,//类加载器对象
                arrayOf(clazz),//因为我们的接口不需要继承别的接口,所以直接传入接口的class就行
                HttpFunctionsHandler()//InvocationHandler接口的实现类,用来处理代理对象的方法调用
            ) as HttpFunctions
        }

    }

    /**

     * 获取玩安卓的json数据

     * @param cid 这个接口的参数(虽然不知道有什么用emmm)

     */

    fun getJson(_callback: ObserverCallBack?,
                cid: String
    )

}