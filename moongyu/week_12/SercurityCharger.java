import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SercurityCharger {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        char[] arr = br.readLine().toCharArray();

        // 길이가 홀수면 정상 괄호 X
        if (N % 2 == 1) {
            System.out.println("No");
            return;
        }

        int left = 0;   // 현재 왼쪽 괄호 개수
        int right = 0;  // 현재 오른쪽 괄호 개수

        // 이미 있는 괄호 개수 세기
        for (int i = 0; i < N; i++) {
            if (arr[i] == '(') {
                left++;
            } else if (arr[i] == ')') {
                right++;
            }
        }

        // 정상 문자열이 되려면 딱 N/2개씩 필요
        int needleft = N / 2 - left;
        int needright = N / 2 - right;

        // 이미 괄호가 필요개수 보다 많으면 종료
        if (needleft < 0 || needright < 0) {
            System.out.println("No");
            return;
        }

        // '?'를 적절히 바꾸기
        // 가능한 한 앞쪽 '?'부터 '('로 바꿔준다.
        for (int i = 0; i < N; i++) {
            if (arr[i] == '?') {
                if (needleft > 0) {
                    arr[i] = '(';
                    needleft--;
                } else {
                    arr[i] = ')';
                    needright--;
                }
            }
        }

        // 정상 문자열인지 검사
        int balance = 0;

        for (int i = 0; i < N; i++) {
            if (arr[i] == '(') {
                balance++;
            } else {
                balance--;
            }

            if (balance < 0) {
                System.out.println("No");
                return;
            }
        }

        if (balance == 0) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}