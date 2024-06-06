package prod.prog.dataTypes.rss

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.source.Source
import prod.prog.service.rss.RssService

class NewsPiecesByCompanyRssSource(
    private val rssService: RssService,
    private val company: Company,
    private val rssNewsLinks: List<RssNewsLink>,
) : Source<List<NewsPiece>>() {
    override fun invoke(t: Unit): List<NewsPiece> =
        rssService.getNewsByCompany(company, rssNewsLinks)

    override fun message(): String =
        "NewsPiecesByCompanyRssSource(Company: $company, links: $rssNewsLinks)"
}