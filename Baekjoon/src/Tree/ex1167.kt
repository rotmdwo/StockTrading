package Tree

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

class ex1167 {
    private var max = 0
    private lateinit var visited: BooleanArray
    private var tempMax = 0
    private var farthestNode = 1

    fun main() {
        // Tree, DFS 문제
        // Edge에 가중치가 있는 트리의 가장 먼 두 노드의 거리 찾기
        // Root에서부터 가장 먼 거리에 있는 노드를 찾고 이 노드로부터 가장 먼 거리를 찾는다
        // 트리 구현은 Tree.Segment_Tree.Node 클래스와 Tree.Segment_Tree.Node 배열로 함
        // DFS에서 거리를 잴 때 재귀함수 부르기 전에 가중치를 더하고 재귀함수에서 나오면 가중치를 뺀다.
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())
        val N = st.nextToken().toInt()

        val nodeArray = Array(N + 1, { Node(it) })

        for (i in 1..N) {
            st = StringTokenizer(br.readLine())
            val loc = st.nextToken().toInt()
            var dest = st.nextToken().toInt()
            var weight = st.nextToken().toInt()

            while (true) {
                nodeArray[loc].nodeList.add(nodeArray[dest])
                nodeArray[loc].indexList.add(weight)

                dest = st.nextToken().toInt()
                if (dest == -1) break
                weight = st.nextToken().toInt()
            }
        }

        visited = BooleanArray(N + 1, {false})
        tempMax = 0
        findTheFarthestNode(1, nodeArray)

        visited = BooleanArray(N + 1, {false})
        tempMax = 0
        max = 0
        findDiameterInTreeByDFS(farthestNode, nodeArray)

        bw.write("$max")
        bw.flush()
    }

    private fun findTheFarthestNode(begin: Int, nodeArray: Array<Node>) {
        dfs(begin, nodeArray, true)
    }

    private fun findDiameterInTreeByDFS(begin: Int, nodeArray: Array<Node>) {
        dfs(begin, nodeArray, false)
    }

    private fun dfs(begin: Int, nodeArray: Array<Node>, wantFarthestNode: Boolean) {
        visited[begin] = true

        for (i in 0 until nodeArray[begin].nodeList.size) {
            if (!visited[nodeArray[begin].nodeList[i].key]) {
                val weight = nodeArray[begin].indexList[i]
                tempMax += weight
                if (tempMax > max) {
                    max = tempMax
                    if (wantFarthestNode) farthestNode = nodeArray[begin].nodeList[i].key
                }
                dfs(nodeArray[begin].nodeList[i].key, nodeArray, wantFarthestNode)
                tempMax -= weight
            }
        }
    }

    private data class Node(val key: Int) {
        val nodeList = ArrayList<Node>()
        val indexList = ArrayList<Int>()
    }
}

