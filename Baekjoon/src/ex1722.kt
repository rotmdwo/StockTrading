import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // 주어진 순열의 순서를 구하는 문제 + 주어진 순서에 맞는 순열 구하는 문제
    // 팩토리얼 이용
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    var st = StringTokenizer(br.readLine())
    val N = st.nextToken().toInt()
    st = StringTokenizer(br.readLine())

    if (st.nextToken().toInt() == 1) {
        val k = st.nextToken().toLong()
        val answer = findNthOrderOfPermutation(N, k)

        for (i in 0 until N) {
            bw.write("${answer[i]} ")
        }
    } else {
        val order = IntArray(N)
        for (i in 0 until N) order[i] = st.nextToken().toInt()

        bw.write("${findNthOrderOfPermutation(N, order)}")
    }

    bw.flush()
}

// DP 사용한 팩토리얼 메서드
fun factorial(N: Int, dp: LongArray): Long {
    if (dp[N] != 0L) return dp[N]
    else if (N == 0 || N == 1) return 1
    else {
        dp[N] = N * factorial(N - 1, dp)
        return dp[N]
    }
}

// 숫자의 배열이 주어졌을 때 순열의 몇 번째 조합인지 찾아주는 메서드
// ex) 1 4 2 3은 5번째 순열조합
fun findNthOrderOfPermutation(N: Int, arr: IntArray): Long {
    var nthOrder = 1L
    val dp = LongArray(N + 1, {0L})
    val leftNumbers = LinkedList<Int>()

    for (i in 1..N) leftNumbers.add(i)


    for (i in 0 until N) {
        val index = leftNumbers.indexOf(arr[i])
        nthOrder += index * factorial(leftNumbers.size - 1, dp)
        leftNumbers.removeAt(index)
        leftNumbers.sort()
    }

    return nthOrder
}

// N개의 숫자로 이루어지는 순열들에서 order번째 순서의 순열조합을 반환하는 메서드
// ex) N = 4, order = 5 -> 1 4 2 3
fun findNthOrderOfPermutation(N: Int, order: Long): IntArray {
    val dp = LongArray(N + 1)
    val nthOrderArray = IntArray(N)
    val leftNumbers = LinkedList<Int>()
    for (i in 1..N) leftNumbers.add(i)
    var leftCount = order - 1

    for (i in 0 until N) {
        val factorial = factorial(leftNumbers.size - 1, dp)
        val times = leftCount / factorial
        leftCount -= times * factorial
        nthOrderArray[i] = leftNumbers.removeAt(times.toInt())
    }

    return nthOrderArray
}