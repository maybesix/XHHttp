package top.maybesix.xhhttpdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.maybesix.xhhttpdemo.CommonExt.print
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            URL(HttpConfig.ROOT_URL + "article/list/0/json?cid=1").readText().print()
        }//这里,如果没问题的话就会在logcat中打印出本次网络请求返回的数据

        HttpFunctions.instance.getJson(null, "1")

    }
}
