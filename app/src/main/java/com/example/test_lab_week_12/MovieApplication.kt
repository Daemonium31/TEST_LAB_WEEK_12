package com.example.test_lab_week_12

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory // Assuming you use Moshi
import com.example.test_lab_week_12.api.MovieService
// Assuming MovieService and MovieRepository are imported

class MovieApplication: Application() {
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        // create a Retrofit instance [cite: 77]
        val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory (MoshiConverterFactory.create())
        .build()

        // create a MovieService instance and bind the MovieService interface to Retrofit [cite: 82, 83]
        val movieService = retrofit.create(
        MovieService::class.java
        )
        // create a MovieRepository instance [cite: 90]
        movieRepository = MovieRepository (movieService)
    }
}