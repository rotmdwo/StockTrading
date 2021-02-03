import java.io.*;
import java.util.StringTokenizer;

public class ex17144 {
    static int[] upperPurifier;
    static int[] lowerPurifier;
    static int[][] direction = {{-1,0},{1,0},{0,-1},{0,1}};

    static public void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int time = Integer.parseInt(st.nextToken());
        int[][] mat = new int[n+1][m+1];

        for (int i=1;i<=n;i++) {
            st = new StringTokenizer(br.readLine());
            for (int j=1;j<=m;j++) {
                mat[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i=1;i<=n;i++) {
            if (mat[i][1] == -1) {
                upperPurifier = new int[] {i,1};
                lowerPurifier = new int[] {i+1,1};
                break;
            }
        }

        for (int i=0;i<time;i++) simulate(mat, n, m);

        int answer = 2;

        for (int i=1;i<=n;i++) {
            for (int j=1;j<=m;j++) {
                answer += mat[i][j];
            }
        }

        bw.write(""+answer);
        bw.flush();
    }

    static void simulate(int[][] mat, int n, int m) {
        int[][] tempMat = new int[n+1][m+1];
        for (int i=1;i<=n;i++) {
            for (int j=1;j<=m;j++) {
                tempMat[i][j] = 0;
            }
        }

        for (int i=1;i<=n;i++) {
            for (int j=1;j<=m;j++) {
                if (!((i == upperPurifier[0] && j == upperPurifier[1]) ||
                        (i == lowerPurifier[0] && j == lowerPurifier[1]))) {
                    diffuse(mat, tempMat, n, m, i, j);
                }
            }
        }

        for (int i=1;i<=n;i++) {
            for (int j=1;j<=m;j++) {
                mat[i][j] += tempMat[i][j];
            }
        }

        blowAway(mat, n, m);
    }

    static void diffuse(int[][] mat, int[][] tempMat, int n, int m, int x, int y) {
        int howMuch = mat[x][y] / 5;
        int howMany = 0;

        for (int i=0;i<4;i++) {
            int newX = x + direction[i][0];
            int newY = y + direction[i][1];

            if (canSpread(n, m, newX, newY)) {
                howMany++;
                tempMat[newX][newY] += howMuch;
            }
        }

        tempMat[x][y] -= howMany * howMuch;
    }

    static boolean canSpread(int n, int m, int x, int y) {
        if (x < 1 || x > n || y < 1 || y > m) return false;
        else if ((upperPurifier[0] == x && upperPurifier[1] == y) ||
                (lowerPurifier[0] == x && lowerPurifier[1] == y)) return false;
        return true;
    }

    static void blowAway(int[][] mat, int n, int m) {
        for (int i=upperPurifier[0]-2;i>=1;i--) {
            mat[i+1][1] = mat[i][1];
        }

        for (int i=lowerPurifier[0]+2;i<=n;i++) {
            mat[i-1][1] = mat[i][1];
        }

        for (int j=2;j<=m;j++) {
            mat[1][j-1] = mat[1][j];
            mat[n][j-1] = mat[n][j];
        }

        for (int i=2;i<=upperPurifier[0];i++) {
            mat[i-1][m] = mat[i][m];
        }

        for (int i=n-1;i>=lowerPurifier[0];i--) {
            mat[i+1][m] = mat[i][m];
        }

        for (int j=m-1;j>=2;j--) {
            mat[upperPurifier[0]][j+1] = mat[upperPurifier[0]][j];
            mat[lowerPurifier[0]][j+1] = mat[lowerPurifier[0]][j];
        }

        mat[upperPurifier[0]][2] = 0;
        mat[lowerPurifier[0]][2] = 0;
    }
}