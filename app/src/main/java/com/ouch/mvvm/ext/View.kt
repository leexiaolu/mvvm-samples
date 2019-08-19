package com.ouch.mvvm.ext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.ouch.mvvm.App
import org.jetbrains.anko.intentFor

/**
 * Created by 李小璐 on 2019-08-19.
 */

inline fun <reified T : FragmentActivity> FragmentActivity.navigateTo(vararg params: Pair<String, Any?>, bundle: Bundle? = null) {
    Navigator.goto<T>(this, *params, bundle = bundle)
}

object Navigator {
    /**
     * @param ctx    可以Activity或者Application的Context,如不指定则默认是Application Context
     * @param params 传给Activity参数, 如"key" to value的键值对形式
     * @param flags  打开Activity的flag
     */
    inline fun <reified T : FragmentActivity> goto(ctx: Context = App.instance.applicationContext,
                                                   vararg params: Pair<String, Any?>,
                                                   flags: Int = 0,
                                                   bundle: Bundle? = null) {
        val intent = if (bundle != null) {
            Intent(ctx, T::class.java).apply {
                putExtras(bundle)
                setFlags(flags)
            }
        } else {
            ctx.intentFor<T>(*params).apply {
                setFlags(flags)
            }
        }
        ctx.startActivity(intent)
    }
}