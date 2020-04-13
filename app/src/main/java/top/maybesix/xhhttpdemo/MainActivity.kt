package top.maybesix.xhhttpdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.config.XHHttpConfig
import top.maybesix.xhhttp.dsl.callbackOf

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        XHHttp.baseUrl = "https://www.wanandroid.com"
        XHHttp.isDebug = true
//        HttpRequest.instance.getJson(callbackOf<String> {
//            success {
//                "请求成功".log()
//                it.log()
//            }
//            failed {
//                "请求失败".log()
//            }
//        },"0","xiaoming")

        HttpRequest.instance(
            XHHttpConfig()
        ).getPerson(callbackOf<String> {
            success {

            }
            failed {

            }
        },"110","555", Person())
        HttpRequest.instance().getHidden(callbackOf<String> {
            success {

            }
        })
    }
}
