package top.maybesix.xhhttp.util

import android.util.Log
import android.util.Patterns
import top.maybesix.xhhttp.XHHttp

/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc TODO.
 */
object XHHttpUtils {
    fun Any?.logD() =
        if (XHHttp.isDebug)
            Log.d(XHHttp.TAG, this.toString())
        else
            -1
    fun Any?.log() =
            Log.i(XHHttp.TAG, this.toString())


    /**
     * 检查url是否合法
     * @param url String url
     * @return Boolean 结果
     */
    fun checkUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }
}