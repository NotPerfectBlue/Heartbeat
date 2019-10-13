package config

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException

object ConfigFile {
    private val fileName = ".timetracker.cfg"
    private var cachedConfigFile: String? = null

    private val configFilePath: String
        get() {
            if (ConfigFile.cachedConfigFile == null) {
                if (System.getenv("WAKATIME_HOME") != null && !System.getenv("WAKATIME_HOME").trim { it <= ' ' }.isEmpty()) {
                    val folder = File(System.getenv("WAKATIME_HOME"))
                    if (folder.exists()) {
                        ConfigFile.cachedConfigFile = File(folder, ConfigFile.fileName).absolutePath
                        return ConfigFile.cachedConfigFile as? String ?: ""
                    }
                }
                ConfigFile.cachedConfigFile = File(System.getProperty("user.home"), ConfigFile.fileName).absolutePath
            }
            return ConfigFile.cachedConfigFile  as? String ?: ""
        }

    operator fun get(section: String, key: String): String? {
        val file = ConfigFile.configFilePath
        var `val`: String? = null
        try {
            val br = BufferedReader(FileReader(file))
            var currentSection = ""
            try {
                var line: String? = br.readLine()
                while (line != null) {
                    if (line.trim { it <= ' ' }.startsWith("[") && line.trim { it <= ' ' }.endsWith("]")) {
                        currentSection =
                            line.trim { it <= ' ' }.substring(1, line.trim { it <= ' ' }.length - 1).toLowerCase()
                    } else {
                        if (section.toLowerCase() == currentSection) {
                            val parts = line.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (parts.size == 2 && parts[0].trim { it <= ' ' } == key) {
                                `val` = parts[1].trim { it <= ' ' }
                                br.close()
                                return `val`
                            }
                        }
                    }
                    line = br.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } catch (e1: FileNotFoundException) { /* ignored */
        }

        return `val`
    }

    operator fun set(section: String, key: String, `val`: String) {
        val file = ConfigFile.configFilePath
        var contents = StringBuilder()
        try {
            val br = BufferedReader(FileReader(file))
            try {
                var currentSection = ""
                var line: String? = br.readLine()
                var found: Boolean? = false
                while (line != null) {
                    if (line.trim { it <= ' ' }.startsWith("[") && line.trim { it <= ' ' }.endsWith("]")) {
                        if (section.toLowerCase() == currentSection && (!found!!)!!) {
                            contents.append("$key = $`val`\n")
                            found = true
                        }
                        currentSection =
                            line.trim { it <= ' ' }.substring(1, line.trim { it <= ' ' }.length - 1).toLowerCase()
                        contents.append(line + "\n")
                    } else {
                        if (section.toLowerCase() == currentSection) {
                            val parts = line.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            val currentKey = parts[0].trim { it <= ' ' }
                            if (currentKey == key) {
                                if ((!found!!)!!) {
                                    contents.append("$key = $`val`\n")
                                    found = true
                                }
                            } else {
                                contents.append(line + "\n")
                            }
                        } else {
                            contents.append(line + "\n")
                        }
                    }
                    line = br.readLine()
                }
                if ((!found!!)!!) {
                    if (section.toLowerCase() != currentSection) {
                        contents.append("[" + section.toLowerCase() + "]\n")
                    }
                    contents.append("$key = $`val`\n")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } catch (e1: FileNotFoundException) {

            // cannot read config file, so create it
            contents = StringBuilder()
            contents.append("[" + section.toLowerCase() + "]\n")
            contents.append("$key = $`val`\n")
        }

        var writer: PrintWriter? = null
        try {
            writer = PrintWriter(file, "UTF-8")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        if (writer != null) {
            writer.print(contents.toString())
            writer.close()
        }
    }

}
