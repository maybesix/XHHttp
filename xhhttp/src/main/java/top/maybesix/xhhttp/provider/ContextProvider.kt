package top.maybesix.xhhttp.provider

import android.app.Application
import android.content.Context
import top.maybesix.xhhttp.XHHttp

/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc 提供 全局 applicationContext.
 */

object ContextProvider {

    lateinit var application: Application

    fun attachContext(context: Context?) {
        application = context as? Application ?: throw RuntimeException("init XHHttp error !")
        //init
        XHHttp.init(application)
    }
}