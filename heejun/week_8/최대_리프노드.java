import java.lang.Math.*;

class Solution {
    int answer = 1;

    public int solution(int dist_limit, int split_limit) {

        dfs(1, 1, 1, 0, dist_limit, split_limit);

        return answer;
    }

    public void dfs(long c_dist, long u_dist, long c_split, long leaf, int dist_limit, int split_limit) {
        if(c_dist < 0) return;
        answer = (int)Math.max(answer, leaf + c_dist);

        for(int i=2;i<=3;i++) {
            // 분배노드의 자식 개수 결정
            if(c_split*i > split_limit) continue;

            long maxJ = Math.min(i*c_dist, dist_limit-u_dist);
            dfs(maxJ, u_dist+maxJ, c_split*i, leaf+(i*c_dist-maxJ), dist_limit, split_limit);
        }
    }
}