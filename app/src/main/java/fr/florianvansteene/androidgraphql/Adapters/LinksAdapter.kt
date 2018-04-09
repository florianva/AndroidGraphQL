package fr.florianvansteene.androidgraphql.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import fr.florianvansteene.androidgraphql.ListViewModels.LinksModel
import fr.florianvansteene.androidgraphql.R

/**
 * Created by florianvansteene on 09/04/2018.
 */
class LinksAdapter(private var activity: Activity, private var items: ArrayList<LinksModel>): BaseAdapter() {

    private class ViewHolder(row: View?) {
        var tvUrl: TextView? = null
        var tvDescription: TextView? = null

        init {
            this.tvUrl = row?.findViewById<TextView>(R.id.tvUrl)
            this.tvDescription = row?.findViewById<TextView>(R.id.tvDescription)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.links_row, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var LinksModel = items[position]
        viewHolder.tvUrl?.text = LinksModel.url
        viewHolder.tvDescription?.text = LinksModel.description

        return view as View
    }

    override fun getItem(i: Int): LinksModel {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}