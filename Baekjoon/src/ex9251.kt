import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // DP, LCS(가장 긴 공통 부분수열) 문제
    // DP 배열 2개 필요: 1. 각 알파벳이 최초로 나오는 인덱스 배열,
    // 2. 문자열1의 각 인덱스까지의 문자열 중 가장 긴 문자열의 길이
    // 문제해결방법: 문자열2의 각 문자를 for문으로 돌면서
    // DP2의 배열을 for문으로 돌며 문자열2의 현재문자를 뒤에 붙이며 DP2를 업데이트 함
    // 이 때, DP2 배열을 거꾸로 for문을 돌아야 현재문자를 계속 이어붙이는 문제 안 생김
    // DP2 배열을 for문으로 돈 후에는 현재문자를 시작으로 하는 길이 1 수열을 DP2에 넣음
    // 현재문자를 시작으로 하는 길이 1 수열을 for문 전에 넣으면 또 자기 자신에 이어 붙이는 ex문제 생김
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))

    val str1 = StringTokenizer(br.readLine()).nextToken()
    val str2 = StringTokenizer(br.readLine()).nextToken()

    val dp = IntArray(str1.length, {0})
    val alphabetDP = IntArray(26, {-1})
    var answer = 0

    for (i in 0 until str1.length) {
        if (alphabetDP[str1[i].minus('A')] == -1) {
            alphabetDP[str1[i].minus('A')] = i
        }
    }

    // String[] -> Char 반환
    // Char.equals() 하면 문자가 같은지 확인할 수 있음
    for (i in 0 until str2.length) {
        val currentChar = str2[i]

        // 1단계: DP의 해당 문자 인덱스보다 앞에 있는 수열들 뒤에 해당 문자 추가하여 수열 확장
        for (j in str1.length - 1 downTo 0) {
            if (dp[j] == 0) continue

            val max = dp[j]
            val index = j

            for (k in index + 1 until str1.length) {
                if (currentChar.equals(str1[k])) {
                    if (dp[k] < max + 1) dp[k] = max + 1

                    if (answer < max + 1) answer = max + 1

                    break
                }
            }
        }

        // 2단계: 해당 문자가 가장 빨리 나오는 위치 찾음
        val tempIndex = alphabetDP[currentChar.minus('A')]
        if (tempIndex != -1 && dp[tempIndex] < 1) {
            dp[tempIndex] = 1

            if (answer < 1) answer = 1
        }
    }

    bw.write("${answer}")
    bw.flush()
}