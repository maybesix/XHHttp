package top.maybesix.xhhttp.util

import android.util.Log
import top.maybesix.xhhttp.XHHttp

/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc TODO.
 */
object CommUtils {
    fun Any?.log() = Log.d(XHHttp.TAG, this.toString())
}