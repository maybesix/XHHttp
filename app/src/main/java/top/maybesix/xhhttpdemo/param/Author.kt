package top.maybesix.xhhttpdemo.param

import top.maybesix.xhhttp.annotation.ParamIgnore
import top.maybesix.xhhttp.annotation.ParamRename

/**
 * @author MaybeSix
 * @date 2020/5/3
 * @desc TODO.
 */
data class Author(
    @ParamRename("author")
    var name: String,
    @ParamIgnore()
    var id: String
)