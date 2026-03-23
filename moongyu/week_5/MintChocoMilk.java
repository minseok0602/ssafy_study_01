package Day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class MintChocoMilk {
	private static final int[] dr = {-1, 1, 0, 0};
	private static final int[] dc = {0, 0, -1, 1};
	private static final int[] order = {7, 6, 5, 3, 1, 2, 4};	// 출력 매핑을 위한 순서 배열
	private static int N, T;
	private static char[][] F;
	private static int[][] B;
	private static Status[][] status;
	
	static class Status{
		int love;	// 신봉음식 : 111(민트초코우유), 110(민트초코), 101(민트우유), 011(초코우유), 001(우유), 010(초코), 100(민트)
		int level;
		int row, col;
		public Status(int love, int level, int row, int col) {
			this.love = love;
			this.level = level;
			this.row = row;
			this.col = col;
		}
	}
	
	private static int getComb(int love) {		// 단일 : 0, 이중 : 1, 삼중 : 2
		return Integer.bitCount(love) - 1;
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		StringBuilder sb = new StringBuilder();
		N = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		status = new Status[N][N];
		F = new char[N][N];
		B = new int[N][N];
		for (int i = 0; i < N; i++) {
			String line = br.readLine();
			for (int j = 0; j < N; j++) {
				F[i][j] = line.charAt(j);
			}
		}
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				B[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (F[i][j] == 'T') {
					status[i][j] = new Status(4, B[i][j], i, j);		// 민트 : 100
				} else if (F[i][j] == 'C') {
					status[i][j] = new Status(2, B[i][j], i, j);		// 초코 : 010
				} else if (F[i][j] == 'M') {
					status[i][j] = new Status(1, B[i][j], i, j);		// 우유 : 011
				}
			}
		}

		for (int time = 0; time < T; time++) {
			int[] memberSum = new int[8];
			// 1. 아침 시간 : 모든 학생은 신앙심 1씩 획득, 모든 B[i][j] += 1
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					status[i][j].level += 1;
				}
			}
			
			
			// 2. 점심 시간 : 학생들은 인접한 학생들과 신봉 음식이 완전히 같은 경우에만 그룹 형성
			PriorityQueue<Status> group = new PriorityQueue<>((o1, o2) -> {
				if (o1.level != o2.level) return o2.level - o1.level;
				if (o1.row != o2.row) return o1.row - o2.row;
				return o1.col - o2.col;
			});
			
			PriorityQueue<Status> representatives = new PriorityQueue<>((o1, o2) -> {
				int c1 = getComb(o1.love);
				int c2 = getComb(o2.love);
				if (c1 != c2) return c1 - c2;
				if (o1.level != o2.level) return o2.level - o1.level;
				if (o1.row != o2.row) return o1.row - o2.row;
				return o1.col - o2.col;
			});
			
			boolean[][] isGroup = new boolean[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (!isGroup[i][j]) {
						Queue<Status> queue = new ArrayDeque<>();
						queue.add(status[i][j]);
						isGroup[i][j] = true;
						group.add(status[i][j]);
						while(!queue.isEmpty()) {
							Status curNode = queue.poll();
							
							for (int d = 0; d < 4; d++) {
								int nr = curNode.row + dr[d];
								int nc = curNode.col + dc[d];
								
								if (nr < 0 || nr >= N || nc < 0 || nc >= N 
									|| isGroup[nr][nc] || status[nr][nc].love != curNode.love) continue;
								
								queue.add(status[nr][nc]);
								isGroup[nr][nc] = true;
								group.add(status[nr][nc]);
							}
						}

						for (Status status : group) {
							status.level -= 1;
						}
						
						group.peek().level += group.size();
						Status rep = group.poll();
						representatives.add(new Status(rep.love, rep.level, rep.row, rep.col));
						group.clear();
					}
				}
			}
			
			// 3. 저녁 시간 : 모든 그룹의 대표자들이 신앙을 전파
			
			boolean[][] defense = new boolean[N][N];

			while(!representatives.isEmpty()) {
				Status curRep = representatives.poll();
				
				if (defense[curRep.row][curRep.col]) continue;

				int goDir = curRep.level % 4;
				int please = curRep.level - 1;
				status[curRep.row][curRep.col].level = 1;
				
				int nr = curRep.row + dr[goDir];
				int nc = curRep.col + dc[goDir];
				
				while(please > 0) {
					if (nr < 0 || nr >= N || nc < 0 || nc >= N) break;
					
					if (curRep.love != status[nr][nc].love) {	// 전파자와 신봉 음식이 다른 경우 전파 진행
						
						defense[nr][nc] = true;

						if (please > status[nr][nc].level) {		// x > y 강한 전파 상황
							status[nr][nc].love = curRep.love;		// 전파자의 신봉음식과 동일 음식 신봉
							please -= (status[nr][nc].level + 1);	// 간절함은 (y + 1)만큼 감소
							status[nr][nc].level++;					// 전파 대상의 신앙심은 1 증가
						} 
						
						else {										// x <= y 약한 전파 상황									
							status[nr][nc].love |= curRep.love;
							status[nr][nc].level += please;			// 신앙심 x 만큼 증가
							please = 0;								// 간절함 0
						}
					}
					
					nr += dr[goDir];
					nc += dc[goDir];
				}
			}
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					memberSum[status[i][j].love] += status[i][j].level;
				}
			}
			
			for (int s : order) {
				sb.append(memberSum[s]).append(' ');
			}
			sb.append('\n');
		}
		System.out.println(sb);
	}
}