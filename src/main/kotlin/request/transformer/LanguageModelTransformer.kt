package prod.prog.request.transformer

import prod.prog.actionProperties.contextFactory.GptAction
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.service.languageModel.LanguageModelService

class LanguageModelTransformer(private val languageModel: LanguageModelService) :
    Transformer<Pair<Company, NewsPiece>, NewsSummary>() {
    init {
        addContext(GptAction(languageModel.name()))
    }

    override fun invoke(t: Pair<Company, NewsPiece>): NewsSummary =
        languageModel.summarizeNewsPieceByCompany(t.first, t.second)

    override fun message(): String = "LanguageModelTransformer"
}