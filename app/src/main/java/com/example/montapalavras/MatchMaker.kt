package com.example.montapalavras


import org.apache.commons.lang3.StringUtils
import java.text.Normalizer
import java.util.*

/**
 * Nessa versão o algoritmo é bem simples, ele percorre o dicionpário por inteiro e encontra os matches
 *
 * A partir daí ele encontra o grupo de palavras que pode ter a maior pontuação (pois pode ser mais de uma) e seleciona a menor
 *
 *
 * */

/**
 * Essa versão nao desconsidera acentos, espaços e caracteres especiais por que eu nao testei super ainda, então
 * decidi incluir essa versão um pouco melhor testada
 *
 * */

class MatchMaker (private var availableLetters: String){


    /**
     *
     * Eu não sou muito confiante com REGEX kkk
     *
     * */

    init {
        sanitizeLetters()
    }

    private fun sanitizeLetters() {

        availableLetters = Normalizer.normalize(availableLetters, Normalizer.Form.NFD)




        availableLetters = availableLetters.replace("[^\\p{ASCII}]".toRegex(), "")

        availableLetters = StringUtils.deleteWhitespace(availableLetters)

    }

    private val wordsList = listOf("Abacaxi", "Manada", "mandar", "porta", "mesa", "Dado", "Mangas", "Ja", "coisas", "radiografia",
        "matematica", "Drogas", "predios", "implementacao", "computador", "balao", "Xicara", "Tedio",
        "faixa", "Livro", "deixar", "superior", "Profissao", "Reuniao", "Predios", "Montanha", "Botânica",
        "Banheiro", "Caixas", "Xingamento", "Infestacao", "Cupim", "Premiada", "empanada", "Ratos",
        "Ruído", "Antecedente", "Empresa", "Emissario", "Folga", "Fratura", "Goiaba", "Gratuito",
        "Hidrico", "Homem", "Jantar", "Jogos", "Montagem", "Manual", "Nuvem", "Neve", "Operacao",
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

    /**
     * Aqui ele verifica a palavra considerando já ser um match, se encontrar um caracter que não está disponível, ele pula para a próxima e desiste de adicionar.
     *
     * Ao final se ele não desistiu de adicionar (é um match) ele adiciona na lista de matches
     * */

    private fun findMatches(): List<String>{
        val matches = mutableListOf<String>()
        var isMatch: Boolean
        outerLoop@ for(word in wordsList) {
            isMatch = true
            var lettersToLoop = availableLetters.toUpperCase(Locale.US)
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

    /**
     * Essa função descobre dentre os matches qual a maior pontuação adquirida, como mais de uma palavra
     * pode ter a mesma pontuação, ele considera como uma lista de palavras com a maior pontuação
     *
     * */

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

    /**
     * Essa descobre qual a menor palavra da maior pontuação
     *
     *
     * (e também acerta quais sao as letras que sobraram kkkk meio gambiarra releva)
     *
     * */

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


    /**
     *
     * Essas duas funções são helpers que eu criei para poder usar e deixar o código mais
     * claro pra mim mesmo
     *
     * A primeira calcula a pontuação dada uma palavra
     * Essa eu uso quando eu já tenho os matches para poder calcular a pontuação de cada match de um jeito mais claro
     * pra quem le
     *
     *
     * e a segunda remove uma string de um index (dada a string e o index)
     * eu usei para ir remover as 'pecinhas' do jogo
     *
     *
     * */

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