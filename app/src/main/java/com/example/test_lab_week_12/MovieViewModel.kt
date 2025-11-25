package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.util.Calendar
import kotlinx.coroutines.flow.map


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
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString() // Import java.util.Calendar

        // Launch a coroutine in viewModelScope
        viewModelScope.launch {
            movieRepository.fetchMovies()
                // Map the List<Movie> to apply filtering and sorting
                .map { popularMovies ->
                    popularMovies
                        // 1. Filter: Movies with a release date in the current year
                        .filter { movie ->
                            movie.releaseDate?.startsWith(currentYear) == true
                        }
                        // 2. Sort: Descending by popularity
                        .sortedByDescending { it.popularity }
                }
                .catch {
                    // catch is a terminal operator that catches exceptions from the Flow
                    _error.value = "An exception occurred: ${it.message}"
                }
                .collect {
                    // collect is a terminal operator that collects the filtered and sorted values
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