package prod.prog.service.rss

import common.UnitTest
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.AvailableRssSources
import prod.prog.service.newsFilter.NewsFilterByTextService
import javax.xml.parsers.DocumentBuilder

class RssServiceTest : StringSpec({
    tags(UnitTest)
    isolationMode = IsolationMode.InstancePerTest

    val newsFilter = NewsFilterByTextService()
    val documentBuilder = mockk<DocumentBuilder>()
    val rssService = spyk(RssServiceImpl(newsFilter, documentBuilder), recordPrivateCalls = true)

    "fetch news about company from sources" {

        val source1 = AvailableRssSources.LENTA.rssNewsLink
        every { rssService invoke "fetchNewsFromRssSource" withArguments listOf(source1) } returns listOf(
            NewsPiece("link1", "title1", "Yandex bad"),
            NewsPiece("link2", "title2", "Google good"),
        )

        val source2 = AvailableRssSources.RBK.rssNewsLink
        every { rssService invoke "fetchNewsFromRssSource" withArguments listOf(source2) } returns listOf(
            NewsPiece("link1", "title1", "Yandex good"),
            NewsPiece("link2", "title2", "Google bad"),
        )

        val rssSources = listOf(source1, source2)
        val yandexCompany = Company(name = "yandex")
        val newsAboutYandex = rssService.getNewsByCompany(listOf(yandexCompany), rssSources)

        val expectedNews = listOf(
            Pair(yandexCompany, NewsPiece(link = "link1", title = "title1", text = "Yandex good")),
            Pair(yandexCompany, NewsPiece(link = "link1", title = "title1", text = "Yandex bad")),
        )
        newsAboutYandex shouldContainExactlyInAnyOrder expectedNews
    }
})