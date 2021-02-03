package Utils

import java.io.BufferedWriter
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class Methods {

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





    // a ~ z로 이루어진 string에서 각 알파벳이 쓰인 횟수를 arr[0] ~ arr[25]에 저장해 반환하는 메소드
    fun countLetters(string: String): IntArray {
        val arr = IntArray(26)
        for (i in string.indices) {
            arr[string[i].toInt().minus('a'.toInt())]++
        }
        return arr
    }

    // tensor[N][M][K] -> -1: 이동할 수 없는 공간, 0: 이동할 수 있는 공간, 1: 이동할 수 있는 물체
    // n > 1: 이동한 step
    // 3차원 텐서를 탐색하는 BFS 함수
    fun bfs3D(tensor: Array<Array<IntArray>>, N: Int, M: Int, K: Int) {
        val queue = LinkedList<IntArray>()

        for (n in 0 until N) {
            for (m in 0 until M) {
                for (k in 0 until K) {
                    if (tensor[n][m][k] == 1) queue.add(intArrayOf(n, m, k))
                }
            }
        }

        while (queue.isNotEmpty()) {
            val currentLoc = queue.removeAt(0)
            val x = currentLoc[0]
            val y = currentLoc[1]
            val z = currentLoc[2]

            if (x > 0 && tensor[x - 1][y][z] == 0) {
                addToQueue(tensor, queue, x, y, z, -1, 0, 0)
            }

            if (x < N - 1 && tensor[x + 1][y][z] == 0) {
                addToQueue(tensor, queue, x, y, z, 1, 0, 0)
            }

            if (y > 0 && tensor[x][y - 1][z] == 0) {
                addToQueue(tensor, queue, x, y, z, 0, -1, 0)
            }

            if (y < M - 1 && tensor[x][y + 1][z] == 0) {
                addToQueue(tensor, queue, x, y, z, 0, 1, 0)
            }

            if (z > 0 && tensor[x][y][z - 1] == 0) {
                addToQueue(tensor, queue, x, y, z, 0, 0, -1)
            }

            if (z < K - 1 && tensor[x][y][z + 1] == 0) {
                addToQueue(tensor, queue, x, y, z, 0, 0, 1)
            }
        }
    }

    // 3차원 텐서를 탐색하는 BFS함수를 보조하는 함수: 큐에 다음에 탐색할 위치를 넣어줌
    fun addToQueue(tensor: Array<Array<IntArray>>, queue: LinkedList<IntArray>, x: Int, y: Int, z: Int, dx: Int, dy: Int, dz: Int) {
        tensor[x + dx][y + dy][z + dz] = tensor[x][y][z] + 1
        queue.add(intArrayOf(x + dx, y + dy, z + dz))
    }

    // 이진트리의 PreOrder 순회 메서드
    fun traverseTreeByPreorder(node: BinaryTreeNode, bw: BufferedWriter) {
        bw.write("${node.key} ")

        node.left?.let { traverseTreeByPreorder(it, bw) }
        node.right?.let { traverseTreeByPreorder(it, bw) }
    }

    // 이진트리 노드
    data class BinaryTreeNode(val key: Int) {
        var left: BinaryTreeNode? = null
        var right: BinaryTreeNode? = null
    }

    // IntArray의 index 0를 기준으로 오름차순 정렬하는 Comparator
    object IncreasingIntArrayComparator: Comparator<IntArray> {
        override fun compare(o1: IntArray, o2: IntArray): Int {
            if (o1[0] < o2[0]) return -1
            else if (o1[0] > o2[0]) return 1
            else return 0
        }
    }
}