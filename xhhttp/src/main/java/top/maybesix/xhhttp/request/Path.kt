package top.maybesix.xhhttp.request

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc TODO.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Path(val value: String)
