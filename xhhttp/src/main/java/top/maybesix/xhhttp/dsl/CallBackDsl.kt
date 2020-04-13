package top.maybesix.xhhttp.dsl

import com.alibaba.fastjson.JSONObject
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.util.XHHttpUtils.logD

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
inline fun <reified T> callbackOf(initDsl: CallBackDsl<T>.() -> Unit): ObserverCallBack {
    val dsl = CallBackDsl<T>()
    //初始化dsl
    dsl.initDsl()

    return object : ObserverCallBack {
        override fun onStart() {
            "请求开始 onStart()".logD()
            dsl.mStart?.invoke()
        }

        override fun onHandle(data: String?, encoding: Int, method: Int) {

            //可以在这里根据业务判断是否请求成功
            //引入fastjson来解析json    implementation 'com.alibaba:fastjson:1.2.67'
            val bean = JSONObject.parseObject(data, T::class.java)
            if (bean != null) {
                dsl.mSuccess?.invoke(bean)
            } else {
                dsl.mFailed?.invoke(data)
            }
        }

        override fun onComplete() {
            dsl.mComplete?.invoke()
        }
    }
}

class CallBackDsl<T> {
    var mStart: (() -> Unit)? = null

    fun start(listener: () -> Unit) {
        mStart = listener
    }

    /**
     * 网络请求成功的回调
     */
    var mSuccess: ((bean: T) -> Unit)? = null

    fun success(listener: (bean: T) -> Unit) {
        mSuccess = listener
    }

    /**
     * 网络请求失败的回调
     */
    var mFailed: ((data: String?) -> Unit)? = null

    fun failed(listener: (data: String?) -> Unit) {
        mFailed = listener
    }

    var mComplete: (() -> Unit)? = null

    fun complete(listener: () -> Unit) {
        mComplete = listener
    }

}