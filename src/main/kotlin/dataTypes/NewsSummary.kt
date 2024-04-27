package prod.prog.dataTypes

import java.io.Serializable

data class NewsSummary(val company: Company, val newsPiece: NewsPiece, val summary: String) : Serializable
