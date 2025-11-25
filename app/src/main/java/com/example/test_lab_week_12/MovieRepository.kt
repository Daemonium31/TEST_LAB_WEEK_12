package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
// Assuming Movie and MovieService are imported
class MovieRepository (private val movieService: MovieService) {
    private val apiKey = "b2f31b91c07b880e547ad02b39d59dad" // <-- REPLACE THIS WITH YOUR API KEY

    // LiveData that contains a List of movies [cite: 51]
    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    // LiveData that contains an error message [cite: 54]
    private val errorLiveData  = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    // fetch movies from the API [cite: 61]
    suspend fun fetchMovies() {
        try {
            // get the List of popular movies from the API [cite: 63]
            val popularMovies = movieService.getPopularMovies (apiKey)
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            // if an error occurs, post the error message to the errorLiveData [cite: 65, 66]
            errorLiveData.postValue(
                "An error occurred: ${exception.message}")
        }
    }
}