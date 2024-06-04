package prod.prog.utils

import org.w3c.dom.CharacterData
import org.w3c.dom.Element

fun getCharacterDataFromElement(e: Element?): String {
    return (e?.firstChild as? CharacterData)?.data ?: ""
}