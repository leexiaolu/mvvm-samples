package com.ouch.mvvm.di

import com.ouch.mvvm.ui.MainActivity
import com.ouch.mvvm.ui.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by 李小璐 on 2019-08-19.
 */

/**
 * 所有需要依赖注入的Activity都要在ActivityBindingModule要在这里声明,
 * 同时Activity也要实现Injectable或HasSupportFragmentInjector接口.
 *
 * 该Activity或该Activity内含的Fragment的依赖, 放到该Activity同目录下的Module文件中,
 * 其中的依赖要添加PerActivity或PerFragment注解, 以保证生命周期就与Activity一致.
 */

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity

}