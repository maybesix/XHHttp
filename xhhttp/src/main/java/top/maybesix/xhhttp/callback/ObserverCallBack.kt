package top.maybesix.xhhttp.callback
/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc 请求的回调.
 */
interface ObserverCallBack {
    fun onStart()
    fun onHandle(data: String?, encoding: Int, method: Int)
    fun onComplete()
}