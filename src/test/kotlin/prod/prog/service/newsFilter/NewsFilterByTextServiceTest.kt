package prod.prog.service.newsFilter

import common.UnitTest
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece

class NewsFilterByTextServiceTest : StringSpec({
    tags(UnitTest)
    isolationMode = IsolationMode.InstancePerTest

    val newsFilter = NewsFilterByTextService()

    "no info about company" {
        val news = NewsPiece(link = "link", title = "title", text = "text", categories = listOf("category"))
        val company = Company(name = "yandex")
        val filterResult = newsFilter.isNewsContainsInfoAboutCompany(news, company)

        filterResult shouldBe false
    }

    "info about company in title" {
        val news = NewsPiece(link = "link", title = "wow yAnDeX", text = "text", categories = listOf("category"))
        val company = Company(name = "yandex")
        val filterResult = newsFilter.isNewsContainsInfoAboutCompany(news, company)

        filterResult shouldBe true
    }

    "info about company in text" {
        val news = NewsPiece(link = "link", title = "title", text = "oh yyAndexx", categories = listOf("category"))
        val company = Company(name = "yandex")
        val filterResult = newsFilter.isNewsContainsInfoAboutCompany(news, company)

        filterResult shouldBe true
    }

    "info about company in category" {
        val news = NewsPiece(link = "link", title = "title", text = "text", categories = listOf("yandex"))
        val company = Company(name = "yandex")
        val filterResult = newsFilter.isNewsContainsInfoAboutCompany(news, company)

        filterResult shouldBe true
    }
})