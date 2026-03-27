package org.ewsk.fortunebox.api.enums

enum class BoxType(val text: String) {
    CSGO("CSGO"),
    Roulette("Roulette"),
    Roll("Roll");

    companion object{
        private val map = entries.associateBy(BoxType::text)
        fun fromText(text: String) = map[text]
    }
}