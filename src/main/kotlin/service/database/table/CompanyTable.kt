package prod.prog.service.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object CompanyTable : IntIdTable("companies") {
    val name = varchar("name", 50).uniqueIndex()
}