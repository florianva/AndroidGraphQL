package fr.florianvansteene.androidgraphql

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import fr.florianvansteene.androidgraphql.Adapters.LinksAdapter
import fr.florianvansteene.androidgraphql.ListViewModels.LinksModel
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.util.logging.Logger

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val BASE_URL = "http://10.107.1.149:8080/graphql"
    private lateinit var client: ApolloClient
    var listView: ListView? = null
    var adapter: LinksAdapter? = null


    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById<ListView>(R.id.lvLinks)

        btContact!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btContact) {
            progress_bar.visibility = View.VISIBLE
            client = setupApollo()
            client.query(FindQuery
                .builder()
                //.url("www.tuto.com") //Passing required argument
                .build())
                .enqueue(object : ApolloCall.Callback<FindQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<FindQuery.Data>) {

                        runOnUiThread({
                            progress_bar.visibility = View.GONE

                            val size = response.data()?.allLinks()?.size!!

                            var result = ArrayList<LinksModel>()
                            for (i in 0 until size) {
                                val data_url = response.data()?.allLinks()?.get(i)?.url()
                                val data_description = response.data()?.allLinks()?.get(i)?.description()
                                var link: LinksModel = LinksModel(data_url!!, data_description!!)
                                result.add(link)
                            }

                            adapter = LinksAdapter(this@MainActivity, result)

                            listView?.adapter = adapter
                            adapter?.notifyDataSetChanged()
                        })
                    }
                })
        }
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
