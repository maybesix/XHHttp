package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.annotation.GET
import top.maybesix.xhhttp.annotation.Params
import top.maybesix.xhhttp.annotation.Path
import top.maybesix.xhhttp.callback.ObserverCallBack


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

    @GET("/dept/{deptId}/json")
    fun getPerson(
        _callback: ObserverCallBack?,
        @Path("deptId") deptId: String,
        groupId: String,
        @Params() person: Person
    )

}