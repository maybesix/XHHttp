package top.maybesix.xhhttp.annotation

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc 用于改变成功码.
 */
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApiSuccessCode(val value: Int)