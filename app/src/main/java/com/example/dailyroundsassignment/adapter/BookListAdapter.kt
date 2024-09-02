package com.example.dailyroundsassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.databinding.ItemBookBinding
import com.squareup.picasso.Picasso

class BookListAdapter(
    private var books: List<BookData>,
    private val onFavoriteClick: (BookData) -> Unit,
    val onItemClick: (BookData) -> Unit
) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    class BookViewHolder(val binding: ItemBookBinding, context: Context) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
        return BookViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

       holder.binding.tvBookTitle.text = book.title
        holder.binding.tvBookRating.text = book.popularity.toString()
        if(book.image?.isEmpty() == true){
            Picasso.get().load(R.drawable.ic_book_placeholder).into(holder.binding.ivBookCover)
        }else{
            Picasso.get().load(book.image).into(holder.binding.ivBookCover)
        }
        holder.binding.tvBookPublishYear.text = "Published in ${book.year}"
        if(book.favorite){
            holder.binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite)
        }else{
            holder.binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_border)
        }

        holder.binding.ivFavoriteIcon.setOnClickListener {
            val updatedBook = book.copy(favorite = !book.favorite)

            onFavoriteClick(updatedBook)
            notifyItemChanged(position)
        }
        holder.binding.bookCardItem.setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int = books.size


    fun updateBooks(newBooks: List<BookData>) {
        books = newBooks
        notifyDataSetChanged()
    }
}

