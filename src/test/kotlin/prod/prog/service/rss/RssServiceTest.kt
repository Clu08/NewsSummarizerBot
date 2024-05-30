package prod.prog.service.rss

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import common.UnitTest
import io.kotest.common.runBlocking
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.every
import io.mockk.mockk
import prod.prog.common.mockedRssItem
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.AvailableRssSources
import prod.prog.service.newsFilter.NewsFilterByTextService

class RssServiceTest : StringSpec({
    tags(UnitTest)
    isolationMode = IsolationMode.InstancePerTest

    val rssParser = mockk<RssParser>()

    val newsFilter = NewsFilterByTextService()
    val rssService = RssServiceImpl(newsFilter, rssParser)

    "fetch news about company from sources" {
        val mockedNewsSource1 = listOf(
            mockedRssItem("link1", "title1", "Yandex bad"),
            mockedRssItem("link2", "title2", "Google good"),
        )

        val rssChannel1 = mockk<RssChannel>()
        every { rssChannel1.items } returns mockedNewsSource1

        val source1 = AvailableRssSources.RBK.rssNewsLink
        every { runBlocking { rssParser.getRssChannel(eq(source1.sourceUrl)) } } returns rssChannel1

        val mockedNewsSource2 = listOf(
            mockedRssItem("link1", "title1", "Yandex good"),
            mockedRssItem("link2", "title2", "Google bad"),
        )

        val rssChannel2 = mockk<RssChannel>()
        every { rssChannel2.items } returns mockedNewsSource2

        val source2 = AvailableRssSources.LENTA.rssNewsLink
        every { runBlocking { rssParser.getRssChannel(eq(source2.sourceUrl)) } } returns rssChannel2

        val rssSources = listOf(source1, source2)
        val yandexCompany = Company(name = "yandex")
        val newsAboutYandex = rssService.getNewsByCompany(yandexCompany, rssSources)

        val expectedNews = listOf(
            NewsPiece(link = "link1", title = "title1", text = "Yandex bad"),
            NewsPiece(link = "link1", title = "title1", text = "Yandex good"),
        )

        newsAboutYandex shouldContainExactlyInAnyOrder expectedNews
    }
})