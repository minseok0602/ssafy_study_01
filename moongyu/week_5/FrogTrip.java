package Day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class FrogTrip {
	private static final int[] dr = {-1, 1, 0, 0};
	private static final int[] dc = {0, 0, -1, 1};
	private static int N;
	private static char[][] map;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		StringBuilder sb = new StringBuilder();
		N = Integer.parseInt(br.readLine());
		map = new char[N + 1][N + 1];
		for (int i = 1; i <= N; i++) {
			String line = br.readLine();
			for (int j = 1; j <= N; j++) {
				map[i][j] = line.charAt(j - 1);
			}
		}
		
		int Q = Integer.parseInt(br.readLine());
		for (int t = 0; t < Q; t++) {
			int[][][] dist = new int[N + 1][N + 1][6];	// 점프력에 따라 dist 배열 생성
			for (int i = 0; i <= N; i++) {
				for (int j = 0; j <= N; j++) {
					Arrays.fill(dist[i][j], Integer.MAX_VALUE);
				}
			}
			st = new StringTokenizer(br.readLine());
			int sr = Integer.parseInt(st.nextToken());
			int sc = Integer.parseInt(st.nextToken());
			int fr = Integer.parseInt(st.nextToken());
			int fc = Integer.parseInt(st.nextToken());
			
			dist[sr][sc][1] = 0;
			
			PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
			pq.add(new int[] {sr, sc, 0, 1});			// 0 : row, 1 : col, 2 : dist, 3 : jumpPower
			
			while(!pq.isEmpty()) {
				int[] curState = pq.poll();
				int cRow = curState[0];
				int cCol = curState[1];
				int cDist = curState[2];
				int cJump = curState[3];
				
				if (dist[cRow][cCol][cJump] < cDist) continue;
				
				// 1. 점프 : 현재 점프력 그대로 점프하는 경우
				for (int d = 0; d < 4; d++) {
					if (!canJump(cRow, cCol, d, cJump)) continue;
					
					int nr = cRow + dr[d] * cJump;
					int nc = cCol + dc[d] * cJump;
					int newDist = cDist + 1;
					
					if (dist[nr][nc][cJump] > newDist) {
						dist[nr][nc][cJump] = newDist;
						pq.add(new int[] {nr, nc, newDist, cJump});
					}
				}
				
				// 2. 점프력 증가 : 점프력을 증가시키고 점프하는 경우
				for (int nJump = cJump + 1; nJump <= 5; nJump++) {
					int cost = 0;
					for (int k = cJump + 1; k <= nJump; k++) {
						cost += k * k;
					}
					
					for (int d = 0; d < 4; d++) {
						if (!canJump(cRow, cCol, d, nJump)) continue;
						
						int nr = cRow + dr[d] * nJump;
						int nc = cCol + dc[d] * nJump;
						int newDist = cDist + cost + 1;	// cost: 점프력 증가시키는데만 걸리는 시간, 그래서 점프시간 1 더해줘야 함.
						
						if (dist[nr][nc][nJump] > newDist) {
							dist[nr][nc][nJump] = newDist;
							pq.add(new int[] {nr, nc, newDist, nJump});
						}
					}
				}
				
				// 3. 점프력 감소 : 점프력을 원하는 값으로 감소시키고 점프하는 경우
				for (int nJump = 1; nJump < cJump; nJump++) {
					for (int d = 0; d < 4; d++) {
						if (!canJump(cRow, cCol, d, nJump)) continue;
						
						int nr = cRow + dr[d] * nJump;
						int nc = cCol + dc[d] * nJump;
						int newDist = cDist + 2;	// 점프력 감소시키는데 1초, 점프하는데 1초
						
						if (dist[nr][nc][nJump] > newDist) {
							dist[nr][nc][nJump] = newDist;
							pq.add(new int[] {nr, nc, newDist, nJump});
						}
					}
				}
				
			}
			
			int ans = Integer.MAX_VALUE;
			
			for (int i = 1; i <= 5; i++) {
				ans = Math.min(ans, dist[fr][fc][i]);
			}
			
			sb.append(ans == Integer.MAX_VALUE ? -1 : ans).append('\n');
		}
		System.out.println(sb);
	}
	
	static boolean canJump(int row, int col, int dir, int jumpPower) {
		for (int i = 1; i <= jumpPower; i++) {
			int nr = row + dr[dir] * i;
			int nc = col + dc[dir] * i;
			
			if (nr < 1 || nr >= N + 1 || nc < 1 || nc >= N + 1) return false;		// 밖으로 벗어나는 경우
			if (map[nr][nc] == '#') return false;						// 오리가 있는 경우
			
			if (i == jumpPower && map[nr][nc] == 'S') return false;		// 점프로 도착하는 곳이 미끄러운 곳인 경우
		}
		return true;
	}
}
