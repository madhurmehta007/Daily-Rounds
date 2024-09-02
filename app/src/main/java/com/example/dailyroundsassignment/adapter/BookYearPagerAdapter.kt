package com.example.dailyroundsassignment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.ui.fragment.book_list_page.BookListPageFragment


open class BookYearPagerAdapter(
    fragment: Fragment,
    private val years: List<Int>,
    private val groupedBooks: Map<Int, List<BookData>>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = years.size

    override fun createFragment(position: Int): Fragment {
        val year = years[position]
        val books = groupedBooks[year] ?: emptyList()
        return BookListPageFragment.newInstance(books)
    }

}
