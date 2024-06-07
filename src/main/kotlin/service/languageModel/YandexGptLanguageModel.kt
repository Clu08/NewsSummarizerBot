package prod.prog.service.languageModel

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.cdimascio.dotenv.dotenv
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.NewsPiece
import prod.prog.dataTypes.NewsSummary
import java.io.IOException

class YandexGptLanguageModel : LanguageModelService {
    private val folder = dotenv()["YANDEX_FOLDER"]
    private val apiKey = dotenv()["YANDEX_API_KEY"]

    override fun summarizeNewsPieceByCompany(company: Company, newsPiece: NewsPiece): NewsSummary {
        val prompt = JsonObject().apply {
            addProperty("modelUri", "gpt://$folder/yandexgpt-lite")
            add("completionOptions", JsonObject().apply {
                addProperty("stream", false)
                addProperty("temperature", 0.5)
                addProperty("maxTokens", "2000")
            })
            add("messages", JsonArray().apply {
                add(JsonObject().apply {
                    addProperty("role", "user")
                    addProperty(
                        "text",
                        """"Вот название новости: "${newsPiece.title}".
                            И её краткое описание: "${newsPiece.text}".
                            Может ли данная новость иметь положительное или отрицательное влияение инвестиционное положение компании "${company.name}"? 
                            Основывайся исключительно на этой новости. Напиши первым словом одно число от -10 (сильно изменилось в худшую) до +10 (сильно изменилось в лучшую). 
                            После напиши короткое объяснение своего решения. Новость может быть малозначительной, но всё равно сформируй мнение о ней, не используя других данных о компании и рынке."""
                    )
                })
            })
        }

        val url = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion"
        val headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .add("Authorization", "Api-Key $apiKey")
            .build()

        val client = OkHttpClient()
        val requestBody = prompt.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .post(requestBody)
            .build()

        val jsonResponse = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            response.body?.string() ?: throw IOException("Empty response body")
        }

        val summary = getTextFromJsonResponse(jsonResponse)

        return NewsSummary(company, newsPiece, summary)
    }

    private fun getTextFromJsonResponse(jsonResponse: String): String {
        val jsonObject = JsonParser.parseString(jsonResponse).asJsonObject
        val summary = jsonObject.getAsJsonObject("result")
        val alternatives = summary.getAsJsonArray("alternatives")
        val firstAlternative = alternatives.first().asJsonObject
        val message = firstAlternative.getAsJsonObject("message")
        return message.getAsJsonPrimitive("text").asString
    }

    override fun name() = "YandexGptLanguageModel"
}