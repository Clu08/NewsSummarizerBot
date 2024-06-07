package prod.prog.dataTypes

import prod.prog.utils.escapeMarkdownV2
import java.io.Serializable

data class NewsSummary(val company: Company, val newsPiece: NewsPiece, val summary: String) : Serializable {
    override fun toString(): String {
        val styledTitle = escapeMarkdownV2(newsPiece.title)
        val styledLink = escapeMarkdownV2(newsPiece.link)
        val styledSummary = escapeMarkdownV2(summary)
        return "\n*$styledSummary*\n_To get more details follow the [link]($styledLink)_\n"
    }
}
