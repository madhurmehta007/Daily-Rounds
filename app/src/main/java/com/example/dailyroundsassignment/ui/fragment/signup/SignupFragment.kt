package com.example.dailyroundsassignment.ui.fragment.signup

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dailyroundsassignment.R
import com.example.dailyroundsassignment.databinding.FragmentSignupBinding
import com.example.dailyroundsassignment.networking.ApiInterface
import com.example.dailyroundsassignment.utils.Constants.Companion.BASE_URL_1
import com.example.dailyroundsassignment.utils.PasswordValidator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel: SignupViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    private var countryList:MutableList<String> = mutableListOf()
    private lateinit var retrofit: Retrofit
    private lateinit var api: ApiInterface
    private var defaultCountry:String? = null
    private val binding: FragmentSignupBinding by lazy {
        FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiInterface::class.java)

       CoroutineScope(Dispatchers.IO).launch {
           try{
            val response = api.getNetworkData()
               viewModel._networkDataResponse.postValue(response)
           }catch (e:IOException){
               Log.e("Error", "Exception: ${e.message}")
           }
       }

        viewModel.networkDataResponse.observe(viewLifecycleOwner){
            if (it.body() != null){
               defaultCountry = it.body()?.country
            }
        }

        viewModel.getCountryList()
        setupListeners()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val repeatPassword = binding.etRepeatPassword.text.toString().trim()
            val country = binding.spinnerCountry.selectedItem.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && country.isNotEmpty()  && (password==repeatPassword)) {
                signUpUser(email, password, country)
            } else {
                Toast.makeText(context, "Please check all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        viewModel.countryListResponse.observe(viewLifecycleOwner) { response ->
            response.body()?.map { it.country }?.let { countries ->
                countryList.clear()
                countryList.addAll(countries)
                updateSpinner()
            }
        }

    }

    private fun setupListeners() {
        binding.etPassword.addTextChangedListener(passwordTextWatcher)
    }

    private fun updateSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapter

        val defaultPosition = if (defaultCountry != null && countryList.contains(defaultCountry)) {
            countryList.indexOf(defaultCountry)
        } else {
            0
        }

        binding.spinnerCountry.setSelection(defaultPosition)

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCountry = countryList[position]
                Toast.makeText(requireContext(), "Selected: $selectedCountry", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updatePasswordStrength(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }


    private fun updatePasswordStrength(password: String) {
        val result = PasswordValidator.validatePassword(password)

        binding.tvPasswordStrength.visibility = View.VISIBLE
        binding.llPasswordStrength.visibility = View.VISIBLE

        when (result) {
            "Strong" -> {
                binding.dot1.setImageResource(R.drawable.ic_dot_active)
                binding.dot2.setImageResource(R.drawable.ic_dot_active)
                binding.dot3.setImageResource(R.drawable.ic_dot_active)
            }
            "Moderate" -> {
                binding.dot1.setImageResource(R.drawable.ic_dot_active)
                binding.dot2.setImageResource(R.drawable.ic_dot_active)
                binding.dot3.setImageResource(R.drawable.ic_dot_inactive)
            }
            else -> {
                binding.dot1.setImageResource(R.drawable.ic_dot_active)
                binding.dot2.setImageResource(R.drawable.ic_dot_inactive)
                binding.dot3.setImageResource(R.drawable.ic_dot_inactive)
            }
        }

        binding.tvPasswordStrength.text = result
    }

    private fun signUpUser(email: String, password: String, country:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    val sharedPref = requireActivity().getSharedPreferences("Country", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("selectedCountry", country)
                    editor.apply()
                    Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

                } else {
                    Toast.makeText(context, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}