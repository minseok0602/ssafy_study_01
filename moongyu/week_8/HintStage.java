class HintStage {
    int n;
    int[][] cost;
    int[][] hint;
    int answer = Integer.MAX_VALUE;

    public int solution(int[][] cost, int[][] hint) {
        this.n = cost.length;
        this.cost = cost;
        this.hint = hint;
        
        // 각 스테이지에서 사용할 수 있는 힌트 개수 (힌트를 산다면 여기에 저장됨)
        int[] hints = new int[n]; 
        dfs(0, hints, 0);

        return answer;
    }
    
    // sum에 cost 누적됨
    void dfs(int stage, int[] hints, int sum) {
        // 현재 스테이지에서 쓸 수 있는 힌트는 무조건 다 쓰는 게 이득 (몇 개 쓸건지 결정)
    	// 한 스테이지에서 최대로 사용할 수 있는 힌트권은 n - 1개임. (계속 사서 넣다보면 n - 1개 넘을 수도 있음)
        int use = Math.min(hints[stage], n - 1);
        sum += cost[stage][use];

        // 가지치기
        if (sum >= answer) return;

        // 마지막 스테이지면 종료
        if (stage == n - 1) {
            answer = Math.min(answer, sum);
            return;
        }

        // 1) 현재 스테이지에서 번들을 사지 않는 경우
        dfs(stage + 1, hints, sum);

        // 2) 현재 스테이지에서 번들을 사는 경우 (
        int[] nextHints = hints.clone();
        for (int i = 1; i < hint[stage].length; i++) {
        	// 문제 번호는 1번부터라서 -1
            nextHints[hint[stage][i] - 1]++;
        }
        
        // 구매 비용 더하고 dfs 진행
        dfs(stage + 1, nextHints, sum + hint[stage][0]);
    }
}