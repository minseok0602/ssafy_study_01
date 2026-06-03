import java.util.Scanner;

public class 보안담당자 {
    static int right_need = 0; // ? 중에서 ( 개수
    static int left_need = 0;  // ? 중에서 ) 개수

    static int right_cnt = 0; // 문자열에서 ( 개수
    static int left_cnt = 0;  // 문자열에서 ) 개수

    static int w_cnt = 0;
    static int interval = 0;
    static boolean check = false;
    static int N;
    static String S;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();
        S = sc.next();

        for (int i = 0; i < S.length(); i++) {
            if (S.charAt(i) == '(') {
                right_cnt++;
            } else if (S.charAt(i) == ')') {
                left_cnt++;
            } else {
                w_cnt++;
            }
        }

        interval = Math.abs(right_cnt - left_cnt);

        // 일단 적어도 (랑 ) 차이만큼은 ?가 있어야함 
        if (w_cnt < interval) {
            System.out.println("No");
            return;
        }
        // (랑) 차이 만큼 ?가 있더라도, 그거 제외한 나머지 ? 개수가 홀수면 안됨
        if ((w_cnt - interval) % 2 != 0) {
            System.out.println("No");
            return;
        }

        // 문자열 시작이 )이거나, 문자열 끝이 (면 답이 없음
        if (S.charAt(0) == ')' || S.charAt(N - 1) == '(') {
            System.out.println("No");
            return;
        }

        if (right_cnt >= left_cnt) {
        	// 기본적으로, w_cnt-inteval을 2로 나눈 만큼은 left_need, right_need가 가지고 있어야함
        	// 거기다가 추가로 (가 부족한 상황에서는 left_need에 그만큼 더해줌
        	// )가 부족한 상황에는 right_need에 그만큼 더해줌
            left_need = right_cnt - left_cnt + (w_cnt - interval) / 2;
            right_need = (w_cnt - interval) / 2;
        } else {
            right_need = left_cnt - right_cnt + (w_cnt - interval) / 2;
            left_need = (w_cnt - interval) / 2;
        }
        // status는, 입출입 정합성을 판단하는 변수 (문자열을 탐색하면서 0이만으로 떨어지면 안됨)
        int status = 0;
        int used_right = 0;
        for(int i = 0;i<N;i++){
            if(S.charAt(i)=='('){
            	// 현재 문자가 (면 status를 증가 시킴
                status++;
            }
            // 만약에 )인데 status가 0이면, 더이상 못나감 -> 에러
            else if(S.charAt(i)==')'){
                if(status==0)
                    {
                        System.out.println("No");
                        return;
                    }
                status--;
            }
            // 이번 문제가 '?'인 경우
            else{
                // 만약에 아직 right가 남아있으면 무조건  right쓰기
                if(used_right<right_need){
                    status++;
                    used_right++;
                }
                // right가 안 남아있으면 ?를 )에다가 써야함
                else if(right_need-used_right==0){
                	// 무조건 )를 써야되는데 status가 0이면 말이 안됨
                    if(status==0){
                        System.out.println("No");
                        return;
                    }
                    status--;
                    
                }
            }
        }

        System.out.println("Yes");
    }
}