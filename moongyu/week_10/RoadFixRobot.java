import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class RoadFixRobot {

    static int N, K;
    // 각 구멍 위치 저장 배열
    static long[] holes;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        // 구멍 개수
        N = Integer.parseInt(st.nextToken());
        // 패치 최대 개수
        K = Integer.parseInt(st.nextToken());

        holes = new long[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            holes[i] = Long.parseLong(st.nextToken());
        }

        Arrays.sort(holes);
        
        long left = 1;
        long right = holes[N - 1] - holes[0] + 1;
        long answer = right;
        
        // 패치 길이 이분 탐색
        while (left <= right) {
            long mid = (left + right) / 2;
            
            // 해당 길이의 패치로 모든 구멍을 덮을 수 있는지 체크
            if (isPossible(mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        System.out.println(answer);
    }

    static boolean isPossible(long length) {
    	// 사용 패치 개수
        int count = 0;
        
        // 덮지 않은 구멍을 체크
        int idx = 0;

        while (idx < N) {
            count++;

            long start = holes[idx];
            // 해당 패치가 커버할 수 있는 범위
            long end = start + length - 1;
            
            // 현재 패치로 덮을 수 있는 구멍 스킵
            while (idx < N && holes[idx] <= end) {
                idx++;
            }

            if (count > K) {
                return false;
            }
        }

        return true;
    }
}