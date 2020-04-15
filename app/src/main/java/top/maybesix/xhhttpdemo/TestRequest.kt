package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.annotation.GET
import top.maybesix.xhhttp.annotation.Path
import top.maybesix.xhhttp.callback.ObserverCallBack


/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc TODO.
 */

interface TestRequest {

    @GET("/article/list/{path}/json")
    fun getJson(_callback: ObserverCallBack?, @Path("path") page: String, name: String)



}