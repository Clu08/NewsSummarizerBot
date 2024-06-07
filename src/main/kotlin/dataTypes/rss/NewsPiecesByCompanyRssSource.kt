package prod.prog.dataTypes.rss

import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.request.transformer.Transformer
import prod.prog.service.rss.RssService

class NewsPiecesByCompanyRssSource(
    private val rssService: RssService,
) : Transformer<Pair<List<Company>, List<RssNewsLink>>, List<Pair<Company, NewsPiece>>>() {
    override fun invoke(t: Pair<List<Company>, List<RssNewsLink>>): List<Pair<Company, NewsPiece>> =
        rssService.getNewsByCompany(t.first, t.second)

    override fun message(): String =
        "NewsPiecesByCompanyRssSource"
}