import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MineRobotSimul {
	
	private static int N, T;
	private static int[][] board;

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		
		board = new int[N][N];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int[][] startDp = new int[N][N];
		int[][] endDp = new int[N][N];
		int[][][] dp = new int[N][N][T + 1];

		// startDp : 시작점에서 (r, c) 칸에 올 때 얻을 수 있는 최대 이득 저장
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				startDp[r][c] = Integer.MIN_VALUE;
				
				
				// 시작점
				if (r == 0 && c == 0) {
					startDp[r][c] = board[r][c];
					continue;
				}
				
				// 위에서 오는 경로가 최대
				if (r > 0) {
					startDp[r][c] = Math.max(startDp[r][c], startDp[r - 1][c] + board[r][c]);
				}
				// 왼쪽에서 오는 경로가 최대
				if (c > 0) {
					startDp[r][c] = Math.max(startDp[r][c], startDp[r][c - 1] + board[r][c]);
				}
			}
		}

		// endDp : (r, c) 칸에서 도착점까지 이동 시 얻을 수 있는 최대 이득 저장
		for (int r = N - 1; r >= 0; r--) {
			for (int c = N - 1; c >= 0; c--) {
				endDp[r][c] = Integer.MIN_VALUE;
				
				// 종점
				if (r == N - 1 && c == N - 1) {
					endDp[r][c] = board[r][c];
					continue;
				}
				
				// 아래에서 오는 경로가 최대
				if (r + 1 < N) {
					endDp[r][c] = Math.max(endDp[r][c], endDp[r + 1][c] + board[r][c]);
				}
				
				// 오른쪽에서 오는 경로 최대
				if (c + 1 < N) {
					endDp[r][c] = Math.max(endDp[r][c], endDp[r][c + 1] + board[r][c]);
				}
			}
		}

		// dp[r][c][t] -> (r, c) 칸에서 t초 이동했을 때 얻을 수 있는 최대 이득 저장
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				for (int t = 0; t <= T; t++) {
					dp[r][c][t] = Integer.MIN_VALUE;
				}
			}
		}

		// 0초 동안 이동하면 추가로 얻는 이득은 0
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				dp[r][c][0] = 0;
			}
		}

		// (r, c)에서 정확히 t초 이동하는 경우 계산
		for (int t = 1; t <= T; t++) {
			for (int r = 0; r < N; r++) {
			    for (int c = 0; c < N; c++) {
			    	
			    	// 아래로 이동하면 board[r + 1][c] 먹고, 이동에 1초 썼으니까 아래 칸에서 (t - 1)초 이동하는 최대 
					if (r + 1 < N && dp[r + 1][c][t - 1] != Integer.MIN_VALUE) {
						dp[r][c][t] = Math.max(
							dp[r][c][t],
							board[r + 1][c] + dp[r + 1][c][t - 1]
						);
					}

					// 오른쪽으로 이동하면 board[r][c + 1] 먹고, 동일하게
					if (c + 1 < N && dp[r][c + 1][t - 1] != Integer.MIN_VALUE) {
						dp[r][c][t] = Math.max(
							dp[r][c][t],
							board[r][c + 1] + dp[r][c + 1][t - 1]
						);
					}
				}
			}
		}

		// 시간 역행을 아예 사용하지 않는게 최대일 수도 있음.
		int answer = startDp[N - 1][N - 1];
		
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (dp[r][c][T] == Integer.MIN_VALUE) continue;
				
				// 모든 (r, c) 좌표에 
				// [시작점 -> (r, c) + (r, c)에서 시간 역행 최대값 + (r, c) -> 도착] 계산
				int total = startDp[r][c] + dp[r][c][T] + endDp[r][c];
				answer = Math.max(answer, total);
			}
		}

		System.out.println(answer);
	}
}