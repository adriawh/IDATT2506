package ntnu.adriawh.oving7.service

import android.content.Context
import android.util.Log
import ntnu.adriawh.oving7.data.Movie
import ntnu.adriawh.oving7.managers.DatabaseManager

class Database(context: Context, movies : ArrayList<Movie>?) : DatabaseManager(context) {

    init {

        try {
            this.clear()
            movies?.forEach {
                this.insert(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTOR, arrayOf(ACTOR_NAME))

    val allDirectors: ArrayList<String>
        get() = performQuery(TABLE_DIRECTOR, arrayOf(DIRECTOR_NAME), null)

    val allMovies: ArrayList<String>
        get() = performQuery(TABLE_MOVIE, arrayOf(MOVIE_TITLE), null)


    val allMoviesAndDirectors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIE.$MOVIE_TITLE", "$TABLE_DIRECTOR.$DIRECTOR_NAME")
            val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE, TABLE_DIRECTOR_MOVIE)
            val join = JOIN_DIRECTOR_MOVIE

            return performRawQuery(select, from, join)
        }

    fun getMoviesByDirector(director: String): ArrayList<String> {
        val select = arrayOf("$TABLE_MOVIE.$MOVIE_TITLE")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE, TABLE_DIRECTOR_MOVIE)
        val join = JOIN_DIRECTOR_MOVIE
        val where = "$TABLE_DIRECTOR.$DIRECTOR_NAME='$director'"

        return performRawQuery(select, from, join, where)
    }

    fun getDirectorsByMovie(title: String): ArrayList<String> {
        val select = arrayOf("$TABLE_DIRECTOR.$DIRECTOR_NAME")
        val from = arrayOf(TABLE_DIRECTOR, TABLE_MOVIE, TABLE_DIRECTOR_MOVIE)
        val join = JOIN_DIRECTOR_MOVIE
        val where = "$TABLE_MOVIE.$MOVIE_TITLE='$title'"

        return performRawQuery(select, from, join, where)
    }

    fun getActorsByMovie(title: String): ArrayList<String> {
        val select = arrayOf("$TABLE_ACTOR.$ACTOR_NAME")
        val from = arrayOf(TABLE_ACTOR, TABLE_MOVIE, TABLE_ACTOR_MOVIE)
        val join = JOIN_ACTOR_MOVIE
        val where = "$TABLE_MOVIE.$MOVIE_TITLE='$title'"

        return performRawQuery(select, from, join, where)
    }
}
