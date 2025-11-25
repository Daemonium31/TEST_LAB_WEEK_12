package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope // For launching coroutines in ViewModel scope
import kotlinx.coroutines.Dispatchers // For specifying coroutine dispatcher
import kotlinx.coroutines.launch // For launching a coroutine
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
// Assuming Movie and MovieRepository are imported

// In MovieViewModel.kt

class MovieViewModel (private val movieRepository: MovieRepository)
    : ViewModel() {
    init {
        fetchPopularMovies()
    }

    // define the StateFlow in replace of the LiveData
    // a StateFlow is an observable Flow that emits state updates to the collectors
    // MutableStateFlow is a StateFlow that you can change the value
    private val _popularMovies = MutableStateFlow(
        emptyList<Movie>()
    )
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    // fetch movies from the API
    private fun fetchPopularMovies() {
        // Launch a coroutine in viewModelScope
        viewModelScope.launch {
            movieRepository.fetchMovies()
                .catch {
                    // catch is a terminal operator that catches exceptions from the Flow
                    _error.value = "An exception occurred: ${it.message}"
                }
                .collect {
                    // collect is a terminal operator that collects the values from the Flow
                    // the results are emitted to the StateFlow
                    _popularMovies.value = it
                }
        }
    }

    // Removed old LiveData properties and function:
    // val popularMovies: LiveData<List<Movie>> get() = movieRepository.movies
    // val error: LiveData<String> get() = movieRepository.error
    // private fun fetchPopularMovies() { ... }
}