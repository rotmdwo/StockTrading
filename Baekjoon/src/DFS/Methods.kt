package DFS

// 행렬의 각 원소에는 알파벳 대문자가 써있고, 한번 방문한 문자는 재방문 불가할 때 최대 많이 방문한 알파벳 수 구하는 DFS.dfs
fun dfs(mat: Array<CharArray>, visited: HashSet<Char>, x: Int, y: Int, depth: Int): Int {
    val currentAlphabet = mat[x][y]
    visited.add(currentAlphabet)
    var max = depth

    if (x > 0 && !visited.contains(mat[x - 1][y])) {
        max = dfs(mat, visited, x - 1, y, depth + 1)
    }
    if (x < mat.size - 1 && !visited.contains(mat[x + 1][y])) {
        val tempNum = dfs(mat, visited, x + 1, y, depth + 1)
        if (tempNum > max) max = tempNum
    }
    if (y > 0 && !visited.contains(mat[x][y - 1])) {
        val tempNum = dfs(mat, visited, x, y - 1, depth + 1)
        if (tempNum > max) max = tempNum
    }
    if (y < mat[0].size - 1 && !visited.contains(mat[x][y + 1])) {
        val tempNum = dfs(mat, visited, x, y + 1, depth + 1)
        if (tempNum > max) max = tempNum
    }
    visited.remove(currentAlphabet)
    return max
}

// 아직 방문하지 않은 곳은 true, 갈 수 없거나 이미 방문한 곳은 false로 표시된
// 크기 N의 행렬 mat를 DFS로 방문할 수 있는 곳을 모두 방문하는 메소드.
// x, y는 현재 방문한 노드의 행/열을 의미하며, mat가 visited 역할을 함
fun dfs(mat: Array<BooleanArray>, N: Int, x: Int, y: Int) {
    mat[x][y] = false // visited 역할

    // 인근 노드 방문
    if (x > 0 && mat[x - 1][y]) dfs(mat, N, x - 1, y)
    if (y < N - 1 && mat[x][y + 1]) dfs(mat, N, x, y + 1)
    if (x < N - 1 && mat[x + 1][y]) dfs(mat, N, x + 1, y)
    if (y > 0 && mat[x][y - 1]) dfs(mat, N, x, y - 1)
}