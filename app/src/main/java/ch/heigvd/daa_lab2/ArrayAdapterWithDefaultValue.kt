package ch.heigvd.daa_lab2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes

class ArrayAdapterWithDefaultValue<T>(
    context: Context,
    @LayoutRes private val resource: Int,
    objects: List<T>,
    private val hintText: String
) : ArrayAdapter<T>(context, resource, listOf(null) + objects) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position == 0) {
            return TextView(context).apply {
                height = 0
                tag = 1 // indique que cette vue est un "hint"
            }
        }

        return if (convertView?.tag == 1) { // si la vue précédente est un "hint", il faut en créer une nouvelle
            super.getDropDownView(position, null, parent)
        } else {
            super.getDropDownView(position, convertView, parent)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position == 0) {
            return getHintView(parent)
        }
        return super.getView(position, convertView, parent)
    }

    private fun getHintView(parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent, false)

        view.findViewById<TextView>(android.R.id.text1).also {
            it.hint = this.hintText
        }

        return view
    }
}