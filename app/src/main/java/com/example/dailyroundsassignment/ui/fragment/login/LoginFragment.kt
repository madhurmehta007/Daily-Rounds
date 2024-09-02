package com.example.dailyroundsassignment.ui.fragment.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.databinding.FragmentLoginBinding
import com.example.dailyroundsassignment.utils.Constants.Companion.HAS_LOGGED_IN
import com.example.dailyroundsassignment.utils.Constants.Companion.LOGIN_PREF
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
    }

    private fun initClicks() {
        binding.apply {
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }

            btnLogIn.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    saveLoginSession()
                    loginUser(email, password)
                } else {
                    Toast.makeText(context,
                        getString(R.string.please_fill_all_the_fields), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context,
                        getString(R.string.successfully_logged_in), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_bookFragment)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.login_failed, task.exception?.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveLoginSession() {
        val sharedPref = requireActivity().getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(HAS_LOGGED_IN, true)
        editor.apply()
    }
}