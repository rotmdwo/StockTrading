package Combination

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

class ex1759 {
    fun main() {
        // 조합론 문제
        // nCr 중 r이 확고히 정해져 있지 않은 경우 중첩 for문으로 풀 수 없음
        // 여기서 푼 방식은 바뀌지 않는 originalArray, 함수를 돌며 바뀌는 array,
        // n, r, 함수를 돌아도 r의 값을 유지하는 q가 필요
        // 다만 if(r == 0)문에서 array의 배열은 0부터 q-1까지 거꾸로 되어 있음
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())
        val L = st.nextToken().toInt()
        val C = st.nextToken().toInt()
        val consonants = ArrayList<Char>()
        val vowels = ArrayList<Char>()

        st = StringTokenizer(br.readLine())

        for (i in 0 until C) {
            val letter = st.nextToken()

            if (letter == "a" || letter == "e" || letter == "i" || letter == "o" || letter == "u") {
                vowels.add(letter[0])
            } else {
                consonants.add(letter[0])
            }
        }

        consonants.sort()
        vowels.sort()

        val to = Math.min(vowels.size, L - 2)
        val answerArrayList = ArrayList<String>()

        for (i in 1..to) {
            val numOfVowels = i
            val numOfConsonants = L - i

            if (consonants.size < numOfConsonants) continue

            val combinedConsonants = getCombinatedLetters(consonants, consonants.size, numOfConsonants)
            val combinedVowels = getCombinatedLetters(vowels, vowels.size, numOfVowels)

            for (i in combinedConsonants.indices) {
                for (j in combinedVowels.indices) {
                    val combinedLetters = combinedConsonants[i] + combinedVowels[j]
                    combinedLetters.sort()
                    var string = ""

                    for (element in combinedLetters) string += element
                    answerArrayList.add(string)
                }
            }
        }

        answerArrayList.sort()

        for (i in answerArrayList.indices) {
            bw.write("${answerArrayList[i]}\n")
        }
        bw.flush()
    }

    fun getCombinatedLetters(letters: ArrayList<Char>, n: Int, r: Int): Array<CharArray> {
        val combinedLetters = ArrayList<CharArray>()
        val lettersArray = letters.toCharArray()
        val originalArray = CharArray(n)
        System.arraycopy(lettersArray, 0, originalArray, 0, n)

        combination(originalArray, lettersArray, combinedLetters, n, r, r)

        return combinedLetters.toTypedArray()
    }

    fun combination(originalArray: CharArray, lettersArray: CharArray, combinedLetters: ArrayList<CharArray>,
                    n: Int, r: Int, q: Int) {
        if (r == 0) {
            val array = CharArray(q)
            System.arraycopy(lettersArray, 0, array, 0, q)
            combinedLetters.add(array)
        } else if (n < r) {
            return
        } else {
            lettersArray[r - 1] = originalArray[n - 1]
            combination(originalArray, lettersArray, combinedLetters, n - 1, r - 1, q)
            combination(originalArray, lettersArray, combinedLetters, n - 1, r, q)
        }
    }
}
