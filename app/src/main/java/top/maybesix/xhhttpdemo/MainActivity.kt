package top.maybesix.xhhttpdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import top.maybesix.xhhttp.XHHttp
import top.maybesix.xhhttp.dsl.callbackApiOf
import top.maybesix.xhhttp.dsl.callbackOf
import top.maybesix.xhhttpdemo.param.Author

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //设置全局配置
        //配置baseUrl
        XHHttp.baseUrl = "https://www.wanandroid.com/"
        //开启调试模式
        XHHttp.isDebug = true
        initListener()
    }

    private fun initListener() {
        //直接发起get请求
        btn_get.setOnClickListener {
            HttpRequest.instance().getChapters(callbackOf<String> {
                start { }
                success {}
                failed { data, error ->
                }
                complete { }
            })
        }
        //直接发起post请求
        btn_post.setOnClickListener {
            HttpRequest.instance().search(
                callbackOf<String> {
                    success {

                    }
                },
                "kotlin"
            )
        }

        //发起请求，通过@Path注解动态替换url中的字段
        btn_get_path.setOnClickListener {
            HttpRequest.instance().getArticleJson(
                callbackOf<String> {
                    success {
                        showResult(it)
                    }
                    failed { data, error ->

                    }
                },
                "0"
            )
        }

        //以实体类为参数
        btn_get_param.setOnClickListener {
            HttpRequest.instance()
                .getArticleByAuthorName(
                    callbackOf<String> {
                        success {
                            showResult(it)
                        }

                    },
                    Author("鸿洋", "777")
                )
        }

        //直接发起get请求，使用apiresult接收
        btn_get_param_api.setOnClickListener {
            HttpRequest.instance().getChapters(
                callbackApiOf<MyApiResult<String>, String> {
                    success {
                        showResult(it.code.toString())
                        showResult(it.msg)
                    }
                    failed { data, error ->
                        showResult(error.toString())
                    }
                }
            )
        }

    }

    private fun showResult(result: String) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        Log.i("请求成功的结果showResult ", result)
    }
}
