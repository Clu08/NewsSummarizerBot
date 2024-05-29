package prod.prog.service.manager
data class Config(val buttons: List<ButtonEnum>)

object ConfigLoader {
    fun loadConfig(): Config {
        return Config(
            buttons = ButtonEnum.entries
        )
    }
}