package prod.prog.service.database

import common.UnitTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary

class DatabaseServiceTest : StringSpec({
    tags(UnitTest)

    val company1 = Company("Company1")
    val company2 = Company("Company2")
    val company3 = Company("Company3")
    val newsPiece1 = NewsPiece("link1", "news about Company1", "Company1 is good")
    val newsPiece2 = NewsPiece("link2", "news about Company1 and Company3", "Company1 and Company3 are bad")
    val newsSummary1 = NewsSummary(company1, newsPiece1, "10")
    val newsSummary2 = NewsSummary(company1, newsPiece2, "3")
    val newsSummary3 = NewsSummary(company3, newsPiece2, "3")

    val database = DatabaseService(DatabaseImpl(DatabaseURL.IN_MEMORY))
    database.addCompany(company1.name)
    database.addCompany(company2.name)
    database.addCompany(company3.name)
    database.addNewsPiece(newsPiece1.link, newsPiece1.title, newsPiece1.text)
    database.addNewsPiece(newsPiece2.link, newsPiece2.title, newsPiece2.text)
    database.addNewsSummary(newsSummary1.company, newsSummary1.newsPiece, newsSummary1.summary)
    database.addNewsSummary(newsSummary2.company, newsSummary2.newsPiece, newsSummary2.summary)
    database.addNewsSummary(newsSummary3.company, newsSummary3.newsPiece, newsSummary3.summary)

    "getCompanyByName" {
        database.getCompanyByName(company1.name) shouldBe company1
        database.getCompanyByName(company2.name) shouldBe company2
        database.getCompanyByName(company3.name) shouldBe company3
    }

    "getNewsPieceByLink" {
        database.getNewsPieceByLink(newsPiece1.link) shouldBe newsPiece1
        database.getNewsPieceByLink(newsPiece2.link) shouldBe newsPiece2
    }

    "getNewsSummariesByCompany" {
        database.getNewsSummariesByCompany(company1) shouldContainAll listOf(newsSummary1, newsSummary2)
        database.getNewsSummariesByCompany(company2) shouldContainAll listOf()
        database.getNewsSummariesByCompany(company3) shouldContainAll listOf(newsSummary3)
    }

    "getNewsSummariesByNewsPiece" {
        database.getNewsSummariesByNewsPiece(newsPiece1) shouldContainAll listOf(newsSummary1)
        database.getNewsSummariesByNewsPiece(newsPiece2) shouldContainAll listOf(newsSummary2, newsSummary3)
    }

    "getNewsPiecesByCompany" {
        database.getNewsPiecesByCompany(company1) shouldContainAll listOf(newsPiece1, newsPiece2)
        database.getNewsPiecesByCompany(company2) shouldContainAll listOf()
        database.getNewsPiecesByCompany(company3) shouldContainAll listOf(newsPiece2)
    }
})
