package com.example.test_lab_week_12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.example.test_lab_week_12.model.Movie
import android.content.Intent



// Assuming MovieAdapter, MovieApplication, MovieViewModel, and R.id/R.layout are imported
class MainActivity : AppCompatActivity() {
    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    // In MainActivity.kt, inside onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel  = ViewModelProvider(
            this, object: ViewModelProvider.Factory {
                override fun <T: ViewModel> create (modelClass: Class<T>): T{
                    return MovieViewModel (movieRepository) as T
                }
            })[MovieViewModel::class.java]

        // New Flow collection:
        lifecycleScope.launch {
            repeatOnLifecycle (Lifecycle.State.STARTED) {
                launch {
                    // collect the list of movies from the StateFlow
                    movieViewModel.popularMovies.collect { movies ->
                        // add the List of movies to the adapter
                        movieAdapter.addMovies (movies)
                    }
                }
                launch {
                    // collect the error message from the StateFlow
                    movieViewModel.error.collect { error ->
                        // if an error occurs, show a Snackbar with the error message
                        if (error.isNotEmpty()) Snackbar
                            .make(
                                recyclerView, error, Snackbar.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }
    }
    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}