package integration.stepDefinitions

import common.randomId
import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import kotlin.math.abs


class CucumberDataTransformers {
    @ParameterType(".+")
    fun company(string: String) = Company(string)

    @ParameterType(".*")
    fun companyList(string: String): List<String> =
        string.split(" and ").map { it.trim() }

    @DataTableType
    fun companyTransformer(entry: Map<String?, String?>) = Company(entry["name"]!!)

    @DataTableType
    fun newsPieceTransformer(entry: Map<String?, String?>) =
        NewsPiece("news/${randomId()}", entry["text"]!!)

    @ParameterType("better then|same as|worse then")
    fun compare(string: String): (Double, Double) -> Boolean =
        when (string) {
            "better then" -> { x, y -> x > y }
            "worse then" -> { x, y -> x < y }
            "same as" -> { x, y -> abs(x - y) < 1 }
            else -> throw IllegalArgumentException()
        }
}
