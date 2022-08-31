package ntnu.adriawh.oving4

import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ntnu.adriawh.oving4.R

class InformationFragment : Fragment() {

    private var movies: Array<String> = arrayOf()
    private var movieDescriptions: Array<String> = arrayOf()
    private var movieImages : TypedArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movies = resources.getStringArray(R.array.movies)
        movieDescriptions = resources.getStringArray(R.array.movieDescriptions)
        movieImages = resources.obtainTypedArray(R.array.movieImages)
    }

    fun setMovie(position: Int){
        requireView().findViewById<TextView>(R.id.title).text = movies[position]
        requireView().findViewById<ImageView>(R.id.image).setImageDrawable(movieImages?.getDrawable(position))
        requireView().findViewById<TextView>(R.id.description).text = movieDescriptions[position]
    }

}