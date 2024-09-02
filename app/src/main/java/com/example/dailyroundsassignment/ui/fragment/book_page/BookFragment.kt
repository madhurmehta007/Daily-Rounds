package com.example.dailyroundsassignment.ui.fragment.book_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.adapter.BookYearPagerAdapter
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.databinding.FragmentBookBinding
import com.example.dailyroundsassignment.ui.fragment.book_list_page.BookListPageFragment
import com.example.dailyroundsassignment.ui.fragment.book_list_page.BookListPageViewModel
import com.example.dailyroundsassignment.utils.Constants.Companion.HAS_LOGGED_IN
import com.example.dailyroundsassignment.utils.Constants.Companion.LOGIN_PREF
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment() {

    private lateinit var viewModel: BookListPageViewModel

    private val binding: FragmentBookBinding by lazy {
        FragmentBookBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(BookListPageViewModel::class.java)
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

        viewModel.loadBooks()
        attachObservers()
        initClicks()
    }

    private fun initClicks(){
        binding.btnLogout.setOnClickListener {
            findNavController().navigate(R.id.action_bookFragment_to_loginFragment)

            val sharedPref = requireActivity().getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean(HAS_LOGGED_IN, false)
            editor.apply()

            Toast.makeText(requireContext(), getString(R.string.logged_out), Toast.LENGTH_SHORT).show()
        }
    }

    private fun attachObservers(){
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.booksByYear.observe(viewLifecycleOwner) { booksByYear ->
            setupViewPager(booksByYear)
        }
    }

    private fun setupViewPager(booksByYear: Map<Int, List<BookData>>) {
        val years = booksByYear.keys.sortedDescending()

        val pagerAdapter = object : BookYearPagerAdapter(this, years, booksByYear) {
            override fun createFragment(position: Int): Fragment {
                val fragment =
                    BookListPageFragment.newInstance(booksByYear[years[position]] ?: emptyList())
                fragment.setOnLastItemVisibleListener {
                    if (position < years.size - 1) {
                        binding.viewPager.setCurrentItem(position + 1, true)
                    }
                }
                return fragment
            }
        }

        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = years[position].toString()
        }.attach()
    }
}
