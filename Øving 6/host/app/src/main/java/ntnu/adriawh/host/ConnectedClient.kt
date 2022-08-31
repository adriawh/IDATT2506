package ntnu.adriawh.host

import java.net.Socket

data class ConnectedClient(
    var name: String,
    var socket: Socket
)
