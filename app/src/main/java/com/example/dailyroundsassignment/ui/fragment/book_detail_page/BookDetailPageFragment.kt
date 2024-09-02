package com.example.dailyroundsassignment.ui.fragment.book_detail_page

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.SavedBookData
import com.example.dailyroundsassignment.databinding.FragmentBookDetailPageBinding
import com.example.dailyroundsassignment.ui.fragment.book_list_page.BookListPageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailPageFragment(val bookData: BookData) : BottomSheetDialogFragment() {

    private lateinit var viewModel: BookListPageViewModel
    lateinit var binding: FragmentBookDetailPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val animeInflater = LayoutInflater.from(requireContext())
        binding = FragmentBookDetailPageBinding.inflate(animeInflater)
        viewModel = ViewModelProvider(requireActivity()).get(BookListPageViewModel::class.java)
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

        binding.ivFavoriteIcon.setOnClickListener {
            val updatedBook = bookData.copy(favorite = !bookData.favorite)
            viewModel.handleFavoriteClick(updatedBook)

            bookData.favorite = !bookData.favorite

            if (bookData.favorite) {
                binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}