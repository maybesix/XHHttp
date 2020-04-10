package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.request.GET


/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc TODO.
 */
interface HttpRequest {
    companion object {
        val instance = XHHttp.getInstance(HttpRequest::class.java)
    }

    @GET("article/list/0/json?")
    fun getJson(_callback: ObserverCallBack?,
                cid: String)

}