package top.maybesix.xhhttp.util

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.alibaba.fastjson.JSON
import org.json.JSONException
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.annotation.ApiRename
import top.maybesix.xhhttp.annotation.ApiSuccessCode
import top.maybesix.xhhttp.model.ApiResult

/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc TODO.
 */
object XHHttpUtils {
    fun Any?.logD() =
        if (this != null && XHHttp.isDebug)
            Log.d(XHHttp.TAG, this.toString())
        else
            -1

    fun logD(s: String) {
        if (XHHttp.isDebug)
            Log.d(XHHttp.TAG, s)
    }

    fun Any?.log() =
        Log.d(XHHttp.TAG, this.toString())

    fun log(s: String) =
        Log.d(XHHttp.TAG, s)

    fun Any?.logE() =
        Log.e(XHHttp.TAG, this.toString())

    fun logE(s: String) {
        Log.e(XHHttp.TAG, s)
    }

    /**
     * 检查url是否合法
     * @param url String url
     * @return Boolean 结果
     */
    fun checkUrl(url: String): Boolean {
        return if (!(XHHttp.baseUrl.startsWith("http://") || XHHttp.baseUrl.startsWith("https://"))) {
            logE("baseUrl必须以“http://”或者“https://”开头")
            false
        } else if (!XHHttp.baseUrl.endsWith("/")) {
            logE("baseUrl必须以“/”结尾")
            false
        } else {
            Patterns.WEB_URL.matcher(url).matches()
        }
    }

    /**
     * 反射获取继承ApiResult的类的处理结果
     * @param classZ Class<T>
     * @param json String?
     * @return ApiResult<String>?
     * @throws JSONException
     */
    @Throws(JSONException::class)
    fun <T> parseApiResult(classZ: Class<T>, json: String?): ApiResult<String>? {
        //读取真实的值
        var realCode = "code"
        var realMsg = "msg"
        var realData = "data"
        val apiResult = ApiResult<String>()

        //获取泛型的实体类
        //反射获取所有的实体参数
        classZ.declaredFields.forEach { filed ->
            when (filed.name) {
                "code" -> {
                    if (filed.annotations.isNotEmpty()) {
                        filed.annotations.forEach {
                            if (it is ApiRename) {
                                realCode = it.value
                            } else if (it is ApiSuccessCode) {
                                apiResult.successCode = it.value
                            }
                        }
                    }
                }
                "msg" -> {
                    if (filed.annotations.isNotEmpty()) {
                        filed.annotations.forEach {
                            if (it is ApiRename) {
                                realMsg = it.value
                            }
                        }
                    }
                }
                "data" -> {
                    if (filed.annotations.isNotEmpty()) {
                        filed.annotations.forEach {
                            if (it is ApiRename) {
                                realData = it.value
                            }
                        }
                    }
                }

            }

        }
        if (TextUtils.isEmpty(json))
            return null
        val jsonObject = JSON.parseObject(json)
        if (jsonObject.containsKey(realCode)) {
            apiResult.code = jsonObject.getIntValue(realCode)
        } else {
            throw Exception("没找到code字段，请检查ApiResult是否设置正确！！")
        }
        if (jsonObject.containsKey(realMsg)) {
            apiResult.msg = jsonObject.getString(realMsg)
        } else {
            throw Exception("没找到msg字段，请检查ApiResult是否设置正确！！")
        }
        if (jsonObject.containsKey(realData)) {
            apiResult.data = jsonObject.getString(realData)
        } else {
            throw Exception("没找到data字段，请检查ApiResult是否设置正确！！")
        }
        return apiResult
    }
}