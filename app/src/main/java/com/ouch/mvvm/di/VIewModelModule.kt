package com.ouch.mvvm.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import kotlin.reflect.KClass

/**
 * Created by 李小璐 on 2019-08-19.
 */

/**
 * 参考: https://blog.kotlin-academy.com/understanding-dagger-2-multibindings-viewmodel-8418eb372848
 */
/*****************************
 * 声明ViewModelKey注解
 * 该注用做把ViewModel的类型与Module中提供该ViewModel的provider关联(与@Bind注解一起),
 * 生成的Map作为ViewModelFactory构造函数的参数
 *****************************/
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}