package com.ssafy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
    static int n;
    static int[] arr;
    static int answer;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();

        for (int tc = 1; tc <= T; tc++) {
            n = Integer.parseInt(br.readLine().trim());
            arr = new int[n];
            answer = Integer.MAX_VALUE;
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            int max = 0;
            for (int i = 0; i < n; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
                max = Math.max(max,arr[i]);
            }
            // target을 max, max+1, max+2까지 검사 (이 범위면 충분)
            int one = 0; // +1이 필요한 총 횟수
            int two = 0; // +2가 필요한 총 횟수

            for (int i = 0; i < n; i++) {
                int diff = max - arr[i];
                one += (diff & 1);      // diff % 2
                two += (diff / 2);
            }

            // two를 사용할 수 있는 날은 짝수이기 때문에, 짝수일 때만 기다리게 되면 너무 비효율적임
            // two는 결국 두 번의 one 작업으로 수행할 수 있기 때문에, one으로 환산해서, two와 one의 비율을 비슷하게 맞추는 작업이 필요함
            // 그렇다고 one이 two보다 훨씬 많은 경우를 리밸런싱할 필요가 있나? -> one이 two보다 많다고 해도 one을 two로 바꿀 수는 없다. 1씩만 필요한 작업인데 2를 올리게 되면 초과해버린다.

            // two와 one의 차이를 최소화를 하되,
            //two가 one보다 많을 때까지만 진행
            if(one==0&&two==0)
                answer = 0;
            else{
                while (two>=0) {
                    answer = Math.min(answer,Math.max(2*two,2*one-1));
                    two--;
                    one+=2;
                }
            }
            sb.append("#").append(tc).append(" ").append(answer).append("\n");
        }
        System.out.print(sb);
    }
}