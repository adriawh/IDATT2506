package ntnu.adriawh.host

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
import java.net.ServerSocket
import java.net.Socket
import java.util.HashMap

class ChatHost() {

    private val PORT = 12345
    private var textView : TextView? = null

    private val connectedClients : HashMap<Socket, ConnectedClient> = HashMap()

    /**
     * @param textView textview to show messages in
     */
    fun start(textView: TextView ) {
        this.textView = textView

        Log.i("ChatHost", "Starting up the host...")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerSocket(PORT).use { serverSocket: ServerSocket ->
                    while (true) {
                        val socket = serverSocket.accept()
                        newClient(socket)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun newClient(clientSocket: Socket){

        //Remove the waiter-text when the first connection is made
        if(connectedClients.size == 0){
            textView?.text = null
        }

        Log.i("ChatHost", "New client connected")
        CoroutineScope(Dispatchers.IO).launch {

            //read name of the client
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val clientName = reader.readLine()
            Log.i("ChatHost", "Client joined with name: $clientName")

            val newClient = ConnectedClient(clientName, clientSocket)
            connectedClients[clientSocket] = newClient

            //Send welcome message to all existing clients and new
            sendToAllClients( "Velkommen $clientName!", null)
            sendToClient(clientSocket, "Host")
            sendToClient(clientSocket, "Velkommen $clientName!")

            listenToClient(clientSocket)
        }
    }

    /**
     * Coroutine for listening for messages from the different connections
     */
    private fun listenToClient(socket: Socket) {
        while (true) {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val message = reader.readLine()
            connectedClients[socket]?.name?.let { addMessage(it, message) }
            sendToAllClients(message, socket)
            if (message == null) {
                connectedClients.remove(socket)
                socket.close()
                break
            }
        }
    }

    /**
     * Adding message to feed
     */
    private fun addMessage(name: String, message: String){
        MainScope().launch(){
            val previous = textView?.text
            val new = "$previous \n \n $name \n $message"
            textView?.text = new
        }
    }

    /**
     * Sends a message to the given socket
     */
    private fun sendToClient(socket: Socket, message: String) {
        val writer = PrintWriter(socket.getOutputStream(), true)
        writer.println(message)
    }

    /**
     * Sends a message to all connected clients, if no owner is set the
     * host sends it as its own message
     */
    fun sendToAllClients(message: String , owner: Socket?) {
        Log.i("ChatHost", "Sending message to all chat members")

        if(owner == null){
            addMessage("Deg", message)
        }
        CoroutineScope(Dispatchers.IO).launch {
            connectedClients.forEach { connectedClient ->
                if (connectedClient.key != owner) {
                    if (owner != null) {
                        connectedClients[owner]?.name?.let { sendToClient(connectedClient.key, it) }
                    } else {
                        sendToClient(connectedClient.key, "Host")
                    }
                    sendToClient(connectedClient.value.socket, message)
                }

            }
        }
    }

}