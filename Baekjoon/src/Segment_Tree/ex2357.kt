package Segment_Tree

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex2357 {
    fun main() {
        // 세그먼트 트리 문제
        // 세그먼트 트리란 배열의 각 구간의 계산을 미리 해두어 트리 형태로 만들어 찾기 쉽게 하는 것
        // 데이터의 개수가 N개이면 N보다 큰 2의 제곱 수의 2배 만큼의 노드 필요함
        // ex) 데이터 19개라면 64개의 노드 필요
        // 트리 루트의 인덱스를 0이 아닌 1로 하여 계산하기 좋게 함
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())
        val N = st.nextToken().toInt()
        val M = st.nextToken().toInt()
        val arr = IntArray(N)

        for (i in 0 until N) arr[i] = StringTokenizer(br.readLine()).nextToken().toInt()

        val segmentTree = makeSegmentTree(arr, N)

        for (i in 0 until M) {
            st = StringTokenizer(br.readLine())

            val a = st.nextToken().toInt()
            val b = st.nextToken().toInt()

            val minMax = findSequenceMinMax(segmentTree, 0, N - 1, 1, a - 1, b - 1)
            bw.write("${minMax[0]} ${minMax[1]}\n")
        }

        bw.flush()
    }

    fun makeSegmentTree(arr: IntArray, N: Int): Array<Node> {
        val tree = Array<Node>(4 * N, { Node(it) })

        initSegmentTree(arr, tree, 0, N - 1, 1)

        return tree
    }

    fun initSegmentTree(arr: IntArray, tree: Array<Node>, start: Int, end: Int, key: Int): Node {
        if (start == end) {
            tree[key].max = arr[start]
            tree[key].min = arr[start]

            return tree[key]
        }

        val mid = (start + end) / 2

        val nodeLeft = initSegmentTree(arr, tree, start, mid, key * 2)
        val nodeRight = initSegmentTree(arr, tree, mid + 1, end, key * 2 + 1)

        tree[key].max = Math.max(nodeLeft.max, nodeRight.max)
        tree[key].min = Math.min(nodeLeft.min, nodeRight.min)

        return tree[key]
    }

    // start, end: 데이터의 시작과 끝 0, N - 1
    // key: 방문 노드의 key
    // left, right: 찾으려는 데이터 구간
    fun findSequenceMinMax(tree: Array<Node>, start: Int, end: Int, key: Int, left: Int, right: Int): IntArray{
        if (left > end || right < start) return intArrayOf(Int.MAX_VALUE, Int.MIN_VALUE)

        if (left <= start && end <= right) return intArrayOf(tree[key].min, tree[key].max)

        val mid = (start + end) / 2

        val arrayLeft = findSequenceMinMax(tree, start, mid, key * 2, left, right)
        val arrayRight = findSequenceMinMax(tree, mid + 1, end, key * 2 + 1, left, right)

        val min = Math.min(arrayLeft[0], arrayRight[0])
        val max = Math.max(arrayLeft[1], arrayRight[1])

        return intArrayOf(min, max)
    }

    data class Node(val key: Int) {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
    }
}
