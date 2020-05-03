package top.maybesix.xhhttp.dsl

import com.alibaba.fastjson.JSON
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.model.ApiResult
import top.maybesix.xhhttp.util.XHHttpUtils
import top.maybesix.xhhttp.util.XHHttpUtils.logE
import java.lang.reflect.ParameterizedType


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
 */
inline fun <reified T, reified A> callbackApiOf(initDsl: CallBackApiDsl<T, A>.() -> Unit): ObserverCallBack {
    val dsl = CallBackApiDsl<T, A>()
    //初始化dsl
    dsl.initDsl()

    return object : ObserverCallBack {
        override fun onStart() {
            dsl.mStart?.invoke()
        }

        override fun onHandle(response: String?, errorCode: Int) {
            try {
                val classZ = T::class.java
                if (ApiResult::class.java.isAssignableFrom(classZ)) {
                    val apiResultParse = XHHttpUtils.parseApiResult(classZ, response)
                    val type =
                        (classZ.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                    val apiResult = ApiResult<A>()
                    //最后处理ApiResult
                    if (apiResultParse != null && apiResultParse.isOk()) {
                        try {
                            val data = apiResultParse.data
                            val bean: A? = try {
                                JSON.parseObject(data, type)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                val classA = A::class.java
                                val classAT =
                                    ((classA.genericSuperclass as ParameterizedType?)!!.actualTypeArguments[0]) as Class<T>
                                JSON.parseArray(data, classAT) as A
                            }
                            apiResult.data = bean
                            "===================onSuccess()===================".logE()
                            dsl.mSuccess?.invoke(apiResult)
                        } catch (e: Exception) {
                            "===================onException()===================".logE()
                            if (XHHttp.isDebug) {
                                e.printStackTrace()
                            }
                            dsl.mException?.invoke(e)
                        }
                    } else {
                        "===================onFailed()===================".logE()
                        val failResult = ApiResult<A>()
                        failResult.code = apiResultParse?.code ?: -1
                        failResult.msg = apiResultParse?.msg ?: ""
                        failResult.sourceData = response ?: ""
                        dsl.mFailed?.invoke(failResult)
                    }
                } else {
                    logE("使用callbackApiOf必须使用ApiResult的子类")
                    throw Exception("使用callbackApiOf必须使用ApiResult的子类")
                    //其他类型的数据处理
                }
            } catch (e: Exception) {
                "===================onException()===================".logE()
                if (XHHttp.isDebug) {
                    e.printStackTrace()
                }
                dsl.mException?.invoke(e)
            }
        }

        override fun onComplete() {
            "===================onComplete()===================".logE()
            dsl.mComplete?.invoke()
        }
    }
}

class CallBackApiDsl<T, A> {
    var mStart: (() -> Unit)? = null

    fun start(listener: () -> Unit) {
        mStart = listener
    }

    /**
     * 网络请求成功的回调
     */
    var mSuccess: ((result: ApiResult<A>) -> Unit)? = null

    fun success(listener: (result: ApiResult<A>) -> Unit) {
        mSuccess = listener
    }


    /**
     * 网络轻功但是状态码不为200
     */
    var mFailed: ((result: ApiResult<A>) -> Unit)? = null

    fun failed(listener: (result: ApiResult<A>) -> Unit) {
        mFailed = listener
    }

    var mComplete: (() -> Unit)? = null

    fun complete(listener: () -> Unit) {
        mComplete = listener
    }

    var mException: ((e: Exception) -> Unit)? = null

    fun exception(listener: (e: Exception) -> Unit) {
        mException = listener
    }
}