package prod.prog.integration.cucumber

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.every
import io.mockk.mockk
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import prod.prog.request.Request
import prod.prog.request.transformer.IdTransformer
import prod.prog.request.transformer.database.NewsPiecesByCompanyDB
import prod.prog.request.transformer.LanguageModelTransformer
import prod.prog.request.transformer.Transformer
import prod.prog.request.transformer.Transformer.Companion.forEach
import prod.prog.request.transformer.database.CompanyByNameDB
import prod.prog.request.transformer.database.NewsSummariesByCompanyDB
import prod.prog.service.database.DatabaseService
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
                company, newsPiece, when {
                    !newsPiece.text.contains(company.name) -> "5"
                    newsPiece.text.contains("good") -> "10"
                    newsPiece.text.contains("bad") -> "0"
                    else -> "5"
                }
            )

        override fun name() = "LanguageModelDummy"
    }
    private val database = mockk<DatabaseService>().also {
        every { it.getNewsPiecesByCompany(any<Company>()) } answers { callOriginal() }
        every { it.name() } answers { "DatabaseMock" }
    }

    private var result = mutableMapOf<String, Double>()

    @Given("database has these companies:")
    fun `database has these companies`(companies: List<Company>) {
        for (company in companies)
            every { database.getCompanyByName(company.name) } returns company
    }

    @Given("database has these news:")
    fun `database has these news`(newsPieces: List<NewsPiece>) {
        for (newsPiece in newsPieces)
            every { database.getNewsPieceByLink(newsPiece.link) } returns newsPiece
    }

    @Given("there are such references:")
    fun `there are such references`(references: List<Map<String, String>>) {
        val groupedNewsPieces = references.groupBy { Company(it["name"]!!) }
            .mapValues { (_, list) ->
                list.map { item -> NewsPiece(item["link"]!!, item["title"]!!, item["text"]!!) }
            }
        for ((company, newsList) in groupedNewsPieces)
            every { database.getNewsPiecesByCompany(company) } returns newsList

        val groupedSummaries =
            Request.basicTransformerRequest(
                LanguageModelTransformer(languageModelStub).forEach()
            )(references.map { item ->
                Pair(
                    Company(item["name"]!!),
                    NewsPiece(item["link"]!!, item["title"]!!, item["text"]!!, emptyList())
                )
            })
                .get(supervisor)
                .groupBy { it.company }

        for ((company, newsList) in groupedSummaries)
            every { database.getNewsSummariesByCompany(company) } returns newsList
    }

    @When("asked about {companyList}")
    fun `asked about companies`(names: List<String>) {
        for (name in names) {
            val summaries =
                Request.basicTransformerRequest(
                    CompanyByNameDB(database)
                        .andThen(NewsSummariesByCompanyDB(database))
                )(name).get(supervisor)
            result[name] = summaries.map { it.summary.toDouble() }.average()
        }
    }

    @Then("{company} should be {compare} {company}")
    fun `company should be better then other`(
        company: Company,
        compare: (Double, Double) -> Boolean,
        otherCompany: Company,
    ) {
        assert(compare(result[company.name]!!, result[otherCompany.name]!!))
    }
}
