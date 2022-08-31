package ntnu.adriawh.host

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chatHost = ChatHost()

        chatHost.start(findViewById(R.id.textView))

        val messageInput = findViewById<EditText>(R.id.messageInput)

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            chatHost.sendToAllClients(messageInput.text.toString(), null)
            messageInput.setText("", TextView.BufferType.EDITABLE)
        }
    }
}