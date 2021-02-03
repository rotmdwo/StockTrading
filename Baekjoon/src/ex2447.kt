import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.util.*

fun main() {
    // 재귀함수 문제
    // 각 레벨에서는 정사각형을 9등분 하여, 가운데를 비우고 ㅁ자로 그리되, 각 등분을 다음 레벨의 사각형으로 그린다
    // 각 레벨에서 별을 찍을 때는 찍는 위치를 알아야 하기 때문에 x라는 값으로 찍는 행을 알려준다
    // x값은 레벨을 더욱 깊숙히 들어갈 때 마다 늘려준다
    // 찍은 별과 공백을 저장하는 String은 자주 바뀌어야 하기 때문에 StringBuilder를 쓰면 효율적이다
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val N = StringTokenizer(br.readLine()).nextToken().toInt()

    val mat = Array(N, {StringBuilder("")})

    drawStars(mat, N, 0, false)

    for (i in 0 until N) {
        bw.write("${mat[i]}\n")
    }
    bw.flush()
}

fun drawStars(mat: Array<StringBuilder>, N: Int, x: Int, isHole: Boolean) {
    if (N == 3 && !isHole) {
        mat[x].append('*')
        mat[x].append('*')
        mat[x].append('*')
        mat[x + 1].append('*')
        mat[x + 1].append(' ')
        mat[x + 1].append('*')
        mat[x + 2].append('*')
        mat[x + 2].append('*')
        mat[x + 2].append('*')
    } else if (N == 3 && isHole) {
        for (i in 0..2) {
            for (j in 0..2) {
                mat[x + i].append(' ')
            }
        }
    } else if (isHole) {
        for (i in 0..2) {
            for (j in 0..2) {
                drawStars(mat, N / 3, x + N / 3 * i, true)
            }
        }
    } else {
        drawStars(mat, N / 3, x + N / 3 * 0, false)
        drawStars(mat, N / 3, x + N / 3 * 0, false)
        drawStars(mat, N / 3, x + N / 3 * 0, false)
        drawStars(mat, N / 3, x + N / 3 * 1, false)
        drawStars(mat, N / 3, x + N / 3 * 1, true)
        drawStars(mat, N / 3, x + N / 3 * 1, false)
        drawStars(mat, N / 3, x + N / 3 * 2, false)
        drawStars(mat, N / 3, x + N / 3 * 2, false)
        drawStars(mat, N / 3, x + N / 3 * 2, false)
    }
}