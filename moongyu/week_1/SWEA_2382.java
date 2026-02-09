package week_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_2382 {

    // 1:상, 2:하, 3:좌, 4:우
    static final int[] dr = {0, -1, 1, 0, 0};
    static final int[] dc = {0, 0, 0, -1, 1};

    // 방향 반전
    static int reverseDir(int d) {
        if (d == 1) return 2;
        if (d == 2) return 1;
        if (d == 3) return 4;
        return 3;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken()); // 맵 크기
            int M = Integer.parseInt(st.nextToken()); // 격리 시간
            int K = Integer.parseInt(st.nextToken()); // 군집 개수

            // 군집 데이터 관리용 배열
            int[] r = new int[K];
            int[] c = new int[K];
            int[] cnt = new int[K];
            int[] dir = new int[K];

            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());
                r[i] = Integer.parseInt(st.nextToken());
                c[i] = Integer.parseInt(st.nextToken());
                cnt[i] = Integer.parseInt(st.nextToken());
                dir[i] = Integer.parseInt(st.nextToken());
            }

            // 최적화용 배열 (1차원 배열 활용)
            int size = N * N;                // 2차원을 1차원으로 펼치기 위한 크기
            int[] sum = new int[size];      // 해당 칸 총합
            int[] max = new int[size];      // 해당 칸 최대 미생물 수
            int[] dirPick = new int[size];  // 이동 방향 결정
            boolean[] touched = new boolean[size];    // 방문 여부
            int[] touchedKeys = new int[size];        // 실제 방문한 칸 인덱스 리스트

            // 시뮬레이션 시작
            for (int t = 0; t < M; t++) {
                int touchedCnt = 0;

                for (int i = 0; i < K; i++) {
                    if (cnt[i] == 0) continue; // 소멸한 군집 패스

                    // 이동
                    int nr = r[i] + dr[dir[i]];
                    int nc = c[i] + dc[dir[i]];
                    int ncnt = cnt[i];
                    int ndir = dir[i];

                    // 경계 처리
                    if (nr == 0 || nc == 0 || nr == N - 1 || nc == N - 1) {
                        ncnt /= 2;            // 반토막
                        ndir = reverseDir(ndir); // 방향 반전
                        if (ncnt == 0) {      // 소멸 처리
                            cnt[i] = 0;
                            continue;
                        }
                    }

                    // 병합 준비 (2D -> 1D 인덱스 변환)
                    int key = nr * N + nc;

                    // 해당 칸 첫 방문 시 초기화 및 리스트 등록
                    if (!touched[key]) {
                        touched[key] = true;
                        touchedKeys[touchedCnt++] = key;
                        sum[key] = 0;
                        max[key] = 0;
                        dirPick[key] = 0;
                    }

                    // 동일 칸 군집 정보 누적
                    sum[key] += ncnt; 
                    if (ncnt > max[key]) {    // 가장 큰 군집의 방향 선택
                        max[key] = ncnt;
                        dirPick[key] = ndir;
                    }
                }

                // 이동 결과 갱신 (살아남은 군집 확인)
                int newK = 0;
                for (int idx = 0; idx < touchedCnt; idx++) {
                    int key = touchedKeys[idx];
                    int total = sum[key];
                    
                    if (total > 0) {
                        r[newK] = key / N;       // 좌표 2차원으로 복구
                        c[newK] = key % N;
                        cnt[newK] = total;       // 병합된 수
                        dir[newK] = dirPick[key]; // 결정된 방향
                        newK++;
                    }
                    
                    // 다음 시뮬레이션 준비
                    touched[key] = false;
                    sum[key] = 0;
                    max[key] = 0;
                    dirPick[key] = 0;
                }

                // 군집 개수 업데이트
                for (int i = newK; i < K; i++) cnt[i] = 0;
                K = newK;
            }
            
            int ans = 0;
            for (int i = 0; i < K; i++) ans += cnt[i];

            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb.toString());
    }
}
