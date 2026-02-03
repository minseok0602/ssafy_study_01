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
            // 입력 처리
            int N = Integer.parseInt(br.readLine());
            int [][] arr = new int[N][N];
            for (int i = 0; i < N; i++){
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++){
                    arr[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            // 메인 로직
            sb.append("#").append(testCase).append(" ").append("\n");
            for (int i = 0; i < N; i++){
                // 90도 회전
                for (int j = 0; j < N; j++){
                    sb.append(arr[N - 1 - j][i]);
                }
                sb.append(" ");
                // 180도 회전
                for (int j = 0; j < N; j++){
                    sb.append(arr[N - 1 - i][N - 1 - j]);
                }
                sb.append(" ");
                // 270도 회전
                for (int j = 0; j < N; j++){
                    sb.append(arr[j][N - 1 - i]);
                }
                sb.append("\n");
            }
        }
        System.out.println(sb.toString());
    }
}