package ph.kodego.ligtaspass.generator_settings

data class GeneratorSettings(
    var includeSymbols: Boolean = true,
    var includeNumbers: Boolean = true,
    var includeLowercase: Boolean = true,
    var includeUppercase: Boolean = true
)