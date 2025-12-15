package com.example.rec.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // 🔹 Whisper (Hugging Face)
    private const val WHISPER_BASE_URL =
        "https://twinmindassignmentbackendtranstwo.onrender.com/"

    // 🔹 Gemini (Render)
    private const val GEMINI_BASE_URL =
        "https://twinmindassignmentbackend.onrender.com/"

    // ---------- Whisper Retrofit ----------
    private val whisperClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val whisperRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WHISPER_BASE_URL)
            .client(whisperClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val transcriptionApi: TranscriptionApi by lazy {
        // Use Render backend for transcription
        geminiRetrofit.create(TranscriptionApi::class.java)
    }

    // ---------- Gemini Retrofit ----------
    private val geminiClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val geminiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .client(geminiClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val summaryApi: SummaryApi by lazy {
        geminiRetrofit.create(SummaryApi::class.java)
    }
}
