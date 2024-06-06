package prod.prog.service.database

enum class DatabaseURL(val databaseURL: String) {
    IN_MEMORY("jdbc:h2:mem:inMemory;DB_CLOSE_DELAY=-1"),
    MAIN("jdbc:h2:file:./database/main;DB_CLOSE_DELAY=-1"),
    TEST("jdbc:h2:file:./database/test;DB_CLOSE_DELAY=-1")
}