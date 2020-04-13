package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.request.GET
import top.maybesix.xhhttp.request.Path


/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc TODO.
 */
interface HttpRequest {
    companion object {
        val instance = XHHttp.getInstance(HttpRequest::class.java)
    }

    @GET("/article/list/{path}/json")
    fun getJson(_callback: ObserverCallBack?, @Path("path") page: String, name: String)

}