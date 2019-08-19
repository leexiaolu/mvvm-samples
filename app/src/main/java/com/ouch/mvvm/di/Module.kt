package com.ouch.mvvm.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ouch.mvvm.App
import com.ouch.mvvm.BuildConfig
import com.ouch.mvvm.constant.Constants
import com.ouch.mvvm.data.source.UserService
import com.ouch.mvvm.helper.NetworkManager
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.attempt
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by 李小璐 on 2019-08-19.
 */

@Module(includes = [PreferenceModule::class, ApiModule::class])
class AppModule {
    @Provides
    @Singleton
    @Named("app")
    fun providerAppContext(): Context {
        return App.instance.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("YourAppName", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRxSharedPreferences(prefs: SharedPreferences): RxSharedPreferences {
        return RxSharedPreferences.create(prefs)
    }
}

@Module
class PreferenceModule {
    @Provides
    @Named(Constants.PREF_USER_TOKEN)
    fun providerPreferenceUserToken(preferences: RxSharedPreferences): Preference<String> {
        return preferences.getString(Constants.PREF_USER_TOKEN)
    }
}

@Module
class ApiModule {

    @Provides
    @Singleton
    @Named("base_api")
    fun provideUserUrl(): HttpUrl {
        return HttpUrl.parse(BuildConfig.BASE_URL)!!
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    @Named("api")
    fun provideApiClient(client: OkHttpClient,
                         logger: HttpLoggingInterceptor,
                         cacheFolder: Cache?,
//                         @Named("params") params: Interceptor,
                         @Named("caches") caches: Interceptor,
                         @Named("auth") auth: Interceptor
    ): OkHttpClient {
        return client.newBuilder().apply {
            addInterceptor(caches)
//            addInterceptor(params)
            addInterceptor(auth)
            addInterceptor(logger)
            retryOnConnectionFailure(true)
            connectTimeout(30000, TimeUnit.MILLISECONDS)
            readTimeout(30000, TimeUnit.MILLISECONDS)
            writeTimeout(30000, TimeUnit.MILLISECONDS)
            cacheFolder?.let { cache(it) }
        }.build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @Named("retrofit")
    fun provideUserRetrofit(@Named("base_api") baseUrl: HttpUrl,
                            @Named("api") client: OkHttpClient,
                            gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    @Singleton
    @Named("user")
    fun provideUserService(@Named("retrofit") retrofit: Retrofit): UserService {
        return retrofit.create<UserService>(UserService::class.java)
    }

    @Provides
    @Singleton
    @Named("auth")
    fun authInterceptor(@Named(Constants.PREF_USER_TOKEN) token: Preference<String>): Interceptor {
        return Interceptor {
            val builder = it.request().newBuilder()
            // is login?
            builder.addHeader("token", token.get())
            it.proceed(builder.build())
        }
    }

    @Provides
    @Singleton
    fun provideCache(): Cache? {
        attempt {
            return Cache(File(App.instance.cacheDir, "http-cache"), CacheSize.LARGE)
        }
        return null
    }

    @Provides
    @Named("caches")
    fun provideOfflineCacheIInterceptor(network: NetworkManager): Interceptor {
        return Interceptor {
            var request = it.request()
            if (!network.isConnected && isCacheable(request)) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            it.proceed(request)
        }
    }

    private fun isCacheable(req: Request): Boolean {
        if (req.method() !== "GET")
            return false
        return true
    }

    @Provides
    @Named("params")
    fun provideCommonParamsInterceptor(): Interceptor {
        return Interceptor {
            var request = it.request()
            val url = request.url()
            val urlBuilder = url.newBuilder()
            urlBuilder.addQueryParameter("timezone", TimeZone.getDefault().id)
            request = request.newBuilder().url(urlBuilder.build()).build()
            it.proceed(request)
        }
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { Timber.d(it) }.apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
}

object CacheSize {
    const val LARGE = 10 * 1024 * 1024L
}