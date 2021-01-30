package de.moritzruth.rhomes

import org.bukkit.configuration.Configuration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigFile private constructor(
    private val file: File,
    private val yamlConfiguration: YamlConfiguration
): Configuration by yamlConfiguration {
    fun save() {
        yamlConfiguration.save(file)
    }

    companion object {
        fun createOrLoad(name: String): ConfigFile {
            val file = plugin.dataFolder.resolve("$name.yml")
            val yamlConfiguration = YamlConfiguration.loadConfiguration(file)

            return ConfigFile(file, yamlConfiguration)
        }
    }
}
