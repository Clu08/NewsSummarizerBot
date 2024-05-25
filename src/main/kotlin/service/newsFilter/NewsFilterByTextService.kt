package prod.prog.service.newsFilter

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import java.util.*

class NewsFilterByTextService : NewsFilterService {
    override fun isNewsContainsInfoAboutCompany(news: NewsPiece, company: Company): Boolean {
        return news.categories.any { category -> textContainsDataAboutCompany(category, company) } ||
                textContainsDataAboutCompany(news.title, company) ||
                textContainsDataAboutCompany(news.text, company)
    }

    private fun textContainsDataAboutCompany(text: String, company: Company): Boolean =
        text.lowercase(Locale.getDefault()).contains(company.name)

    override fun name() = "NewsFilterServiceImpl"
}