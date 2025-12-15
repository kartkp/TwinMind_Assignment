package com.example.rec.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {

    // 🔹 Whisper (Hugging Face)
    private const val WHISPER_BASE_URL =
        "https://prakharsingh0-wishpermodel.hf.space/"

    // 🔹 Gemini (Render)
    private const val GEMINI_BASE_URL =
        "https://twinmind-backend-8kly.onrender.com/"

    // ---------- Whisper Retrofit ----------
    private val whisperRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WHISPER_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val transcriptionApi: TranscriptionApi by lazy {
        whisperRetrofit.create(TranscriptionApi::class.java)
    }

    // ---------- Gemini Retrofit ----------
    private val geminiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON only
            .build()
    }

    val summaryApi: SummaryApi by lazy {
        geminiRetrofit.create(SummaryApi::class.java)
    }
}
