import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = br.readLine();
        StringTokenizer st = new StringTokenizer(br.readLine());

        int k = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        if (k > str.length()) {
            System.out.println(0);
            return;
        }
        // 특정 패턴이 몇 번 등장했는지 기록
        Map<Long, Integer> map = new HashMap<>();

        long value = 0;
        // 제일 처음 패턴 (문자열에서 0 ~ k까지)을 Long으로 만들어서 기록
        for (int i = 0; i < k; i++) {
            value = value * 2 + (str.charAt(i) - '0');
        }

        map.put(value, 1);

        if (m == 1) {
            System.out.println(1);
            return;
        }

        // 비트 윈도우 슬라이딩을 하기 위한 기준 비트 설정
        long mask = (1L << k) - 1;

        for (int i = k; i < str.length(); i++) {
            // 현재 value를 왼쪽으로 쉬프팅 -> xxxxx0으로 전환
            // 여기에다가 0111...을 & 연산 -> 제일 왼쪽 비트는 무조건 0이됨 -> 제일 앞쪽 비트 무력화
            // 새로운 문자열 하나를 비트로 만들어서 오른쪽에다 붙임
            value = ((value << 1) & mask) + (str.charAt(i) - '0');

            int count = map.getOrDefault(value, 0) + 1;
            map.put(value, count);

            if (count >= m) {
                System.out.println(1);
                return;
            }
        }

        System.out.println(0);
    }
}