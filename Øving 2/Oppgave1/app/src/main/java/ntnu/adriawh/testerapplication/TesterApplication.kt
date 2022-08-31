package ntnu.adriawh.testerapplication

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class TesterApplication : Activity() {

    private val requestCode = 1
    private var randomNumber = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val max = 100
        val i = Intent("ntnu.adriawh.RandomNumber")
        i.putExtra("max", max)
        startActivityForResult(i, requestCode)
    }

     public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            Log.e("onActivityResult()", "Noe gikk galt")
            return
        }
        if (requestCode == requestCode) {
            randomNumber = data.getIntExtra("randomNumber", randomNumber)
            val textView = findViewById<TextView>(R.id.textView)
            textView.text = randomNumber.toString()
        }
    }
}