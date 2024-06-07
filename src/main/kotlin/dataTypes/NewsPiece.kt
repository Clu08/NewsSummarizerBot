package prod.prog.dataTypes

import prod.prog.utils.escapeMarkdownV2
import java.io.Serializable

data class NewsPiece(
    val link: String,
    val title: String,
    val text: String,
    val categories: List<String> = listOf(),
) : Serializable
{
    override fun toString(): String {
        val styledTitle = escapeMarkdownV2(title)
        val styledLink = escapeMarkdownV2(link)
        return "\n*$styledTitle*\n_To get more details follow the [link]($styledLink)_\n"
    }
}
