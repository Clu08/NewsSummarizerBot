package prod.prog.service.manager

enum class ButtonEnum(val displayName: String) {
    COMPANY_1("Google"),
    COMPANY_2("Yandex"),
    COMPANY_3("Microsoft"),
    COMPANY_4("Apple"),
    COMPANY_5("NVIDIA");

    companion object {
        fun fromDisplayName(displayName: String): ButtonEnum? {
            return entries.find { it.displayName == displayName }
        }
    }
}