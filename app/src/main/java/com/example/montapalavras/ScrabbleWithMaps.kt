package com.example.montapalavras

/**
 * Essa é a classe que encontra o melhor candidato, ele se baseia na estrutura criada
 * que é uma árvore de palavras organizada por sílabas de forma decrescente
 * em que cada item é uma mapa de (pontos do maior pro menor (chave) - palavras da menor pra maior com aquela pontuação (valor))
 *
 * Assim foi feito pois o algoritmo só ira buscar nas palavras que são compatíveis com o número de vogais
 * (logo sílabas, pois está em português) disponibilizado
 *
 * É interessante dessa forma pois com a garantia da ordenação, o algoritmo já sabe que o primeiro match
 * encontrado é a maior pontuação e a menor palavra, e retorna mais cedo
 *
 * Ao encontrar um match em um ponto ele só vai começar a procurar outro match de novo se
 * existir uma palavra menor com uma possível pontuação maior ou igual
 *
 * (como está em ordem descrescente, a ordem do loop sabe que primeiro se olha as maiores e por fim as
 * menores)
 *
 * */


class ScrabbleWithMaps (private val availableLetters: String){

     private val wordsBySyllable: Map<Int, ScoreList> = mapOf(
        5 to ScoreList("Vitupério", "radiografia", "matematica", "implementacao", "Antecedente"),
        4 to ScoreList("Botânica", "Zombaria","Tatuagem","Operação","Goiaba","Fratura", "Abacaxi", "empanada", "Premiada", "superior", "Emissário","Infestação", "Xingamento"),
        3 to ScoreList( "Zangado", "Voltagem", "Último", "viagem", "Queijo","Montagem", "Manual","Hídrico", "Ruído",  "manada", "xícara", "Empresa","reuniao", "Banheiro", "Gratuito","profissao", "Montanha"),
        2 to ScoreList(  "Tigre", "Uva","Quintal", "Solto", "rota", "Selva","Quarto", "Ontem", "Pato", "Nuvem", "Neve", "Homem", "Jantar", "Jogos", "mesa", "dado", "Cupim","Ratos","Folga", "Caixas","porta", "tedio", "faixa","livro","balao","mandar", "mangas", "coisas", "drogas", "predios", "deixar"),
        1 to ScoreList("ja", "Pé", "Dor")
    )

    /**
     * Essa é a função que procura o primeiro match
     * */
    private fun findFirstMatch(wordsList: List<String>): Pair<String, String>{
        outerLoop@ for(word in wordsList) {
            print("${word}\n")
            var lettersToLoop = availableLetters.toUpperCase()
            for(letter in word) {
                if(!lettersToLoop.contains(letter.toUpperCase())) {
                    print("Nao possui ${letter}, próximo\n")
                    continue@outerLoop
                }
                print("$lettersToLoop possui $letter, será?\n")
                lettersToLoop = removeAt(lettersToLoop, lettersToLoop.indexOf(letter.toUpperCase()))
            }
            print("achou\n")
            return Pair(word, lettersToLoop)
        }
        return Pair("", availableLetters)
    }

    /**
     * Nessa função acontece o algoritmo
     *
     * o melhor candidato começa vazio, entao o mapa de palavras por sílaba é filtrado pra ser compatível com
     * o número de vogais disponíveis
     *
     * pra cada valor dentro do mapa (que é o tal mapa (pontuação - lista de palavras menor pro maior com tal pontuação)
     * ele busca o primeiro match caso tenha lá dentro uma pontuação que possa superar a atual (como a pontuação está em ordem crescente de pontuação e descrescente de tamanho
     * , o primeiro será a maior pontuação com a menor quantidade de letras)
     *
     * achado um candidato a pontuação e o melhor candidato são atualizados
     *
     * no final retorna a pontuação, o melhor candidato e a quantidade de letras que sobraram
     *
     *
     * */

    fun findBestCandidate(nSyllables: Int): Triple<String, String, Int> {

        var bestCandidate = ""
        var remainingLetters = ""
        var points = 0

        val wordsThatMatchesSyllableNumber = wordsBySyllable.filterKeys {
            it <= nSyllables
        }

        wordsThatMatchesSyllableNumber.forEach {
            val scores = it.value.getAllScores()
            val scoresToLook = scores.filter {
                index ->
                index >= points
            }
            print("pontos ${points} scores to look ${scoresToLook}\n")
            if(scoresToLook.isNotEmpty()) {
            print("entrou\n")
                scoresToLook.forEach(fun(index: Int) {
                    val (candidate, remains) = findFirstMatch(
                        it.value.getScoreWordListOrNUll(index) ?: listOf()
                    )
                    print("match - $candidate sobrou - ${remains}\n")
                    remainingLetters = remains
                    if (candidate != "") {
                      print("entrou\n")
                       points = index
                       bestCandidate = candidate
                       print("best candidate - ${bestCandidate}\n")
                        return
                    }
                })
            }
        }
        print(bestCandidate)
        return Triple(bestCandidate, remainingLetters, points)
    }

    private fun removeAt(string: String, index: Int): String {
        return "${string.substring(0,index)}${string.substring(index+1)}"
    }

}