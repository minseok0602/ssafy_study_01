import java.io.*;
import java.util.*;

public class MicroOrganism{
	private static final int[] dc = {0, 0, -1, 1};
	private static final int[] dr = {-1, 1, 0, 0};
	

    static int N, Q;
    static int[][] map;
    
    // i번째 id를 갖고 있는 미생물 무리가 차지하고 있는 좌표를 저장하는 변수
    // p[0] : row좌표, p[1] : col 좌표
    static ArrayList<int[]>[] groups;
    
    // i번째 그룹이 살아있는지 여부 -> 짤리면 사라짐.
    static boolean[] alive;
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        groups = new ArrayList[Q + 1];
        alive = new boolean[Q + 1];
        for (int i = 1; i <= Q; i++) groups[i] = new ArrayList<>();

        // Q번의 실험 반복
        for (int i = 1; i <= Q; i++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            
            // 1. 미생물 투입 단계 - i번째 미생물 그룹이 투입됨
            addOrganism(r1, c1, r2, c2, i);
            
            // 2. 배양 용기 이동 단계
            moveAll();
            
            // 3. 실험 결과 기록 단계
            int res = score();
            
            sb.append(res).append('\n');
        }

        System.out.print(sb);
    }
    
    // 미생물 투입 함수
    static void addOrganism(int r1, int c1, int r2, int c2, int id) {
    	
    	// 1-1. 새롭게 투입되는 미생물 그룹의 자리에 있는 미생물 그룹 전처리 과정 
    	HashSet<Integer> set = new HashSet<>();			// 현재 투입되는 미생물 그룹과 겹치는 그룹을 저장
    	for (int r = r1; r < r2; r++) {
    		for (int c = c1; c < c2; c++) {
    			if (map[r][c] != 0) set.add(map[r][c]);
    		}
    	}
    	
    	for (int groupIdx : set) {
    		if (!alive[groupIdx]) continue;				// 없어진 미생물 그룹이라면 Skip
    		
    		// 새롭게 투입된 미생물 그룹과 겹치지 않는 점들을 저장
    		ArrayList<int[]> next = new ArrayList<>();	
    		for (int[] p : groups[groupIdx]) {
    			// 새로 투입되는 미생물 그룹의 크기 범위 내에 있지 않다면 추가
    			if (!(r1 <= p[0] && p[0] < r2 && c1 <= p[1] && p[1] < c2)) next.add(new int[] {p[0], p[1]});
    		}
    		
    		// 기존에 있던 미생물 그룹들의 칸들을 비워 놓음
    		// 죽는다면 그대로 비워두고, 산다면 다시 채워넣음
    		for (int[] p : groups[groupIdx]) map[p[0]][p[1]] = 0;
    		
    		// next.isEmpty()? -> 새로 들어오는 미생물 그룹에게 완전 덮여버린 경우
    		// comp(next) -> 겹쳐지지 않은 점들이 쪼개졌는지 확인 
    		if (next.isEmpty() || comp(next) >= 2) {
    			alive[groupIdx] = false;
    			groups[groupIdx].clear();
    		} 
    		
    		// 문제 없으면 다시 해당 index로 채워주고, groups에 해당 미생물 그룹 업데이트 해줌.
    		else {
    			groups[groupIdx] = next;
    			for (int[] p : next) map[p[0]][p[1]] = groupIdx;
    		}
    	}
    	
    	
    	// 1-2. 새롭게 투입되는 미생물 그룹 투입
    	alive[id] = true;
    	
    	for (int r = r1; r < r2; r++) {
    		for (int c = c1; c < c2; c++) {
    			map[r][c] = id;
    			groups[id].add(new int[] {r, c});
    		}
    	}
    }
    
    // 겹쳐지지 않은 점들이 나누어졌는지 확인하는 함수 : 그룹이 몇개로 나뉘어지는지 확인 (BFS)
    static int comp(ArrayList<int[]> list) {
        boolean[][] visited = new boolean[N][N];
        Queue<int[]> q = new ArrayDeque<>();
        int cnt = 0;

        for (int[] point : list) {
        	// 이미 방문했다면 skip
            if (visited[point[0]][point[1]]) continue;

            cnt++;
            visited[point[0]][point[1]] = true;
            q.add(new int[] {point[0], point[1]});

            while (!q.isEmpty()) {
                int[] cur = q.poll();

                for (int d = 0; d < 4; d++) {
                    int nr = cur[0] + dr[d];
                    int nc = cur[1] + dc[d];

                    // list 안에 있는 좌표인지 확인
                    for (int[] p : list) {
                        if (!visited[p[0]][p[1]] && p[0] == nr && p[1] == nc) {
                            visited[p[0]][p[1]] = true;
                            q.add(new int[] {nr, nc});
                        }
                    }
                }
            }
        }

        return cnt;
    }
    
    // 배양 용기 이동 함수
    static void moveAll() {
    	// 살아있는 그룹 인덱스를 저장하는 변수
    	ArrayList<Integer> order = new ArrayList<>();
    	for (int i = 1; i <= Q; i++) {
    		if (alive[i]) order.add(i);
    	}
    	
    	// 새로운 배양 용기에 투입 시, 우선순위에 따라 정렬
    	// 1. 크기
    	// 2. 크기가 같다면 먼저 투입된 순서
    	Collections.sort(order, (a, b) -> {
    		if (groups[a].size() != groups[b].size()) return groups[b].size() - groups[a].size();
    		return a - b;
    	});
    	
    	// 새로 옮겨담을 2차원 배열
    	int[][] nextMap = new int[N][N];
    	
    	for (int id : order) {
    		ArrayList<int[]> cur = groups[id];
    		// 미생물 그룹은 원래 직사각형 형태였으므로, 왼쪽 아래의 좌표를 기준으로 상대 좌표 변환
    		int minR = N + 1, minC = N + 1;
    		for (int[] p : cur) {
    			minR = Math.min(minR, p[0]);
    			minC = Math.min(minC, p[1]);
    		}
    		
    		// 왼쪽 아래를 (0, 0) 기준으로 변환해서 저장
    		ArrayList<int[]> shape = new ArrayList<>();
    		for (int[] p : cur) shape.add(new int[] {p[0] - minR, p[1] - minC});
    		
    		
    		// 해당 미생물 그룹은 놓을 수 있는지 없는지 나타냄
    		boolean canLie = false;
    		
    		// r이 가장 작고, c가 가장 작은 위치 부터 검사
    		for (int r = 0; r < N && !canLie; r++) {
    			for (int c = 0; c < N && !canLie; c++) {
    				
    				// 현재 r, c 위치에 지금 미생물 그룹을 놓을 수 있는지 나타냄
    				boolean can = true;
    				for (int[] p : shape) {
    					int nr = r + p[0];
    					int nc = c + p[1];
    					
    					// 범위를 벗어나거나, 이미 다른 미생물 그룹이 존재
    					if (nr < 0 || nr >= N || nc < 0 || nc >= N || nextMap[nr][nc] != 0) {
    						can = false;
    						break;
    					}
    				}
    				
    				// 해당 위치에는 놓지 못함
    				if (!can) continue;
    				
    				// 배치 시작
    				ArrayList<int[]> placed = new ArrayList<>();
    				for (int[] p : shape) {
    					int nr = r + p[0];
    					int nc = c + p[1];
    					nextMap[nr][nc] = id;
    					placed.add(new int[]{nr, nc});
    				}
    				
    				// 해당 그룹의 포인트들 업데이트 시켜줌
    				groups[id] = placed;
    				canLie = true;
    			}
    		}
    		
    		// 모든 위치를 탐색했지만 놓을 수 없다면 삭제
    		if (!canLie) {
    			alive[id] = false;
    			groups[id].clear();
    		}
    	}
    	
    	// 기존 map 업데이트 시킴.
    	map = nextMap;
    }
    
    // 실험 결과 기록 함수
    static int score() {
    	// 그룹끼리 연결되어져 있는지 저장하는 2차원 배열
        boolean[][] isConnect = new boolean[Q + 1][Q + 1];

        // 인접한 그룹 쌍 기록
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
            	// 빈 칸이면 skip
                if (map[r][c] == 0) continue;
                
                // 기준이 되는 그룹
                int a = map[r][c];
                
                // a의 아래와 오른쪽을 확인한다는건 b의 위와 왼쪽을 확인하는 것과 동일
                // 1. a 그룹의 아래 확인
                if (r + 1 < N) {
                    int b = map[r + 1][c];
                    if (b != 0 && a != b) {
                        isConnect[a][b] = true;
                        isConnect[b][a] = true;
                    }
                }

                // 2. a 그룹의 오른쪽 확인
                if (c + 1 < N) {
                    int b = map[r][c + 1];
                    if (b != 0 && a != b) {
                        isConnect[a][b] = true;
                        isConnect[b][a] = true;
                    }
                }
            }
        }

        int ans = 0;

        // 중복 없이 (a < b)만 계산
        for (int a = 1; a <= Q; a++) {
            if (!alive[a]) continue;

            for (int b = a + 1; b <= Q; b++) {
                if (!alive[b]) continue;

                if (isConnect[a][b]) {
                    ans += groups[a].size() * groups[b].size();
                }
            }
        }

        return ans;
    }
}