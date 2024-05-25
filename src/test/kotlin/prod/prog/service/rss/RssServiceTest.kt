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
import prod.prog.dataTypes.RssSource
import prod.prog.service.newsFilter.NewsFilterByTextService

class RssServiceTest : StringSpec({
    tags(UnitTest)
    isolationMode = IsolationMode.InstancePerTest

    val rssParser = mockk<RssParser>()

    val newsFilter = NewsFilterByTextService()
    val rssService = RssServiceImpl(newsFilter, rssParser)

    "fetch news from rss channel successfully" {
        val mockedNews = listOf(
            mockedRssItem("link1", "title1", "Yandex"),
            mockedRssItem("link2", "title2", "Google"),
        )

        val rssChannel = mockk<RssChannel>()
        every { rssChannel.items } returns mockedNews

        every { runBlocking { rssParser.getRssChannel(any()) } } returns rssChannel

        val rssSource = RssSource("someRssChannelUrl")
        val news = rssService.fetchNewsFromRssSource(rssSource)

        val expectedNews = listOf(
            NewsPiece(link = "link1", title = "title1", text = "Yandex"),
            NewsPiece(link = "link2", title = "title2", text = "Google"),
        )

        news shouldContainExactlyInAnyOrder expectedNews

    }

    "fetch news about company from sources" {
        val mockedNewsSource1 = listOf(
            mockedRssItem("link1", "title1", "Yandex bad"),
            mockedRssItem("link2", "title2", "Google good"),
        )

        val rssChannel1 = mockk<RssChannel>()
        every { rssChannel1.items } returns mockedNewsSource1
        every { runBlocking { rssParser.getRssChannel(eq("source1")) } } returns rssChannel1

        val mockedNewsSource2 = listOf(
            mockedRssItem("link1", "title1", "Yandex good"),
            mockedRssItem("link2", "title2", "Google bad"),
        )

        val rssChannel2 = mockk<RssChannel>()
        every { rssChannel2.items } returns mockedNewsSource2
        every { runBlocking { rssParser.getRssChannel(eq("source2")) } } returns rssChannel2

        val rssSources = listOf(RssSource("source1"), RssSource("source2"))
        val yandexCompany = Company(name = "yandex")
        val newsAboutYandex = rssService.getNewsByCompany(yandexCompany, rssSources)

        val expectedNews = listOf(
            NewsPiece(link = "link1", title = "title1", text = "Yandex bad"),
            NewsPiece(link = "link1", title = "title1", text = "Yandex good"),
        )

        newsAboutYandex shouldContainExactlyInAnyOrder expectedNews
    }
})