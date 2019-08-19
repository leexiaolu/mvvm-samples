package com.ouch.mvvm.di

import javax.inject.Scope

/**
 * Created by 李小璐 on 2019-08-19.
 */

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment