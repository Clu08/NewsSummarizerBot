package prod.prog.actionProperties

// todo CreatedBy as a link to RequestManager?
data class ActionContext(val id: Long, val createdBy: String, val data: MutableMap<String, Any> = HashMap()) {
    companion object {
        fun system() = ActionContext(0L, "system  ")
    }
}
