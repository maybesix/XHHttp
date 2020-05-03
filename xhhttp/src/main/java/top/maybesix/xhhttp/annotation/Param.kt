package top.maybesix.xhhttp.annotation

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc 使用实体类作为请求参数，自动反射字段.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Param()
