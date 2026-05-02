package algo;

import java.lang.Math.*;

class Solution {
    int answer = 1;
    static int dist_limit;
    static int split_limit;
    public int solution(int dist_limit, int split_limit) {
        this.dist_limit = dist_limit;
        this.split_limit = split_limit;
        dfs(1, 1, 1, 0);

        return answer;
    }
    // c_dist : 분배기를 달 수 있는 리프 노드의 수
    // u_dist : 사용한 분배기 개수
    // c_split : 이번 깊이의 분배도
    // leaf : 분배기를 달 수 없는 리프 노드의 수 
    public void dfs(long c_dist, long u_dist, long c_split, long leaf) {
        // 지금까지 사용한 분배기 개수가 한계를 넘어섰으면 그대로 종료
        if(u_dist>dist_limit) return;
        
        answer = (int)Math.max(answer, leaf + c_dist);

        for(int i=2;i<=3;i++) {
            // 현재 깊이에서는, 분배기를 쓸 거면, 동일한 분배기를 써야함
            // 현재 깊이에서 분배기를 단다면, 이 분배기로 인해 생기는 리프노드의 분배도는 c_split*i임
            if(c_split*i > split_limit) continue;
                
            // maxJ : 이번 깊이에 가능한 많이 분배기를 달 때의 분배기 수
            // 남은 분배기 vs 이번 깊이에서 추가된 리프노드의 개수 중에 더 작은 걸 선택해서 분배기를 추가로 설치
            long maxJ = Math.min(i*c_dist, dist_limit-u_dist);
            dfs(maxJ, u_dist+maxJ, c_split*i, leaf+(i*c_dist-maxJ));
        }
    }
}