package ntnu.adriawh.oving7.managers

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ntnu.adriawh.oving7.data.Movie
import ntnu.adriawh.oving7.service.Database
import java.io.*

class FileManager(private val activity: Activity) {

	private val filename: String = "newFile.txt"
	private var dir: File = activity.filesDir
	private var file: File = File(dir, filename)

	private fun write(str: String) {
		PrintWriter(file).use { writer ->
			writer.println(str)
		}
	}

	fun writeDatabaseToTxt(database: Database){
		Log.d("New file filepath", file.absolutePath)
		Log.d("Filemanager", "writing to file ${file.name}")
		val string = StringBuffer("")
		for(movie in database.allMovies){
			string.append(movie + "\n")
			for(director in database.getDirectorsByMovie(movie)){
				string.append( director + "\n")
			}
			for(actor in database.getActorsByMovie(movie)){
				string.append(actor)
				if(actor != database.getActorsByMovie(movie)[database.getActorsByMovie(movie).size-1]){
					string.append(", ")
				}
			}
			string.append("\n\n")
		}
		write(string.toString())
	}

	fun readMoviesFileFromResFolder(fileId: Int):ArrayList<Movie>{
 		val movies = ArrayList<Movie>()
		try {
			val inputStream: InputStream = activity.resources.openRawResource(fileId)
			BufferedReader(InputStreamReader(inputStream)).use { reader ->
				var title = reader.readLine()
				var director = reader.readLine()
				var actors = reader.readLine().split(", ")
				reader.readLine()
				while(title != null){
					movies.add(Movie(title, director, actors))
					title = reader.readLine()
					if(title != null){
						director = reader.readLine()
						actors = reader.readLine().split(", ")
					}
					reader.readLine()
				}
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return movies
	}
}
