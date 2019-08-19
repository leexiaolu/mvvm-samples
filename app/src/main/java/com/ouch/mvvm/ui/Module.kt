package com.ouch.mvvm.ui

import androidx.lifecycle.ViewModel
import com.ouch.mvvm.di.PerActivity
import com.ouch.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by 李小璐 on 2019-08-19.
 */
@Module
abstract class MainModule {

    @PerActivity
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(vm: MainViewModel): ViewModel
}