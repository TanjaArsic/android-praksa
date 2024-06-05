package com.example.thingy.network.di
import android.content.Context
import android.util.Log
import com.example.thingy.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val READ_TIMEOUT = 60L
private const val WRITE_TIMEOUT = 60L
private const val CONNECT_TIMEOUT = 60L

private const val AUTH = "Authorization"
private const val BASE_URL = "https://api.beta.thingy.io/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBearerToken(@ApplicationContext context: Context): String {
        return try {
            context.assets.open("token.txt").bufferedReader().use { it.readText().trim() }
        } catch (e: IOException) {
            Log.e("NetworkModule", "Error reading token", e)
            ""
        }
    }
    @Provides
    @Singleton
    fun provideApiService(token: String): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            println(message)
        }
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header(AUTH, token)
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) //create(GsonBuilder().create()))
            .build()
            .create(ApiService::class.java)
    }

}