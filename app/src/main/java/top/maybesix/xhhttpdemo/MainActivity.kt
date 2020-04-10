package top.maybesix.xhhttpdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.maybesix.xhhttp.dsl.callbackOf
import top.maybesix.xhhttp.util.CommUtils.log
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            HttpRequest.instance.getJson(callbackOf<String> {
                success {
                    "请求成功".log()
                    it.log()
                }
                failed {
                    "请求失败".log()
                }
            }, "0")
        }

    }
}
