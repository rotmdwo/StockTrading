package Segment_Tree

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex2042 {
    fun main() {
        // 세그먼트 트리의 합 문제
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())
        val N = st.nextToken().toInt()
        val M = st.nextToken().toInt() + st.nextToken().toInt()
        val arr = LongArray(N)

        for (i in 0 until N) arr[i] = StringTokenizer(br.readLine()).nextToken().toLong()

        val segmentTree = makeSegmentTree(arr, N)

        for (i in 1..M) {
            st = StringTokenizer(br.readLine())

            if (st.nextToken().toInt() == 1) {
                val index = st.nextToken().toInt() - 1
                val diff = st.nextToken().toLong() - arr[index]
                arr[index] += diff
                updateSum(segmentTree, 0, N - 1, 1, index, diff)
            } else {
                val from = st.nextToken().toInt() - 1
                val to = st.nextToken().toInt() - 1
                bw.write("${findSequenceSum(segmentTree, 0, N - 1, 1, from, to)}\n")
            }
        }

        bw.flush()
    }

    fun makeSegmentTree(arr: LongArray, N: Int): LongArray {
        val tree = LongArray(4 * N, { 0L })

        initSegmentTree(arr, tree, 0, N - 1, 1)

        return tree
    }

    fun initSegmentTree(arr: LongArray, tree: LongArray, start: Int, end: Int, key: Int): Long {
        if (start == end) {
            tree[key] = arr[start]

            return tree[key]
        }

        val mid = (start + end) / 2

        val leftSum = initSegmentTree(arr, tree, start, mid, key * 2)
        val rightSum = initSegmentTree(arr, tree, mid + 1, end, key * 2 + 1)

        tree[key] = leftSum + rightSum

        return tree[key]
    }

    // start, end: 데이터의 시작과 끝 0, N - 1
    // key: 방문 노드의 key
    // left, right: 찾으려는 데이터 구간
    fun findSequenceSum(tree: LongArray, start: Int, end: Int, key: Int, left: Int, right: Int): Long{
        if (left > end || right < start) return 0L

        if (left <= start && end <= right) return tree[key]

        val mid = (start + end) / 2

        val leftSum = findSequenceSum(tree, start, mid, key * 2, left, right)
        val rightSum = findSequenceSum(tree, mid + 1, end, key * 2 + 1, left, right)

        return leftSum + rightSum
    }

    fun updateSum(tree: LongArray, start: Int, end: Int, key: Int, index: Int, diff: Long) {
        if (index < start || end < index) return

        tree[key] += diff

        if (start == end) return

        val mid = (start + end) / 2

        updateSum(tree, start, mid, key * 2, index, diff)
        updateSum(tree, mid + 1, end, key * 2 + 1, index, diff)
    }
}
