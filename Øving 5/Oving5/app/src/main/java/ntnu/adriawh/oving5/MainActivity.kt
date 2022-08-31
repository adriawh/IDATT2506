package ntnu.adriawh.oving5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ViewSwitcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var http = HttpWrapper("https://bigdata.idi.ntnu.no/mobil/tallspill.jsp")

    private var playCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switcher = findViewById<ViewSwitcher>(R.id.switcher)
        val header = findViewById<TextView>(R.id.header)

        val fromServer = findViewById<TextView>(R.id.textFromServer)

        findViewById<Button>(R.id.submitButton).setOnClickListener {


            Log.d("MainActivity", "SubmitButton clicked")
            val name = findViewById<EditText>(R.id.nameInput).text.toString()
            val card = findViewById<EditText>(R.id.cardInput).text.toString()

            val data = HashMap<String, String>()
            data["navn"] = name
            data["kortnummer"] = card

            CoroutineScope(Dispatchers.IO).launch {
                val response = http.get(data)
                MainScope().launch {
                    fromServer.text = response
                }
            }

            header.setText(R.string.playHeader)
            switcher.showNext()
        }

        findViewById<Button>(R.id.playButton).setOnClickListener {
            Log.d("MainActivity", "PlayButton clicked")

            playCount++
            if(playCount == 3){
                Log.d("MainActivity", "Reached 3 entries")

                it.visibility = View.GONE
                findViewById<Button>(R.id.restartButton).visibility = View.VISIBLE
            }
            val number = findViewById<EditText>(R.id.gameInput).text.toString()

            val data = HashMap<String, String>()
            data["tall"] = number

            CoroutineScope(Dispatchers.IO).launch {
                    val response = http.get(data)
                if(response.contains("vunnet")){
                    Log.d("MainActivity", "User won")

                    MainScope().launch {
                        it.visibility = View.GONE
                        findViewById<Button>(R.id.restartButton).visibility = View.VISIBLE                    }
                }
                MainScope().launch {
                    fromServer.text = response
                }
            }
        }
        findViewById<Button>(R.id.restartButton).setOnClickListener {

            Log.d("MainActivity", "RestartButton clicked")

            findViewById<EditText>(R.id.nameInput).setText("", TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.cardInput).setText("", TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.gameInput).setText("", TextView.BufferType.EDITABLE)
            recreate()
        }
    }



}