package prod.prog.dataTypes

import java.io.Serializable
import java.net.URL

data class NewsPiece(val link: URL, val text: String) : Serializable
