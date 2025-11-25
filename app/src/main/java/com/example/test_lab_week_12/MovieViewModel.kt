package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope // For launching coroutines in ViewModel scope
import kotlinx.coroutines.Dispatchers // For specifying coroutine dispatcher
import kotlinx.coroutines.launch // For launching a coroutine
import com.example.test_lab_week_12.model.Movie
// Assuming Movie and MovieRepository are imported

class MovieViewModel (private val movieRepository: MovieRepository)
    : ViewModel() {
    init {
        fetchPopularMovies()
    }

    // define the LiveData [cite: 103]
    val popularMovies: LiveData<List<Movie>>
        get() = movieRepository.movies
    val error: LiveData<String>
        get() = movieRepository.error

    // fetch movies from the API [cite: 108]
    private fun fetchPopularMovies() {
        // Launch a coroutine in viewModelScope [cite: 110]
        // Dispatchers.IO means that this coroutine will run on a shared pool of threads [cite: 113, 114]
        viewModelScope.launch (Dispatchers.IO) {
            movieRepository.fetchMovies()
        }
    }
}