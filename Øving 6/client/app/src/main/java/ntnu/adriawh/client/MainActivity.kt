package ntnu.adriawh.client

import android.app.Activity
import android.os.Bundle
import android.widget.*

class MainActivity : Activity() {

    private var nameSent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chatClient = ChatClient()

        val messages  = findViewById<TextView>(R.id.textView)

        val messageInput = findViewById<EditText>(R.id.messageInput)

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            if(!nameSent){
                chatClient.start(messageInput.text.toString(),messages)
                messages.setText("", TextView.BufferType.EDITABLE)
            }
            nameSent = true
            if(messageInput.text.toString() != ""){
                chatClient.sendMessage(messageInput.text.toString())
                messageInput.setText("", TextView.BufferType.EDITABLE)
            }
        }


    }
}