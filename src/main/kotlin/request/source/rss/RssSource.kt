package prod.prog.request.source.rss

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.source.Source
import prod.prog.service.rss.RssService

class RssSource(val company : Company, val service : RssService) : Source<List<NewsPiece>>() {
    override fun getSource(): List<NewsPiece> = service.getNewsByCompany(company)

    override fun message() = "Rss source got"

}