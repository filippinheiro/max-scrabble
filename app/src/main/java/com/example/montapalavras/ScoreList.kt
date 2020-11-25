package com.example.montapalavras

import java.util.*

/**
 * Essa é a principal estrutura do programa
 * elá é responsável por, dado um conjunto de strings, ela calcula a pontuação de cada e organiza em
 * um mapa em ordem crescente de pontuação e decrescente de tamanho
 *
 * ela é a estrutura que calcula as pontuações de antemao pra depois só precisarmos olhar o index do match
 *
 *
 * */

class ScoreList(vararg  words: String) {

    private val pointsMap: Map<Int, List<Char>> = mapOf(
        1 to listOf('E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U'),
        2 to listOf('W', 'D', 'G'),
        3 to listOf('B', 'C', 'M', 'P'),
        4 to listOf('F', 'H', 'V'),
        8 to listOf('J', 'X'),
        10 to listOf('Q', 'Z')
    )


    private var wordList = mutableListOf<String>()
    private var scores = sortedMapOf<Int, List<String>>()

    init {
        for(word in words) {
            wordList.add(word)
        }
    }

    init {
        scores = buildScoresMap()
    }

    private fun pointsHelperFunction(word: String): Int{
        var point = 0
        for(letter in word) {
            pointsMap.forEach {
                if (it.value.contains(letter.toUpperCase())) {
                    point += it.key
                }
            }
        }
        return point
    }

    fun getAllScores(): Collection<Int> {
        return scores.keys
    }

    fun getScoreWordListOrNUll(key: Int): List<String>? {
        return scores[key]
    }

    private fun buildScoresMap(): SortedMap<Int, List<String>> {

        val scoresUnsorted = mutableMapOf<Int, MutableList<String>>()

        wordList.forEach {
            val wordPoints = pointsHelperFunction(it)
            if (scoresUnsorted.containsKey(wordPoints)) {
                scoresUnsorted.getValue(wordPoints).add(it)
                scoresUnsorted[wordPoints]?.sortBy {item ->
                    item.length
                }
                return@forEach
            }
            scoresUnsorted[wordPoints] = mutableListOf(it)

            return@forEach
        }

        return scoresUnsorted.toSortedMap(compareBy {
            it.times(-1)
        })
    }


}