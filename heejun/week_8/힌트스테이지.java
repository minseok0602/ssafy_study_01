class Solution {
	static int answer;
	static int N;

	public int solution(int[][] cost, int[][] hint) {
		answer = 999_999_999;
		N = cost.length; 
		int[] h = new int[N]; // 내가 가지고 있는 힌트권 수
		sim(0, h, 0, cost, hint);
		return answer;
	}

	static void sim(int cnt, int[] h, int money, int[][] cost, int[][] hint) {
		//cnt 현재 스테이지 수 라 생각
		if (cnt == N) {
			answer = Math.min(answer, money);
			return;
		}

		// 현재 스테이지에서 사용할 힌트 수
		int use = h[cnt];
		if (use >= N)
			use = N - 1; // 안전 처리

		// 구매 안 함
		sim(cnt + 1, h.clone(), money + cost[cnt][use], cost, hint);

		// 마지막 스테이지는 힌트 번들 구매 불가
		if (cnt == N - 1)
			return;

		// 힌트권 구매
		int[] nh = h.clone();
		int nMoney = money;
		int[] a = hint[cnt];

		nMoney += a[0];
		for (int i = 1; i < a.length; i++) {
			nh[a[i] - 1]++;
		}

		sim(cnt + 1, nh, nMoney + cost[cnt][use], cost, hint);
	}
}