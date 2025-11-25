package com.example.test_lab_week_12.api
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.test_lab_week_12.model.PopularMoviesResponse
// Assuming PopularMoviesResponse is imported
interface MovieService {
    @GET("movie/popular")
    // here, we are using the suspend keyword to indicate that this function is a coroutine [cite: 39]
    suspend fun getPopularMovies (
        @Query("api_key") apiKey: String
    ): PopularMoviesResponse
}