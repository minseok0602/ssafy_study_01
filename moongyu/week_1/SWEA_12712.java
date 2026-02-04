package week_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_12712 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        int[][] crossDir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        int[][] xDir = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
        for (int testCase = 1; testCase <= T; testCase++){
            // 입력 처리
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int [][] arr = new int[N][N];
            for (int i = 0; i < N; i++){
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++){
                    arr[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            // 메인 로직
            int max = -1;
            int crossSum = 0;               // + 형태 분사 합 (가로, 세로 탐색)
            int xSum = 0;                   // x 형태 분사 합 (대각선 탐색)
            for (int curRow = 0; curRow < N; curRow++){
                for (int curCol = 0; curCol < N; curCol++){
                    crossSum = arr[curRow][curCol];
                    xSum = arr[curRow][curCol];
                    for (int i = 0; i < 4; i++){
                        for (int j = 0; j < M - 1; j++){
                            // M의 세기만큼 형태에 맞게 방위 탐색
                            int crossNr = curRow + (crossDir[i][0] * (j + 1));
                            int crossNc = curCol + (crossDir[i][1] * (j + 1));
                            if (crossNr >= 0 && crossNr < N && crossNc >= 0 && crossNc < N){
                                crossSum += arr[crossNr][crossNc];
                            }

                            int xNr = curRow + (xDir[i][0] * (j + 1));
                            int xNc = curCol + (xDir[i][1] * (j + 1));
                            if (xNr >= 0 && xNr < N && xNc >= 0 && xNc < N) {
                                xSum += arr[xNr][xNc];
                            }
                            // 최댓값 갱신
                            if (crossSum > max) max = crossSum;
                            if (xSum > max) max = xSum;
                        }
                    }
                }
            }
            sb.append("#").append(testCase).append(" ").append(max).append("\n");
        }
        System.out.println(sb.toString());
    }
}
