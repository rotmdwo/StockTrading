import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.HashMap

fun main() {
    // 4개의 배열의 원소 합이 0인 조합 개수 구하기
    // for문 4번 돌면 O(n^4), 배열 2개 씩 쪼개서 이중 for문 2번 돌면 O(n^2)
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    var st = StringTokenizer(br.readLine())
    val N = st.nextToken().toInt()
    val mat = Array(N, {IntArray(4)})
    val hashMap = HashMap<Int, Int>()
    var answer = 0L

    for (i in 0 until N) {
        st = StringTokenizer(br.readLine())

        for (j in 0..3) {
            mat[i][j] = st.nextToken().toInt()
        }
    }

    for (i in 0 until N) {
        for (j in 0 until N) {
            val sum = mat[i][0] + mat[j][1]
            val hashValue = hashMap[sum]

            if (hashValue == null) hashMap[sum] = 1
            else hashMap[sum] = hashValue + 1
        }
    }

    for (i in 0 until N) {
        for (j in 0 until N) {
            val sum = - (mat[i][2] + mat[j][3])
            val hashValue = hashMap[sum]

            if (hashValue != null) answer += hashValue
        }
    }

    bw.write("${answer}")
    bw.flush()
}