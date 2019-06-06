package io.github.ivannarino.android.codingchallenge.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.ivannarino.android.codingchallenge.R
import io.github.ivannarino.android.codingchallenge.databinding.ConversionFragmentBinding

class ConversionFragment : Fragment() {

    companion object {
        fun newInstance() = ConversionFragment()
    }

    private lateinit var viewModel: ConversionViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: ConversionFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.conversion_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(ConversionViewModel::class.java)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        return binding.root
    }
}
