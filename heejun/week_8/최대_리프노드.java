import java.lang.Math.*;

class Solution {
	int answer = 1;

	public int solution(int dist_limit, int split_limit) {

		dfs(1, 1, 1, 0, dist_limit, split_limit);

		return answer;
	}

	/*
	 * c_dist = 지금 당장 더 확장할 수 있는 노드 수 
	 * u_dist = 지금까지 사용한 분배 노드 총 수 
	 * c_split = 지금까지의 분기split
	 * 누적값 leaf = 이미 리프로 확정된 노드 수
	 */

	public void dfs(long c_dist, long u_dist, long c_split, long leaf, int dist_limit, int split_limit) {
		if (c_dist < 0)
			return;
		answer = (int) Math.max(answer, leaf + c_dist);

		for (int i = 2; i <= 3; i++) {
			// 분배노드의 자식 개수 결정
			if (c_split * i > split_limit)
				continue;

			long maxJ = Math.min(i * c_dist, dist_limit - u_dist); //다음단계에 몇개 분배노드 할래
			dfs(maxJ, u_dist + maxJ, c_split * i, leaf + (i * c_dist - maxJ), dist_limit, split_limit);
			/*
			 * leaf → 이전까지 이미 리프로 확정된 개수 
			 * i * c_dist → 이번에 새로 생긴 자식 전체 개수 
			 * maxJ → 그중에서 다음 단계에서도 계속 분배 노드로 쓸 개수
			 */
		}
	}
}