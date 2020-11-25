package com.example.montapalavras

import org.apache.commons.lang3.StringUtils
import java.util.*


class MatchMaker (private var availableLetters: String){


    

    private val wordsList = listOf("Abacaxi", "Manada", "mandar", "porta", "mesa", "Dado", "Mangas", "Ja", "coisas", "radiografia",
        "matemática", "Drogas", "prédios", "implementação", "computador", "balão", "Xicara", "Tedio",
        "faixa", "Livro", "deixar", "superior", "Profissão", "Reunião", "Predios", "Montanha", "Botânica",
        "Banheiro", "Caixas", "Xingamento", "Infestação", "Cupim", "Premiada", "empanada", "Ratos",
        "Ruído", "Antecedente", "Empresa", "Emissario", "Folga", "Fratura", "Goiaba", "Gratuito",
        "Hidrico", "Homem", "Jantar", "Jogos", "Montagem", "Manual", "Nuvem", "Neve", "Operação",
        "Ontem", "Pato", "Pe", "viagem", "Queijo", "Quarto", "Quintal", "Solto", "rota", "Selva",
        "Tatuagem", "Tigre", "Uva", "Último", "Vituperio", "Voltagem", "Zangado", "Zombaria", "Dor"
    )

    private val pointsMap: Map<Int, List<Char>> = mapOf(
        1 to listOf('E', 'A', 'I', 'O', 'N', 'R', 'T', 'L', 'S', 'U'),
        2 to listOf('W', 'D', 'G'),
        3 to listOf('B', 'C', 'M', 'P'),
        4 to listOf('F', 'H', 'V'),
        8 to listOf('J', 'X'),
        10 to listOf('Q', 'Z')
    )


    private fun findMatches(): List<String>{
        val matches = mutableListOf<String>()
        var isMatch: Boolean
        outerLoop@ for(word in wordsList) {
            isMatch = true
            var lettersToLoop = availableLetters.toUpperCase()
            for(letter in word) {
                if(!lettersToLoop.contains(letter.toUpperCase())) {
                    isMatch = false
                    continue@outerLoop
                }
                lettersToLoop = removeAtHelperFunction(lettersToLoop, lettersToLoop.indexOf(letter.toUpperCase()))
            }
            if(isMatch) {
                matches.add(word)
            }
        }
        return matches

    }

    private fun findBestScores() : Pair<List<String>, Int> {
        val matchesList = findMatches()
        val highMatches = mutableListOf<String>()

        var bestScore = 0
        for(match in matchesList) {
            val points = pointsHelperFunction(match)
            if(points >= bestScore) {
                bestScore = points
                highMatches.add(match)
            }
        }

        val bestMatches = highMatches.filter {
            pointsHelperFunction(it) >= bestScore
        }


        return Pair(bestMatches, bestScore)

    }

    fun findBestWord(): Triple<String, Int, String> {
        val (unsortedMatches, score) = findBestScores()
        if(unsortedMatches.isNotEmpty()) {
            val sortedMatches = unsortedMatches.sortedBy {
                it.length
            }

            var remainings = availableLetters.toUpperCase(Locale.US)

            for (letter in sortedMatches[0]) {
                val index = remainings.indexOf(letter.toUpperCase())
                print("$letter is index $index\n")
                remainings = removeAtHelperFunction(remainings, index)
            }

            return Triple(sortedMatches[0], score, remainings)
        }
        return Triple("", 0, availableLetters)
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


    private fun removeAtHelperFunction(string: String, index: Int): String {
        return "${string.substring(0,index)}${string.substring(index+1)}"
    }

}