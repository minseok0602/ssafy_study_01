import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        String s = br.readLine();

        if (N % 2 == 1) {// 홀수면 불가능
            System.out.println("No");
            return;
        }

        int in = 0; //들어온 사람
        int out = 0; //나간 사람
        int question = 0; //물음표

        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);

            if (c == '(') {
                in++;
            } else if (c == ')') {
                out++;
            } else {
                question++;
            }
        }
        // 나간 사람 과 들어온 사람이 딱 반반이어야한다.
        int needIn = N / 2 - in;
        int needOut = N / 2 - out;

        if (needIn < 0 || needOut < 0 || needIn + needOut != question) {
            System.out.println("No");
            return;
        }
        // 나간 사람 들어온 사람 총수 체크
        //들어오면 + 나가면 -
        int count = 0;



        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);

            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            } else { // ?는 벨런스 있게 주자
                if (needIn > 0) {
                    count++;
                    needIn--;
                } else {
                    count--;
                    needOut--;
                }
            }

            // CCTV 어느 시점에서도 들어온 직원수가 나간 직원 수 보다 많아야함
            if (count < 0) {
                System.out.println("No");
                return;
            }
        }

        if (count == 0) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}