package Binary_Search

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

class ex1655 {
    fun main() {
        // 정렬 없이 이분탐색으로 어레이리스트 정렬상태 유지하기 문제
        // 문제의 목표는 지금까지 주어진 수들 중 중앙값 찾기
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        val N = StringTokenizer(br.readLine()).nextToken().toInt()
        val arrayList = ArrayList<Int>(N)

        for (i in 1..N) {
            insertNumIntoSortedArrayList(arrayList, StringTokenizer(br.readLine()).nextToken().toInt())
            bw.write("${arrayList[(arrayList.size - 1) / 2]}\n")
        }

        bw.flush()
    }

    fun insertNumIntoSortedArrayList(arrayList: ArrayList<Int>, num: Int) {
        // 바이너리 서치 사용, O(logN)
        var left = 0
        var right = arrayList.size - 1
        var mid = (left + right) / 2

        while (left <= right) {
            if (arrayList[mid] < num) left = mid + 1
            else right = mid - 1
            mid = (left + right) / 2
        }

        arrayList.add(left, num)
    }
}