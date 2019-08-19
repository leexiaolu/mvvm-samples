package com.ouch.mvvm.ui

import com.f2prateek.rx.preferences2.Preference
import com.ouch.mvvm.base.BaseViewModel
import com.ouch.mvvm.constant.Constants
import com.ouch.mvvm.data.source.UserDataSource
import com.ouch.mvvm.di.PerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by 李小璐 on 2019-08-19.
 */
@PerActivity
class MainViewModel @Inject constructor(
    private val user: UserDataSource,
    @param:Named(Constants.PREF_USER_TOKEN) val token: Preference<String>
) : BaseViewModel() {

    fun signIn() {
        val json = JSONObject()
        json.put("userName", "username")
        json.put("passWord", "password")
        Timber.e("sign in parameters --- $json")
        val body = RequestBody.create(MediaType.parse("application/json"), json.toString())

        /**
         * 需在build.gradle中修改 baseUrl
         * 仅提供示例代码
         */
        disposables.add(
            user.signIn(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    Timber.e("$result")
                        // success set token

                },{
                    Timber.e("$it")
                    // failed -> error

                })
        )
    }
}