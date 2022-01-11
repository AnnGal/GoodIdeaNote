package an.gal.android.goodideanote.idea_new

import an.gal.android.goodideanote.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PurposeAdapter(context: Context, list: List<String>) : ArrayAdapter<String>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val purpose = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.card_spinner_simple_purpose,
            parent,
            false
        )

        view.findViewById<TextView>(R.id.purpose).text = purpose
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val purpose = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.card_spinner_simple_purpose,
            parent,
            false
        )

        purpose?.let {
            view.findViewById<TextView>(R.id.purpose).text = purpose
        }

        return view
    }
}
