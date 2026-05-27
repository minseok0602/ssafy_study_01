package studt_s;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class 로봇청소기 {
	static int N, K, L;
	static int[][] board;

	static class Robot {
		int id;
		int r;
		int c;

		public Robot(int id, int r, int c) {
			super();
			this.id = id;
			this.r = r;
			this.c = c;
		}
	}

	static ArrayList<Robot> robots;
	static int[][] rBoard;

	static int[] dr = { -1, 0, 1, 0 };
	static int[] dc = { 0, 1, 0, -1 };

	static class Node {
		int r;
		int c;
		int depth;

		public Node(int r, int c, int depth) {
			super();
			this.r = r;
			this.c = c;
			this.depth = depth;
		}

	}

	public static void main(String[] args) throws IOException {
		////////////////////// 입력 받기/////////////////////////////////////
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		L = Integer.parseInt(st.nextToken());

		board = new int[N][N];
		rBoard = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		robots = new ArrayList<>();
		for (int i = 0; i < K; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			robots.add(new Robot(i, r, c));
			rBoard[r][c] = 1;
		}

		////////////////////////// 시뮬레이션 시작////////////////////////
		while (L-- > 0) {
			moveRobot();
			clean();
			dust();
			spread();
			out();
		}

	}

	private static void out() {
		int sum = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] >= 0) {
					sum += board[i][j];
				}
			}

		}
		System.out.println(sum);
	}

	private static void spread() {
	    int[][] add = new int[N][N]; // 새로운 배열을 만들어서 한번에 적용시켜야 함

	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            if (board[i][j] != 0) {
	                continue;
	            }

	            int sum = 0;

	            for (int d = 0; d < 4; d++) {
	                int nr = i + dr[d];
	                int nc = j + dc[d];

	                if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
	                    continue;
	                }

	                if (board[nr][nc] == -1) {
	                    continue;
	                }

	                sum += board[nr][nc];
	            }

	            add[i][j] = sum / 10;
	        }
	    }

	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            board[i][j] += add[i][j];
	        }
	    }
	}

	private static void dust() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] > 0)
					board[i][j] += 5;
			}
		}

	}

	private static void clean() {
		// 제외 방향 순서: 왼쪽, 위쪽, 오른쪽, 아래쪽
		// 이 순서가 곧 청소 방향 우선순위 오른쪽 > 아래쪽 > 왼쪽 > 위쪽을 의미함
		int[] cleanDr = { 0, -1, 0, 1, 0 };
		int[] cleanDc = { -1, 0, 1, 0, 0 };

		for (Robot robot : robots) {
			int r = robot.r;
			int c = robot.c;

			int noDir = -1;
			int maxClean = 0;

			// noDir 방향 하나를 제외하고 나머지 4칸 청소
			for (int exclude = 0; exclude < 4; exclude++) {
				int sum = 0;

				for (int d = 0; d < 5; d++) {
					if (d == exclude) {
						continue;
					}

					int nr = r + cleanDr[d];
					int nc = c + cleanDc[d];

					if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
						continue;
					}

					if (board[nr][nc] == -1) {
						continue;
					}

					sum += Math.min(board[nr][nc], 20);
				}

				if (noDir == -1 || sum > maxClean) {
					maxClean = sum;
					noDir = exclude;
				}
			}

			if (maxClean == 0) {
				continue;
			}

			for (int d = 0; d < 5; d++) {
				if (d == noDir) {
					continue;
				}

				int nr = r + cleanDr[d];
				int nc = c + cleanDc[d];

				if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
					continue;
				}

				if (board[nr][nc] == -1) {
					continue;
				}

				board[nr][nc] = Math.max(0, board[nr][nc] - 20);
			}
		}
	}

	private static void moveRobot() {
		for (Robot robot : robots) {
			int startR = robot.r;
			int startC = robot.c;

			// 현재 칸에 먼지가 있으면 이동하지 않음
			if (board[startR][startC] > 0) {
				continue;
			}

			Node next = findNext(startR, startC); //다음 이동할 좌표 찾기

			// 갈 수 있는 먼지 칸이 없으면 제자리
			if (next == null) {
				continue;
			}

			rBoard[startR][startC] = 0;
			rBoard[next.r][next.c] = 1;

			robot.r = next.r;
			robot.c = next.c;
		}
	}

	private static Node findNext(int startR, int startC) {
		boolean[][] visited = new boolean[N][N];
		ArrayList<Node> target = new ArrayList<>();// 같은 깊이의 먼지 모음
		ArrayDeque<Node> que = new ArrayDeque<>();

		int minDepth = Integer.MAX_VALUE;

		que.add(new Node(startR, startC, 0));
		visited[startR][startC] = true;
		//bfs로 먼지 찾기
		while (!que.isEmpty()) {
			Node cur = que.poll();

			int r = cur.r;
			int c = cur.c;
			int depth = cur.depth;

			if (depth > minDepth) {
				continue;
			}

			for (int d = 0; d < 4; d++) {
				int nr = r + dr[d];
				int nc = c + dc[d];

				if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
					continue;
				}

				if (visited[nr][nc]) {
					continue;
				}

				// 물건 칸은 이동 불가
				if (board[nr][nc] == -1) {
					continue;
				}

				// 다른 로봇 칸은 이동 불가
				if (rBoard[nr][nc] == 1) {
					continue;
				}

				visited[nr][nc] = true;

				int nextDepth = depth + 1;

				if (board[nr][nc] > 0) {
					if (nextDepth <= minDepth) {
						minDepth = nextDepth;
						target.add(new Node(nr, nc, nextDepth));
					}
				}

				que.add(new Node(nr, nc, nextDepth));
			}
		}
		// 행,열 기준으로 정렬
		target.sort((o1, o2) -> {
			if (o1.r == o2.r) {
				return o1.c - o2.c;
			}
			return o1.r - o2.r;
		});

		if (target.isEmpty()) {
			return null;
		}

		return target.get(0);
	}

}
