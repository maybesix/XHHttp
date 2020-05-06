package top.maybesix.xhhttp.dsl

import android.os.Looper
import com.alibaba.fastjson.JSON
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.model.ApiResult
import top.maybesix.xhhttp.util.XHHttpUtils
import top.maybesix.xhhttp.util.XHHttpUtils.logE


/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc 请求回调的dsl的模板.
 */
/**
 * 使用dsl的callback
 * ps: CallBackDsl.()这种语法相当于CallBackDsl的一个扩展函数,
 * 把CallBackDsl当做这个函数的this,所以该函数中可以不用this.
 * 这样就可以调用CallBackDsl的参数和方法
 * <reified  : ApiResult<T>, T>
 */
inline fun <reified A : ApiResult<T>, reified T> callbackApiOf(initDsl: CallBackApiDsl<T>.() -> Unit): ObserverCallBack {
    val handler = android.os.Handler(Looper.getMainLooper())

    val dsl = CallBackApiDsl<T>()
    //初始化dsl
    dsl.initDsl()

    return object : ObserverCallBack {
        override fun onStart() {
            dsl.mStart?.invoke()
        }

        override fun onHandle(response: String?, errorCode: Int) {
            var code: Int = -1
            var bean: T? = null
            val apiResult = ApiResult<T>()

            try {
                val classZ = A::class.java
                val apiResultParse = XHHttpUtils.parseApiResult(classZ, response)
                //最后处理ApiResult
                if (apiResultParse != null && apiResultParse.isOk()) {
                    val data = apiResultParse.data
                    bean = JSON.parseObject(data, T::class.java)
                    apiResult.data = bean
                    apiResult.code = apiResultParse.code
                    apiResult.msg = apiResultParse.msg
                    code = 0
                } else {
                    logE("请求码错误")
                    code = -2
                }
            } catch (e: Exception) {
                e.printStackTrace()
                code = -100
            }
            if (code == 0 && bean != null) {
                "===================onSuccess()===================".logE()
                handler.post {
                    dsl.mSuccess?.invoke(apiResult)
                }
            } else {
                "===================onFailed()===================".logE()
                handler.post {
                    dsl.mFailed?.invoke(response ?: "", code)
                }
            }
        }

        override fun onComplete() {
            dsl.mComplete?.invoke()
        }
    }
}

class CallBackApiDsl<T> {
    var mStart: (() -> Unit)? = null

    fun start(listener: () -> Unit) {
        mStart = listener
    }

    /**
     * 网络请求成功的回调
     */
    var mSuccess: ((result: ApiResult<T>) -> Unit)? = null

    fun success(listener: (result: ApiResult<T>) -> Unit) {
        mSuccess = listener
    }


    /**
     * 网络轻功但是状态码不为200
     */
    var mFailed: ((result: String, error: Int) -> Unit)? = null

    fun failed(listener: (result: String, error: Int) -> Unit) {
        mFailed = listener
    }

    var mComplete: (() -> Unit)? = null

    fun complete(listener: () -> Unit) {
        mComplete = listener
    }

}