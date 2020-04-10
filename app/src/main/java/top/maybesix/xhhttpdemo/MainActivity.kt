package top.maybesix.xhhttpdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.dsl.callbackOf
import top.maybesix.xhhttp.util.XHHttpUtils.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        XHHttp.baseUrl = "https://www.wanandroid.com/"

        HttpRequest.instance.getJson(callbackOf<String> {
            success {
                "请求成功".log()
                it.log()
            }
            failed {
                "请求失败".log()
            }
        })


    }
}
