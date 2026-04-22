import java.util.*;
class Solution {
    static int[][] cost;
    static int[][] hint;
    static int[] count;
    static int answer = Integer.MAX_VALUE;
    static int n;
    static int k;
    public int solution(int[][] cost, int[][] hint) {
        this.cost = cost;
        this.hint = hint;
        n = cost.length;
        k = hint[0].length-1;
        // count[i] = x -> i번째 스테이지의 힌트권이 x개이다.
        count = new int[n];
        dfs(0,0);
        return answer;
    }
    public static void dfs(int sum, int idx){
        // 이미 sum이 answer보다 크면 그대로 종료
        if(sum>answer)
            return;
        // 스테이지 5번까지 다 해봤을 때 정답에 반영
        if(idx==n){
            answer = Math.min(answer,sum);
            return;
        }
        // 지금 인덱스 : 현재 스테이지
        // 이 스테이지에서, 현재 가지고 있는 해당 스테이지의 힌트권을 가지고 비용 소모해서 해결
        int cur_stage_count = count[idx];
        
        // 만약 힌트권이, n보다 더 많은 경우에는 그냥 n-1개 보유하고 있는 걸로 인식
        int cur_price = cur_stage_count>=n?cost[idx][n-1]:cost[idx][count[idx]];
        // 번들을 구매하지 않는 경우 -> 그냥 이번 스테이지 해결하고 바로 넘어감
        dfs(sum + cur_price,idx+1);
        
        // 번들을 구매하는 경우
        
        
        
        // 만약 마지막 라운드면 번들을 구매할 수 없음
        if(idx==n-1) return;
        
        // 현재 스테이지에서의 번들 구매 -> 번들 구매 비용을 추가함
        cur_price += hint[idx][0];
        
        // 번들 구매에 의한 힌트 갱신
        for(int i = 1;i<=k;i++){
            count[hint[idx][i]-1]++;
        }
        dfs(sum + cur_price,idx+1);
        
        // 원복
        for(int i = 1;i<=k;i++){
            count[hint[idx][i]-1]--;
        }
    }
}