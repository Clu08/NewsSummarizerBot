package prod.prog.service.languageModel

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary

class IdLanguageModel : LanguageModelService {
    override fun summarizeNewsPieceByCompany(company: Company, newsPiece: NewsPiece): NewsSummary {
        println("IdLanguageModel ${NewsSummary(company, newsPiece, "${company.name}: ${newsPiece.title}")}")
        return NewsSummary(company, newsPiece, "${company.name}: ${newsPiece.title}")
    }

    override fun name() = "IdLanguageModel"
}