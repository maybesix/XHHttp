package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.annotation.GET
import top.maybesix.xhhttp.annotation.POST
import top.maybesix.xhhttp.annotation.Param
import top.maybesix.xhhttp.annotation.Path
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.config.XHHttpConfig
import top.maybesix.xhhttpdemo.param.Author


/**
 * @author MaybeSix
 * @date 2020/5/3
 * @desc 写一个接口，书写伴生类方法.
 */

interface HttpRequest {
    companion object {
        fun instance(config: XHHttpConfig? = null): HttpRequest {
            return XHHttp.getInstance(HttpRequest::class.java, config)
        }
    }

    @GET("wxarticle/chapters/json")
    fun getChapters(_callback: ObserverCallBack)

    @GET("article/list/{path}/json")
    fun getArticleJson(_callback: ObserverCallBack, @Path("path") page: String)

    @POST("article/query/0/json")
    fun search(_callback: ObserverCallBack, k: String = "kotlin")

    @GET("article/list/0/json")
    fun getArticleByAuthorName(callBack: ObserverCallBack, @Param authorEntity: Author)

}