package com.example.dailyroundsassignment.ui.fragment.book_detail_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.databinding.FragmentBookDetailPageBinding
import com.example.dailyroundsassignment.ui.fragment.book_list_page.BookListPageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailPageFragment(private val bookData: BookData) : BottomSheetDialogFragment() {

    private lateinit var viewModel: BookListPageViewModel

    private val binding: FragmentBookDetailPageBinding by lazy {
        FragmentBookDetailPageBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity())[BookListPageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initClicks()
    }

    private fun initClicks() {
        binding.apply {
            ivFavoriteIcon.setOnClickListener {
                val updatedBook = bookData.copy(favorite = !bookData.favorite)
                viewModel.handleFavoriteClick(updatedBook)
                bookData.favorite = !bookData.favorite
                if (bookData.favorite) {
                    ivFavoriteIcon.setImageResource(R.drawable.ic_favorite)
                } else {
                    ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }

    }

    private fun setData() {
        binding.apply {
            Picasso.get().load(bookData.image).into(ivBookCover)
            tvBookTitle.text = bookData.title
            tvRating.text = getString(R.string.rating, bookData.score.toString())
            tvBookYear.text = getString(R.string.publish_year, bookData.year.toString())

            if (bookData.favorite) {
                ivFavoriteIcon.setImageResource(R.drawable.ic_favorite)
            } else {
                ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }

}