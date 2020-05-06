package top.maybesix.xhhttpdemo

import top.maybesix.xhhttp.annotation.ApiRename
import top.maybesix.xhhttp.annotation.ApiSuccessCode
import top.maybesix.xhhttp.model.ApiResult

/**
 * @author MaybeSix
 * @date 2020/5/6
 * @desc TODO.
 */
class MyApiResult<T> : ApiResult<T>() {
    @ApiRename("errorCode")
    override var code: Int = 0

    @ApiRename("errorMsg")
    override var msg: String = ""
    @ApiSuccessCode(0)
    override var successCode: Int = 0

}