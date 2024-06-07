package prod.prog.dataTypes

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

    fun escapeMarkdownV2(text: String): String {
        val charactersToEscape = listOf("_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!")
        return charactersToEscape.fold(text) { acc, char -> acc.replace(char, "\\$char") }
    }
}
