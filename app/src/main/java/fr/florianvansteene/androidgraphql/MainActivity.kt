package fr.florianvansteene.androidgraphql

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {


    private val BASE_URL = "http://10.107.1.149:8080/graphql"
    private lateinit var client: ApolloClient

    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client=setupApollo()
        client.query(FindQuery    //From the auto generated class
                .builder()
                //.url("www.tuto.com") //Passing required argument
                //.description("toto")
                .build())
                .enqueue(object : ApolloCall.Callback<FindQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }
                    override fun onResponse(response: Response<FindQuery.Data>) {
                        Log.info(" " + response.data()?.allLinks()?.get(0)?.url())
                        Log.info(" " + response.data()?.allLinks()?.get(0)?.description())
                        runOnUiThread({
                            //Log.info("toto")
                            //Log.info(response.data()?.allLinks())
                            //progress_bar.visibility = View.GONE
                            /*name_text_view.text = String.format(getString(R.string.name_text),
                                    response.data()?.repository()?.name())
                            description_text_view.text = String.format(getString(R.string.description_text),
                                    response.data()?.repository()?.description())
                            forks_text_view.text = String.format(getString(R.string.fork_count_text),
                                    response.data()?.repository()?.forkCount().toString())
                            url_text_view.text = String.format(getString(R.string.url_count_text),
                                    response.data()?.repository()?.url().toString())*/
                        })
                    }
                })
    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor({ chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    chain.proceed(builder.build())
                })
                .build()
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttp)
                .build()
    }
}
