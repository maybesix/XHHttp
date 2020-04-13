package top.maybesix.xhhttp.annotation

/**
 * @author MaybeSix
 * @date 2020/4/10
 * @desc get请求.
 */

/**
 *
 * @property url String 请求链接
 * @property callbackName String 回调的参数名
 * @constructor
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(
    val url: String,
    val callbackName: String = "_callback"
)