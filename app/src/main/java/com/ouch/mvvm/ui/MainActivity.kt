package com.ouch.mvvm.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ouch.mvvm.R
import com.ouch.mvvm.base.BaseActivity
import com.ouch.mvvm.databinding.ActivityMainBinding
import com.ouch.mvvm.di.Injectable
import javax.inject.Inject
import com.ouch.mvvm.ext.ViewModelFrom

class MainActivity : BaseActivity(), Injectable {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var vm: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelFrom(factory)
        binding = bind(R.layout.activity_main) {
            it.vm = vm
        }
    }
}
