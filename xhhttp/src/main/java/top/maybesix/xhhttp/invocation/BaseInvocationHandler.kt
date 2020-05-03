package top.maybesix.xhhttp.invocation

import android.os.Handler
import android.os.Looper
import okhttp3.*
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.annotation.*
import top.maybesix.xhhttp.callback.ObserverCallBack
import top.maybesix.xhhttp.config.XHHttpConfig
import top.maybesix.xhhttp.exception.HttpException
import top.maybesix.xhhttp.util.XHHttpUtils
import top.maybesix.xhhttp.util.XHHttpUtils.log
import top.maybesix.xhhttp.util.XHHttpUtils.logD
import top.maybesix.xhhttp.util.XHHttpUtils.logE
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.net.UnknownHostException
import kotlin.concurrent.thread
import kotlin.reflect.jvm.kotlinFunction


/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc 具体的代理实现.
 */
class BaseInvocationHandler(config: XHHttpConfig?) : InvocationHandler {
    val handler = Handler(Looper.getMainLooper())

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        //ps:这里为了方便就直接new Thread了,如果是使用的话可以使用线程池或者kt协程,消耗会低很多,一般项目中是不允许直接new Thread的
        thread {
            if (XHHttp.baseUrl.isEmpty()) {
                throw HttpException("baseUrl不能为空")
            }
            if (!XHHttpUtils.checkUrl(XHHttp.baseUrl)) {
                throw HttpException("baseUrl不合法，请检查！！！")
            }

            //获取方法的注解,先获取get注解,如果为空就获取post注解;
            // ps:自己用的时候可以先获取常用的注解,这样就不用判断两次了,比如项目里大部分都是post请求,那就先获取POST
            val annotation =
                method?.getAnnotation(GET::class.java)
                    ?: method?.getAnnotation(POST::class.java)
            //这个地方区分函数上的注解
            when (annotation) {
                is GET -> startGet(proxy, method, args, annotation)
                is POST -> startPost(proxy, method, args, annotation)
                else -> throw RuntimeException("亲,${method?.name}方法是不是忘加注解了?")
            }

        }
        return Unit
    }

    //post请求
    private fun startPost(proxy: Any?, method: Method?, args: Array<out Any>?, post: POST) {
        //post就不写了,大家可以在这里二次封装网络请求,比如使用okhttp,或者使用Socket,甚至可以用别人二次或者三次封装好的网络请求
        //获取接口地址和callback
        val (completeUrl, params, callback) = handleUrlAndGetCallback(
            post.url,
            post.callbackName,
            method,
            args
        )
        val baseUrl = XHHttp.baseUrl
        //添加处理过的url的路径
        val request = getRequestBody(post, baseUrl, completeUrl, params)
        makeARequest(request, callback)
    }

    //get请求
    private fun startGet(proxy: Any?, method: Method?, args: Array<out Any>?, get: GET) {
        //获取接口地址和callback
        var (completeUrl, params, callback) = handleUrlAndGetCallback(
            get.url,
            get.callbackName,
            method,
            args
        )
        val baseUrl = XHHttp.baseUrl
        val request = getRequestBody(get, baseUrl, completeUrl, params)
        makeARequest(request, callback)
    }

    /**
     * 处理参数，返回url和callback
     * @param method Method?
     * @param args Array<out Any>?
     */
    private fun handleUrlAndGetCallback(
        url: String,
        callbackName: String,
        method: Method?,
        args: Array<out Any>?
    ): Triple<String, Map<String, String>, ObserverCallBack?> {
        var pathUrl = url
        var callback: ObserverCallBack? = null

        //参数Map
        val params = mutableMapOf<String, String>()

        //此处用反射遍历参数
        method?.kotlinFunction?.parameters?.forEachIndexed { index, kParameter ->

            when (kParameter.name) {
                null -> {
                    //接口本身的对象,我们不需要
                }
                callbackName -> {
                    //回调对象,ps:index-1是因为parameters的第0位置是代理类对象
                    callback = args?.get(index - 1) as? ObserverCallBack
                }
                else -> {
                    //以下处理被注解标注的参数
                    if (kParameter.annotations.isNotEmpty()) {
                        kParameter.annotations.forEach {
                            if (it is Path) {
                                //如果是Path参数，说明要替换
                                val oldStr = it.value
                                val newStr = args?.get(index - 1).toString()
                                pathUrl = pathUrl.replace("{$oldStr}", newStr)
                            } else if (it is Param) {
                                //要把实体类转换成请求参数
                                val paramObj = args?.get(index - 1)
                                val fields = paramObj?.javaClass?.declaredFields
                                //以下为对实体类的注解做判断
                                fields?.forEach continuing@{ field ->
                                    var key: String = field.name
                                    // 允许访问私有字段
                                    field.isAccessible = true
                                    if (field.annotations.isNotEmpty()) {
                                        field.annotations.forEach { annotation ->
                                            if (annotation is ParamIgnore) {
                                                return@continuing
                                            }
                                            if (annotation is ParamRename) {
                                                key =
                                                    field.getAnnotation(ParamRename::class.java)?.value
                                                        ?: ""
                                            }
                                        }
                                    }
                                    try {
                                        // 获取字段的对象
                                        val obj = field.get(paramObj) ?: return@forEachIndexed
                                        // 如果这个是一个普通的参数
                                        if (obj is Map<*, *>) {
                                            for (o in obj.keys) {
                                                if (o != null && obj[o] != null) {
                                                    params[o.toString()] = obj[o].toString()
                                                }
                                            }
                                        } else {
                                            params[key] = obj.toString()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                        }
                    } else {
                        //依次将参数添加到url上
                        params[kParameter.name.toString()] = args?.get(index - 1).toString()
                    }
                }
            }

        }

        return Triple(pathUrl, params, callback)
    }

    /**
     * 对url做最后的处理
     * @param completeUrl String
     * @param params Map<String, String>
     */
    private fun getUrl(
        type: Annotation,
        baseUrl: String,
        pathUrl: String,
        params: Map<String, String>
    ): String {
        var completeUrl = pathUrl
        if (type is GET) {
            //是否追加了'?'
            var isAddQuestionMark = false
            //构建参数
            for ((key, value) in params) {
                //进行拼接url
                if (!isAddQuestionMark) {
                    completeUrl = "$completeUrl?"
                    isAddQuestionMark = true
                }
                completeUrl = "$completeUrl${key}=${value}&"
            }
            //清除最后一个&
            if (completeUrl.isNotEmpty() && completeUrl.last() == '&') {
                completeUrl = completeUrl.substring(0, completeUrl.length - 1);
            }
        }

        //添加处理过的url的路径
        val url = baseUrl + completeUrl
        //默认去除空格
        url.trim()
        "请求的地址：${url}".log()
        return url
    }

    /**
     * 获取请求体
     * @param type Annotation
     * @param baseUrl String
     * @param completeUrl String
     * @param params Map<String, String>
     * @return Request
     */
    private fun getRequestBody(
        type: Annotation,
        baseUrl: String,
        completeUrl: String,
        params: Map<String, String>
    ): Request {
        val url = getUrl(type, baseUrl, completeUrl, params)
        val builder = Request.Builder().url(url)
        if (type is GET) {
            builder.get()
        } else if (type is POST) {
            val formBody = FormBody.Builder()
            for ((k, v) in params) {
                formBody.add(k, v)
            }
            //构建请求体
            builder.post(formBody.build())
        }
        val request = builder.build()
        //打印相关参数
        request.headers().logD()
        request.body().logD()
        request.url().logD()
        request.method().logD()
        return request
    }

    /**
     * 发起请求和请求处理函数
     * @param request Request
     * @param callback ObserverCallBack?
     */
    private fun makeARequest(request: Request, callback: ObserverCallBack?) {
        val client = OkHttpClient()
        val call: Call = client.newCall(request)
        var data = ""
        var errorCode = -1
        //url不能有中文，此处应该做处理
        handler.post {
            "===================onStart()===================".logE()
            callback?.onStart()
        }
        try {
            val response: Response = call.execute()
            data = response.body()?.string() ?: ""
            "===================response===================".logD()
            "response：$response".logD()
            "data: $data".logD()
            //无错误
            errorCode = 0
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            callback?.onHandle(data, errorCode)
            "===================onComplete()===================".logE()
            handler.post {
                callback?.onComplete()
            }
        }
    }
}