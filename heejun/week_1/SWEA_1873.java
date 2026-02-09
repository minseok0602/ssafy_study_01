package task.Day0206;

import java.io.*;
import java.util.*;

public class SWEA_1873 {
	static int H, W;
	static int now_i;// 현재위치
	static int now_j;// 현재 위치
	static int idx; // 방향을 나타냄 0,1,2,3 => 북동남서
	static int[] dr = { -1, 0, 1, 0 };
	static int[] dc = { 0, 1, 0, -1 };
	static char[][] board; // 게임판

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			H = Integer.parseInt(st.nextToken());
			W = Integer.parseInt(st.nextToken());

			board = new char[H][W]; // 게임판
			int[] map = new int[128];
			Arrays.fill(map, -1); // 초기화

			map['^'] = 0;
			map['>'] = 1;
			map['v'] = 2;
			map['<'] = 3;

			now_i = 0;
			now_j = 0;

			for (int i = 0; i < H; i++) { // 배열 값 넣기
				String one_line = br.readLine();
				for (int j = 0; j < W; j++) {
					char now = one_line.charAt(j);
					board[i][j] = now;
					if (now == '<' || now == '>' || now == '^' || now == 'v') {
						now_i = i;
						now_j = j;
						idx = map[now];
						board[i][j] = '.';
					}
				}
			}

			int N = Integer.parseInt(br.readLine());
			String commend = br.readLine();

			for (char c : commend.toCharArray()) {// 명령어 받기
				switch (c) {
				case 'U':
					idx = 0;// 전차 회전
					move(now_i, now_j);
					break;
				case 'D':
					idx = 2;
					move(now_i, now_j);
					break;
				case 'L':
					idx = 3;
					move(now_i, now_j);
					break;
				case 'R':
					idx = 1;
					move(now_i, now_j);
					break;
				case 'S':
					shoot(now_i, now_j);
					break;
				}

			}

			switch (idx) { // 마지막 위치에서 나의 모양 나타내기
			case 0:
				board[now_i][now_j] = '^';
				break;
			case 1:
				board[now_i][now_j] = '>';
				break;
			case 2:
				board[now_i][now_j] = 'v';
				break;
			case 3:
				board[now_i][now_j] = '<';
				break;
			}

			// 출력
			System.out.print("#" + test_case + " ");
			for (int i = 0; i < H; i++) {
				for (int j = 0; j < W; j++) {
					System.out.print(board[i][j]);
				}
				System.out.println();
			}

		}

	}

	private static void shoot(int i, int j) {// 현재 위치에서 바라보는 방향으로 인덱스를 증가시켜 벽과
												// 부딪히는지 확인
		int nr = now_i;
		int nc = now_j;
		switch (idx) {
		case 0:
			while (nr >= 0) {

				if (board[nr][nc] == '*') {
					board[nr][nc] = '.';
					break;
				} else if (board[nr][nc] == '#') {
					break;
				}
				nr--;
			}
			break;
		case 1:
			while (nc < W) {

				if (board[nr][nc] == '*') {
					board[nr][nc] = '.';
					break;
				} else if (board[nr][nc] == '#') {
					break;
				}
				nc++;
			}
			break;
		case 2:
			while (nr < H) {

				if (board[nr][nc] == '*') {
					board[nr][nc] = '.';
					break;
				} else if (board[nr][nc] == '#') {
					break;
				}
				nr++;
			}
			break;
		case 3:
			while (nc >= 0) {

				if (board[nr][nc] == '*') {
					board[nr][nc] = '.';
					break;
				} else if (board[nr][nc] == '#') {
					break;
				}
				nc--;
			}
			break;
		}

	}

	private static void move(int i, int j) { // 방향에 따라 앞으로 한칸 갈 수 있는지 체크
		//움직일 위치
		int nr = now_i + dr[idx];
		int nc = now_j + dc[idx];

		if (isValid(nr, nc) && board[nr][nc] == '.') { // 보드 안이고 평지여야 움직임
			now_i = nr;
			now_j = nc;

		}

	}

	private static boolean isValid(int i, int j) { // 보드판 범위안인가?
		if (i >= 0 && i < H && j >= 0 && j < W) {
			return true;
		}
		return false;
	}

}
