package top.maybesix.xhhttp

import android.content.Context
import okhttp3.internal.http.HttpHeaders
import top.maybesix.xhhttp.exception.HttpException
import top.maybesix.xhhttp.invocation.BaseInvocationHandler
import java.lang.reflect.Proxy

/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc XHHttp配置入口和动态代理入口.
 */
object XHHttp {
    const val TAG = "XHHttp"

    private var context: Context? = null

    ///基础的url
    var baseUrl = ""

    ///是否开启调试模式，开启后
    var isDebug = false

    ///默认的超时时间
    const val DEFAULT_MILLISECONDS = 60000

    //默认重试次数
    private const val DEFAULT_RETRY_COUNT = 3

    //默认重试延时
    private const val DEFAULT_RETRY_DELAY = 500

    //重试次数默认3次
    var retryCount = DEFAULT_RETRY_COUNT

    //延迟xxms重试
    var retryDelay = DEFAULT_RETRY_DELAY


    //全局公共请求头
    private val mCommonHeaders
            : HttpHeaders? = null

    //全局公共请求参数
    private val mCommonParams = ""


    /**
     * 初始化
     * @param context Context 来自全局的
     */
    @JvmStatic
    fun init(context: Context) {
        this.context = context
    }

    /**
     * 让接口的伴生对象调用这个方法以实现动态代理
     * @param clazz Class<T> 接口类
     * @return T 接口类
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> getInstance(clazz: Class<T>): T {
        if (context == null) {
            throw HttpException("初始化XHHttp失败！")
        }
        if (baseUrl.isEmpty()) {
            throw HttpException("baseUrl不能为空")
        }
        return (Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz),
            BaseInvocationHandler()
        )) as T
    }


}