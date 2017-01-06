package io.ducommun.elo.league_orchestrator

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import org.springframework.stereotype.Service
import java.io.File

@Service
class CsvFileLoader : FileLoader {

    override fun load(fileLocation: String): List<List<NamedPlayerScore>> =

            File(fileLocation).mapOverLines {

                val names = it.getNames()
                val scores = it.getScores()

                names.zip(scores).toNamedPlayerScores()
            }


    private fun Array<String>.getNames() =
            filterIndexed { i, value -> i % 2 == 0 }
                    .filter { it != "" }
                    .map { it.replace(Regex("^ "), "") }
                    .map { it.replace(Regex(" $"), "") }

    private fun Array<String>.getScores() = filterIndexed { i, value -> i % 2 != 0 }.filter { it != "" }.map(String::toDouble)

    private fun List<Pair<String, Double>>.toNamedPlayerScores() = map {
        NamedPlayerScore(playerName = it.first, score = it.second)
    }

    private fun File.mapOverLines(fn: (Array<String>) -> List<NamedPlayerScore>) = open().map(fn)

    private fun File.open() =
            CsvMapper()
                    .apply { enable(CsvParser.Feature.WRAP_AS_ARRAY) }
                    .readerFor(Array<String>::class.java)
                    .readValues<Array<String>>(this)
                    .readAll()
}