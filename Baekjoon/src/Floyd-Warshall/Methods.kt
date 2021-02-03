package `Floyd-Warshall`

// 양방향 불리언(도달 여부) 플로이드 와샬
// i ~ k, k ~ j 도달가능하면 -> i ~ j도 도달가능
fun booleanFloydWarshall(mat: Array<BooleanArray>, N: Int) {
    for (k in 0 until N) {
        for (i in 0 until N) {
            for (j in 0 until N) {
                if (!mat[i][j] && mat[i][k] && mat[k][j]) {
                    mat[i][j] = true
                    mat[j][i] = true
                }
            }
        }
    }
}

// 가장 기본적인 단방향 플로이드 와샬 메서드
fun floydWarshall(mat: Array<IntArray>, N: Int) {
    for (i in 1 .. N) {
        for (j in 1 .. N) {
            for (k in 1 .. N) {
                mat[j][k] = Math.min(mat[j][k], mat[j][i] + mat[i][k])
            }
        }
    }
}