package ntnu.adriawh.oving7

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import ntnu.adriawh.oving7.databinding.ActivityMainBinding
import ntnu.adriawh.oving7.managers.FileManager
import ntnu.adriawh.oving7.service.Database
import java.util.ArrayList

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var db: Database
    private lateinit var binding: ActivityMainBinding
    private lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = PreferenceManager.getDefaultSharedPreferences(this)
        preference.registerOnSharedPreferenceChangeListener(this)

        binding.textView.setBackgroundColor(
            Color.parseColor(
                preference.getString("farge", preference.toString())
            )
        )

        val fileManager  = FileManager(this)
        val movies =fileManager.readMoviesFileFromResFolder(R.raw.movies)
        db = Database(this, movies)
        fileManager.writeDatabaseToTxt(db)
    }

    private fun showResults(list: ArrayList<String>) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        binding.textView.text = res
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "Alle filmer")
        menu.add(0, 2, 0, "Alle regissører")
        menu.add(0, 3, 0, "Alle skuespillere")
        menu.add(0, 4, 0, "Alle filmer og regissører")
        menu.add(0, 5, 0, "Filmer av Cristopher Nolan")
        menu.add(0, 6, 0, "Skuespillere i Interstellar")
        menu.add(0, 7, 0, "Skuespillere i Django Unchained")
        menu.add(0, 8, 0, "Regissør av Deadpool")

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent("ntnu.adriawh.oving7.ColorPickerActivity"))
            1             -> showResults(db.allMovies)
            2             -> showResults(db.allDirectors)
            3             -> showResults(db.allActors)
            4             -> showResults(db.allMoviesAndDirectors)
            5             -> showResults(db.getMoviesByDirector("Cristopher Nolan"))
            6             -> showResults(db.getActorsByMovie("Interstellar"))
            7             -> showResults(db.getActorsByMovie("Django Unchained"))
            8             -> showResults(db.getDirectorsByMovie("Deadpool"))
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == getString(R.string.color)){
            val color = sharedPreferences?.getString(key, sharedPreferences.toString())!!
            binding.textView.setBackgroundColor(Color.parseColor(color))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        preference.unregisterOnSharedPreferenceChangeListener(this)
    }
}