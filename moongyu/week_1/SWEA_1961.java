package week_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_1961 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        for (int testCase = 1; testCase <= T; testCase++){
            int N = Integer.parseInt(br.readLine());
            int [][] arr = new int[N][N];
            for (int i = 0; i < N; i++){
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++){
                    arr[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            // 90도 회전
            int[][] turn90 = new int[N][N];
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    turn90[i][j] = arr[N - 1 - j][i];
                }
            }
            // 180도 회전
            int[][] turn180 = new int[N][N];
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    turn180[i][j] = arr[N - 1 - i][N - 1 - j];
                }
            }
            // 270도 회전
            int[][] turn270 = new int[N][N];
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    turn270[i][j] = arr[j][N - 1 - i];
                }
            }
            sb.append("#").append(testCase).append(" ").append("\n");
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    sb.append(turn90[i][j]);
                }
                sb.append(" ");
                for (int j = 0; j < N; j++){
                    sb.append(turn180[i][j]);
                }
                sb.append(" ");
                for (int j = 0; j < N; j++){
                    sb.append(turn270[i][j]);
                }
                sb.append("\n");
            }
        }
        System.out.println(sb.toString());
    }
}
