package ntnu.adriawh.client

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatClient (
    private val SERVER_IP: String = "10.0.2.2",
    private val SERVER_PORT: Int = 12345
){
    private var textView : TextView? = null
    private var socket : Socket? = null

    /**
     * @param textView textview to show messages in
     */
    fun start(name: String, textView : TextView) {
        this.textView = textView
        Log.i("ChatClient", "Trying to connect")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Socket(SERVER_IP, SERVER_PORT).use { socket: Socket ->
                    sendToHost(name)
                    listenToHost(socket)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Listen for messages from the host
     */
    private fun listenToHost(socket: Socket) {
        this.socket  = socket
        while (true) {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            val name = reader.readLine()
            val message = reader.readLine()

            if (message == null) {
                socket.close()
                break
            }
            addMessage(name, message)
        }
    }

    /**
     * Adds message to feed
     *
     * Copies the previous text, and adds the new message under
     */
    private fun addMessage(name: String, message: String){
        MainScope().launch(){
            val previous = textView?.text
            val new = "$previous \n \n $name \n $message"
            textView?.text = new
        }
    }

    /**
     *  Send message to the connected host
     */

    fun sendMessage(message: String){
        addMessage("Deg", message)
        sendToHost(message)
    }

    private fun sendToHost(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val writer = socket?.getOutputStream()?.let { PrintWriter(it, true) }
            writer?.println(message)
        }
    }
}