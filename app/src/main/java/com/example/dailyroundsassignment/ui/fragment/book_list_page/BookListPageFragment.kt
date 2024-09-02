package com.example.dailyroundsassignment.ui.fragment.book_list_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyroundsassignment.adapter.BookListAdapter
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.databinding.FragmentBookListPageBinding
import com.example.dailyroundsassignment.ui.fragment.book_detail_page.BookDetailPageFragment
import com.example.dailyroundsassignment.utils.Constants.Companion.ARG_BOOKS
import com.example.dailyroundsassignment.utils.Constants.Companion.RecipeDescriptionBottomSheetTag
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookListPageFragment : Fragment() {

    private val binding: FragmentBookListPageBinding by lazy {
        FragmentBookListPageBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance(books: List<BookData>): BookListPageFragment {
            val fragment = BookListPageFragment()
            val args = Bundle()
            args.putSerializable(ARG_BOOKS, ArrayList(books))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var bookListVm: BookListPageViewModel
    private lateinit var bookAdapter: BookListAdapter
    private var onLastItemVisibleListener: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bookListVm = ViewModelProvider(requireActivity()).get(BookListPageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val books = arguments?.getSerializable(ARG_BOOKS) as? List<BookData> ?: emptyList()

        initAdapter(books)

        attachObservers()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    onLastItemVisibleListener?.invoke()
                }
            }
        })
    }

    private fun updateAdapter() {
        val year =
            arguments?.getSerializable(ARG_BOOKS)?.let { (it as List<BookData>)[0].year } ?: return
        val updatedBooks = bookListVm.getFavoriteBooksForYear(year)
        bookAdapter.updateBooks(updatedBooks)
    }

    fun setOnLastItemVisibleListener(listener: () -> Unit) {
        onLastItemVisibleListener = listener
    }

    private fun attachObservers() {
        bookListVm.allSavedBooks.observe(viewLifecycleOwner) {
            updateAdapter()
        }
    }

    private fun initAdapter(books: List<BookData>) {
        bookAdapter = BookListAdapter(books, onFavoriteClick = {
            bookListVm.handleFavoriteClick(it)
        }, onItemClick = {
            val dialog =
                BookDetailPageFragment(
                    it
                )

            dialog.isCancelable = true
            dialog.show(parentFragmentManager, RecipeDescriptionBottomSheetTag)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = bookAdapter
    }
}
