package ntnu.adriawh.randomnumber

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.widget.Toast

class RandomNumber : Activity() {

    private var randomNumber = 0
    private var max = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        max = intent.getIntExtra("max", max)
        randomNumber = (0..max).random()

        //Toast.makeText(this, randomNumber.toString(), Toast.LENGTH_SHORT).show()

        setResult(RESULT_OK, Intent().putExtra("randomNumber", randomNumber))
        finish()
    }
}