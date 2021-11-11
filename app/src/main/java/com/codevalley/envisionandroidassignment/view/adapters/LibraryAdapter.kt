package com.codevalley.envisionandroidassignment.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.codevalley.envisionandroidassignment.R
import com.codevalley.envisionandroidassignment.databinding.ItemLibraryBinding
import com.codevalley.envisionandroidassignment.model.library.Library
import java.util.ArrayList

class LibraryAdapter(val context: Context, private val navController: NavController) :
    RecyclerView.Adapter<LibraryAdapter.ViewHolder>() {
    private var itemList: ArrayList<Library>? = null
    private var layoutInflater: LayoutInflater? = null

    init {
        itemList = ArrayList()
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLibraryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvDate.text = itemList?.get(position)?.date
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("paragraph", itemList?.get(position)?.paragraph)
            val navOptions = NavOptions.Builder().build()
            navController.navigate(
                R.id.action_libraryFragment_to_paragraphFragment,
                bundle,
                navOptions
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(data: List<Library>?) {
        itemList!!.clear()
        itemList!!.addAll(data!!)
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemLibraryBinding) : RecyclerView.ViewHolder(binding.root)

}