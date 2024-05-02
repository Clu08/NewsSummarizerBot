package prod.prog.service.languageModel

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.Service

interface LanguageModelService : Service {
//    todo functions to work with language model

    fun summarizeNewsPieceByCompany(company: Company, newsPiece: NewsPiece): NewsSummary
}