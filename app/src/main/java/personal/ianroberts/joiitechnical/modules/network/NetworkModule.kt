package personal.ianroberts.joiitechnical.modules.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import personal.ianroberts.joiitechnical.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    //very generic gson factory here, but can be used for more specific naming schemas, or create a moshi adapter
    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().create()

    //cache for performance improves with okhttp to cache responses and return 304s for a not-changed response
    @Provides
    @Singleton
    fun provideCacheDir(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir, "beer"), (10 * 1024 * 1024).toLong())
    }

    @Provides
    @Singleton
    fun provideHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder().run {
                        addHeader("Content-Type", "application/json")
                    }.build()

                    chain.proceed(newRequest)
                }
                .addInterceptor(HttpLoggingInterceptor()) //very basic HTTP request logging, should be removed by proguard for release build
                //should also add header interceptors, authenticators here for auth requests
                //and tuning in for read and connect timeouts
                .build()
    }

    //using the basic gson factory, but can
    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson) = GsonConverterFactory.create(gson)

    //allows for the use of Single/Maybe/Observable to be used with a retrofit call, or built-in support for coroutines and suspend functions
    //also allows for an async creation, or one with a specific scheduler (if we want our API calls to only happen on a specific thread/pool
    @Provides
    @Singleton
    fun provideRxCallFactory() = RxJava2CallAdapterFactory.create()

    //build up a retrofit service
    @Provides
    @Singleton
    fun provideRetrofit(
            client: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()
    }

    @Provides
    @Singleton
    fun provideBeerAPI(retrofit: Retrofit): BeerAPI = retrofit.create()
}