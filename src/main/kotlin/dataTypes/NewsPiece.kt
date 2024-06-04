package prod.prog.dataTypes

import java.io.Serializable

data class NewsPiece(
    val link: String,
    val title: String,
    val text: String,
) : Serializable
