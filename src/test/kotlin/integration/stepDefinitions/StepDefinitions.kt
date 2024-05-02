package prod.prog.integration.stepDefinitions

import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonMappingException.Reference
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.every
import io.mockk.mockk
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.request.Request
import prod.prog.request.source.database.CompanySource
import prod.prog.request.source.database.NewsPiecesByCompanySource
import prod.prog.request.transformer.LanguageModelTransformer
import prod.prog.service.database.DataBaseService
import prod.prog.service.languageModel.LanguageModelService
import prod.prog.service.supervisor.Supervisor
import prod.prog.service.supervisor.solver.EmptySolver


class StepDefinitions {
    val supervisor = Supervisor(
        before = EmptySolver(),
        after = EmptySolver(),
        initContext = EmptySolver()
    )
    val languageModelStub = object : LanguageModelService {
        override fun summarizeNewsPieceByCompany(company: Company, newsPiece: NewsPiece): NewsSummary =
            NewsSummary(
                company, newsPiece,
                when {
                    !newsPiece.text.contains(company.name) -> "5"
                    newsPiece.text.contains("good") -> "10"
                    newsPiece.text.contains("bad") -> "0"
                    else -> "5"
                }
            )

        override fun name() = "LanguageModelDummy"
    }
    private val dataBase = mockk<DataBaseService>().also {
        every { it.getNewsPiecesByCompany(any<Company>()) } answers { callOriginal() }
        every { it.name() } answers { "DataBaseMock" }
    }

    private var result = mutableMapOf<Company, Double>()

    @Given("database has these companies:")
    fun `database has these companies`(companies: List<Company>) {
        for (company in companies)
            every { dataBase.getCompanyByName(company.name) } returns company
    }

    @Given("database has these news:")
    fun `database has these news`(newsPieces: List<NewsPiece>) {
        for (newsPiece in newsPieces)
            every { dataBase.getNewsPieceByLink(newsPiece.link) } returns newsPiece
    }

    @Given("there are such references:")
    fun `there are such references`(references: List<Map<String, String>>) {
        val grouped = references.groupBy { Company(it["name"]!!) }
            .mapValues { (_, list) ->
                list.map { item -> NewsPiece(item["link"]!!, item["text"]!!) }
            }
        for ((company, newsList) in grouped)
            every { dataBase.getNewsPiecesByCompany(company) } returns newsList
    }

    @When("asked about {companyList}")
    fun `asked about companies`(names: List<String>) {
        for (name in names) {
            val (company, news) =
                Request.basicSourceRequest(
                    CompanySource(dataBase, name).andThenWithPair { company ->
                        NewsPiecesByCompanySource(dataBase, company)
                    }
                ).get(supervisor)

            val summaries = news.map { newsPiece ->
                Request.basicTransformerRequest(
                    LanguageModelTransformer(languageModelStub)
                )(Pair(company, newsPiece))
                    .get(supervisor)
                    .summary
                    .toInt()
            }
            result[company] = summaries.average()
        }
    }

    @Then("{company} should be {compare} {company}")
    fun `company should be better then other`(
        company: Company,
        compare: (Double, Double) -> Boolean,
        otherCompany: Company,
    ) {
        assert(compare(result[company]!!, result[otherCompany]!!))
    }
}
