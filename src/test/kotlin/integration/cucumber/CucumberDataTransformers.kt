package prod.prog.integration.cucumber

import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType
import prod.prog.common.randomId
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.rss.AvailableRssSources
import kotlin.math.abs


class CucumberDataTransformers {
    @ParameterType(".+")
    fun company(string: String) = Company(string)

    @ParameterType(".+")
    fun rssLink(string: String) = getRssNewsLink(string)

    @ParameterType(".*")
    fun companyList(string: String): List<String> =
        string.split(" and ").map { it.trim() }

    @ParameterType(".*")
    fun rssLinkList(string: String) = string
        .split(" and ")
        .map { it.trim() }
        .map { getRssNewsLink(it) }

    @DataTableType
    fun companyTransformer(entry: Map<String?, String?>) = Company(entry["name"]!!)

    @DataTableType
    fun newsPieceTransformer(entry: Map<String?, String?>) =
        NewsPiece("news/${randomId()}", entry["title"]!!, entry["text"]!!)

    @ParameterType("better then|same as|worse then")
    fun compare(string: String): (Double, Double) -> Boolean =
        when (string) {
            "better then" -> { x, y -> x > y }
            "worse then" -> { x, y -> x < y }
            "same as" -> { x, y -> abs(x - y) < 1 }
            else -> throw IllegalArgumentException()
        }

    private fun getRssNewsLink(string: String) = AvailableRssSources.valueOf(string.uppercase()).rssNewsLink
}
