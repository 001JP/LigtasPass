package ph.kodego.ligtaspass.generator_settings

data class GeneratorSettings(
    var includeSymbols: Boolean = true,
    var includeNumbers: Boolean = true,
    var includeLowercase: Boolean = true,
    var includeUppercase: Boolean = true
) {
    companion object {
        const val INCLUDE_SYMBOLS = "include_symbols"
        const val INCLUDE_NUMBERS = "include_numbers"
        const val INCLUDE_LOWERCASE = "include_lowercase"
        const val INCLUDE_UPPERCASE = "include_uppercase"
    }
}