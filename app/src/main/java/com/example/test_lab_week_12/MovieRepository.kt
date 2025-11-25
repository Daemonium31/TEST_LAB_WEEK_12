package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
// Assuming Movie and MovieService are imported
// In MovieRepository.kt

class MovieRepository (private val movieService: MovieService) {
    private val apiKey = "b2f31b91c07b880e547ad02b39d59dad"

    // Removed LiveData properties:
    // private val movieLiveData = MutableLiveData<List<Movie>>()
    // val movies: LiveData<List<Movie>> get() = movieLiveData
    // private val errorLiveData  MutableLiveData<String>()
    // val error: LiveData<String> get() = errorLiveData

    // fetch movies from the API
    // this function returns a Flow of Movie objects
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // emit the list of popular movies from the API
            emit(movieService.getPopularMovies (apiKey).results)
            // use Dispatchers.IO to run this coroutine on a shared pool of threads
        }.flowOn (Dispatchers.IO)
    }
}