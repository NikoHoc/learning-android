package com.dicoding.mechanicalkeyboards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text


class ListKeyboardAdapter (private val listKeyboard: ArrayList<Keyboard>) : RecyclerView.Adapter<ListKeyboardAdapter.ListViewHolder> () {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_item_price)
        //val tvSpecification : TextView = itemView.findViewById(R.id.item_sp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_keyboard, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listKeyboard.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, specification, photo, price) = listKeyboard[position]

        holder.tvName.text = name
        holder.tvDescription.text = description
        holder.imgPhoto.setImageResource(photo)
        holder.tvPrice.text = String.format("Rp %,d", price)

        //menambahkan data lewat fungsi interface
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listKeyboard[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Keyboard)
    }

}