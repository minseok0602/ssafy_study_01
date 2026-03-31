package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class 여왕_개미 {
    static int N, Q;
    static int nextId;

    static ArrayList<Integer> homes;   // 현재 살아있는 집들의 좌표 (항상 오름차순 유지)
    static ArrayList<Integer> ids;     // homes와 같은 인덱스의 집 번호

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());
        StringTokenizer st;

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
            case 100: // 초기 마을 건설
                N = Integer.parseInt(st.nextToken());
                homes = new ArrayList<>();
                ids = new ArrayList<>();

                for (int i = 1; i <= N; i++) {
                    int x = Integer.parseInt(st.nextToken());
                    homes.add(x);
                    ids.add(i);
                }
                nextId = N + 1;
                break;

            case 200: // 새 집 건설 (항상 맨 뒤)
                int p = Integer.parseInt(st.nextToken());
                homes.add(p);
                ids.add(nextId++);
                break;

            case 300: // 집 철거 (좌표가 아니라 "집 번호" q)
                int removeId = Integer.parseInt(st.nextToken());

                for (int i = 0; i < ids.size(); i++) {
                    if (ids.get(i) == removeId) {
                        ids.remove(i);
                        homes.remove(i);
                        break;
                    }
                }
                break;

            case 400: // 정찰
                int r = Integer.parseInt(st.nextToken());
                System.out.println(findMinTime(r));
                break;
            }
        }
    }

    // 정찰 최소 시간
    private static int findMinTime(int r) {
        int m = homes.size();

        // 살아있는 집이 0개 or 1개면 시간 0
        if (m <= 1) return 0;

        // 집 개수 이상 개미가 있으면 각 집에서 바로 시작하면 끝
        // (여왕 집까지 고려할 필요 없이 이미 0초 가능)
        if (r >= m) return 0;

        int left = 0;
        int right = homes.get(m - 1) - homes.get(0);
        int answer = right;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (can(mid, r)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return answer;
    }

    // 시간 limit 안에 r마리 이하로 모든 집을 커버 가능한가?
    private static boolean can(int limit, int r) {
        int count = 0; //지금까지 사용한 개미 수
        int i = 0; //아직 커버하지 않은 집을 가리키는 인덱스
        int m = homes.size(); //살아 있는 집 개수

        while (i < m) {
            count++;
            int start = homes.get(i);

            // 이 개미는 start ~ start+limit 까지 커버 가능
            while (i < m && homes.get(i) - start <= limit) {
                i++;
            }

            if (count > r) return false;
        }

        return true;
    }
}