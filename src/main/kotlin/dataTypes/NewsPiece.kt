package prod.prog.dataTypes

import java.io.Serializable

data class NewsPiece(val link: String, val text: String) : Serializable {
    override fun toString(): String {
        return "\n$text\nTo get more details follow the link: $link\n"
    }
}
