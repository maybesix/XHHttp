package top.maybesix.xhhttp.annotation

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc TODO.
 */
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApiRename(val value: String)