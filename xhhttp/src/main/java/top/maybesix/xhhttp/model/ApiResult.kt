package top.maybesix.xhhttp.model

import com.alibaba.fastjson.JSON

/**
 * @author MaybeSix
 * @date 2020/4/16
 * @desc TODO.
 */
open class ApiResult<T>(open var code: Int, open var msg: String, open var data: T?) {
    constructor() : this(0, "", null)

    var sourceData: String = ""
    var successCode = 0
    fun isOk(): Boolean {
        return code == successCode
    }

    override fun toString(): String {

        return JSON.toJSONString(this)
    }
}