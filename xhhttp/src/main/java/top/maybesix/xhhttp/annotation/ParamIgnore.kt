package top.maybesix.xhhttp.annotation

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc 用于实体类参数忽略该字段使用.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ParamIgnore()