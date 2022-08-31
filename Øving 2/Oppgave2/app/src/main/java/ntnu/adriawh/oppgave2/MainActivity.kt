package ntnu.adriawh.oppgave2

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private var numberOne = 0
    private var numberTwo = 0
    private var answer = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.additionButton).setOnClickListener {
            getValues()
            if(numberOne + numberTwo == answer){
                Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
            }else{
                val message = StringBuilder().append(getString(R.string.wrong) + " ")
                message.append(numberOne + numberTwo)
                Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
            }
            updateValues()
        }

        findViewById<Button>(R.id.multiplicationButton).setOnClickListener {
            getValues()
            if(numberOne * numberTwo == answer){
                Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
            }else{
                val message = StringBuilder().append(getString(R.string.wrong) + " ")
                message.append(numberOne * numberTwo)
                Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
            }

            updateValues()
        }

    }

    private fun getValues(){
        numberOne = findViewById<TextView>(R.id.numberOne).text.toString().toInt()
        numberTwo = findViewById<TextView>(R.id.numberTwo).text.toString().toInt()
        answer = findViewById<EditText>(R.id.answer).text.toString().toInt()
    }

    private fun updateValues(){
        val max = findViewById<EditText>(R.id.topLimit).text.toString().toInt()
        val i = Intent("ntnu.adriawh.RandomNumber")
        i.putExtra("max", max)

        startActivityForResult(i, 1)
        startActivityForResult(i, 2)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) {
            Log.e("onActivityResult()", "Noe gikk galt")
            return
        }
        if (requestCode == 1) {
            numberOne = data.getIntExtra("randomNumber", numberOne)
            val textView = findViewById<TextView>(R.id.numberOne)
            textView.text = numberOne.toString()
        }else if(requestCode == 2){
            numberTwo = data.getIntExtra("randomNumber", numberTwo)
            val textView = findViewById<TextView>(R.id.numberTwo)
            textView.text = numberTwo.toString()

        }
    }
}