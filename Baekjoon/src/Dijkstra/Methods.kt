package Dijkstra

// (행: 출발위치, 열: 도착위치)를 의미하는 크기 N + 1짜리 행렬 mat이 주어질 때 (index 0은 빈 공간),
// departure 노드로부터 다른 노드들까지 최단 거리를 구해 mat에 저장하는 메소드
fun dijkstra(mat: Array<IntArray>, N: Int, departure: Int) {
    val visited = BooleanArray(N + 1, {i ->
        if (i != departure) false
        else true
    })
    var howManyNodesVisited = 1

    while (howManyNodesVisited < N) {
        var minDistance = Int.MAX_VALUE
        var nodeToVisitNext = 1
        for (i in 1..N) {
            if (!visited[i] && minDistance > mat[departure][i]) {
                nodeToVisitNext = i
                minDistance = mat[departure][i]
            }
        }

        for (i in 1..N) {
            if (mat[departure][i] > mat[departure][nodeToVisitNext] + mat[nodeToVisitNext][i]) {
                mat[departure][i] = mat[departure][nodeToVisitNext] + mat[nodeToVisitNext][i]
            }
        }

        visited[nodeToVisitNext] = true
        howManyNodesVisited++
    }
}