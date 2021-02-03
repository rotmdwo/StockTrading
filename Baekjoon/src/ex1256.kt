import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    // 못 품
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val st = StringTokenizer(br.readLine())
    val N = st.nextToken().toInt()
    val M = st.nextToken().toInt()
    val K = st.nextToken().toInt()
/*
    val arr = IntArray(N + M, {it})
    val dictionaryNumbers = getCombinatedNumbers(arr, N + M, N)
    val dictionaryAZ = Array(dictionaryNumbers.size, {StringBuilder(N + M)})
    val dictionary = Array(dictionaryNumbers.size, {""})

    for (i in 0 until dictionaryNumbers.size) {
        for (j in 0 until N + M) dictionaryAZ[i].append('z')
        for (j in 0 until dictionaryNumbers[i].size) dictionaryAZ[i][dictionaryNumbers[i][j]] = 'a'
        dictionary[i] = dictionaryAZ[i].toString()
    }

    dictionary.sort()
    bw.write("${dictionary[K - 1]}")

 */
    bw.write("${calculateNCr(N, M)}")
    bw.flush()
}

fun getCombinatedNumbers(arr: IntArray, n: Int, r: Int): Array<IntArray> {
    val combinedNumbers = ArrayList<IntArray>()
    val originalArray = IntArray(n)
    System.arraycopy(arr, 0, originalArray, 0, n)

    combination1(originalArray, arr, combinedNumbers, n, r, r)

    return combinedNumbers.toTypedArray()
}

fun combination1(originalArray: IntArray, numbersArray: IntArray, combinedNumbers: ArrayList<IntArray>,
                 n: Int, r: Int, q: Int) {
    if (r == 0) {
        val array = IntArray(q)
        System.arraycopy(numbersArray, 0, array, 0, q)
        combinedNumbers.add(array)
    } else if (n < r) {
        return
    } else {
        numbersArray[r - 1] = originalArray[n - 1]
        combination1(originalArray, numbersArray, combinedNumbers, n - 1, r - 1, q)
        combination1(originalArray, numbersArray, combinedNumbers, n - 1, r, q)
    }
}

private val dp = Array(201, { LongArray(201, { -1 }) })

fun calculateNCr(n: Int, r: Int): Long {
    if (n == r) return 1
    else if (r == 1) return n.toLong()
    else if (dp[n][r] != (-1).toLong()) return dp[n][r]
    else {
        dp[n][r] = calculateNCr(n - 1, r - 1) + calculateNCr(n - 1, r)
        return dp[n][r]
    }
}