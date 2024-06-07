package prod.prog.utils

fun escapeMarkdownV2(text: String): String {
    val charactersToEscape = listOf("_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!")
    return charactersToEscape.fold(text) { acc, char -> acc.replace(char, "\\$char") }
}