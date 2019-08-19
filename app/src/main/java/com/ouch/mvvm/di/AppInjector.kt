package com.ouch.mvvm.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ouch.mvvm.App
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Created by 李小璐 on 2019-08-19.
 */

object AppInjector {
    fun init(app: App) {
        DaggerAppComponent.builder().application(app).build().inject(app)
        app.registerActivityLifecycleCallbacks(object : OnActivityCreated() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                injectActivity(activity)
            }
        })
    }

    private fun injectActivity(activity: Activity) {
        if (activity is Injectable || activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                    if (f is Injectable) {
                        AndroidSupportInjection.inject(f)
                    }
                }
            }, true)
        }
    }
}

abstract class OnActivityCreated : Application.ActivityLifecycleCallbacks {
    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityStopped(activity: Activity?) {}
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity?) {}
}