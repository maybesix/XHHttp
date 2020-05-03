package top.maybesix.xhhttp.annotation

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc 用于实体类参数，重命名字段.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ParamRename(val value:String)