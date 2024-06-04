package prod.prog.service.newsFilter

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece

class NewsFilterByTextService : NewsFilterService {
    override fun isNewsContainsInfoAboutCompany(news: NewsPiece, company: Company): Boolean {
        return textContainsDataAboutCompany(news.title, company) ||
                textContainsDataAboutCompany(news.text, company)
    }

    /**
     * Simply looking if [text] contains substring equal to [company] name
     */
    private fun textContainsDataAboutCompany(text: String, company: Company): Boolean =
        company.name.lowercase() in text.lowercase()

    override fun name() = "NewsFilterServiceImpl"
}