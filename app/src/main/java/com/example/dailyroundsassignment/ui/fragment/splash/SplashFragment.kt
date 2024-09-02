package com.example.dailyroundsassignment.ui.fragment.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.utils.Constants.Companion.HAS_LOGGED_IN
import com.example.dailyroundsassignment.utils.Constants.Companion.LOGIN_PREF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2000)
            val sharedPreference = requireActivity().getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)
            val hasLoggedIn: Boolean = sharedPreference.getBoolean(HAS_LOGGED_IN, false)

            if (hasLoggedIn) {
                findNavController().navigate(R.id.action_splashFragment_to_bookFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }


}