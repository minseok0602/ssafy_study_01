package week_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_5644 {
    // 0:정지, 1:상, 2:우, 3:하, 4:좌
    static final int[] dr = {0, -1, 0, 1, 0};
    static final int[] dc = {0, 0, 1, 0, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int M = Integer.parseInt(st.nextToken());                         // 총 이동 시간
            int A = Integer.parseInt(st.nextToken());                         // 충전소(BC) 개수

            int[] moveA = new int[M];
            int[] moveB = new int[M];

            // 사용자 A
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) moveA[i] = Integer.parseInt(st.nextToken());

            // 사용자 B
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) moveB[i] = Integer.parseInt(st.nextToken());

            // BC
            int[] bcR = new int[A];
            int[] bcC = new int[A];
            int[] bcCov = new int[A];
            int[] bcPow = new int[A];

            for (int i = 0; i < A; i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());                             // 열(X)
                int y = Integer.parseInt(st.nextToken());                             // 행(Y)
                bcC[i] = x;
                bcR[i] = y;
                bcCov[i] = Integer.parseInt(st.nextToken());                         // 충전 범위
                bcPow[i] = Integer.parseInt(st.nextToken());                         // 충전량
            }

            // 사용자 초기 위치 (A: 1,1 / B: 10,10)
            int ar = 1, ac = 1;
            int brp = 10, bcp = 10;

            int ans = 0;

            // 시뮬레이션 시작
            for (int t = 0; t <= M; t++) {
                int best = 0;

                // 모든 BC 조합 완전 탐색 (i: A가 선택한 BC, j: B가 선택한 BC)
                // -1 : 어떤 BC도 선택하지 않음
                for (int i = -1; i < A; i++) {
                    int aVal = 0;
                    if (i != -1) {
                        // 맨해튼 거리 공식
                        int distA = Math.abs(ar - bcR[i]) + Math.abs(ac - bcC[i]);
                        if (distA <= bcCov[i]) aVal = bcPow[i];
                        else continue;
                    }

                    for (int j = -1; j < A; j++) {
                        int bVal = 0;
                        if (j != -1) {
                            int distB = Math.abs(brp - bcR[j]) + Math.abs(bcp - bcC[j]);
                            if (distB <= bcCov[j]) bVal = bcPow[j];
                            else continue;
                        }

                        int total;
                        // 동일 BC 중복 접속 처리
                        if (i != -1 && i == j) {
                            // 합쳐서 반으로 나누나, 그냥 한 번 더하나 똑같음
                            total = bcPow[i]; 
                        } else {
                            total = aVal + bVal;
                        }

                        // 해당 시간 최댓값 갱신
                        if (total > best) best = total;
                    }
                }

                ans += best; // 누적 충전량 합산

                if (t == M) break; // 마지막 이동 후 종료

                // 다음 위치로 이동
                int mdA = moveA[t];
                int mdB = moveB[t];
                ar += dr[mdA];
                ac += dc[mdA];
                brp += dr[mdB];
                bcp += dc[mdB];
            }

            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb.toString());
    }
}
