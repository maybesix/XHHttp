package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.annotation.HttpIgnore
import top.maybesix.xhhttp.annotation.HttpRename

/**
 * @author MaybeSix
 * @date 2020/4/13
 * @desc TODO.
 */
    data class Person(
    val name: String = "xiaoming",
    @HttpIgnore()
    val id: String = "777",
    @HttpRename("qqEmail")
    val email: String = "1478285@qq.com"
)