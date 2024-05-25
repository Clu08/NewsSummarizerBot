package prod.prog.service.newsFilter

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.service.Service

interface NewsFilterService : Service {
    fun isNewsContainsInfoAboutCompany(news: NewsPiece, company: Company): Boolean
}