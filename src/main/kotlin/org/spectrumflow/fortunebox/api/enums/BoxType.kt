package org.spectrumflow.fortunebox.api.enums

enum class BoxType(val text: String) {
    CSGO("CSGO"),
    Roulette("Roulette");

    companion object{
        private val map = entries.associateBy(BoxType::text)
        fun fromText(text: String) = map[text]
    }
}