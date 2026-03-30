import java.util.*;
import java.io.*;
public class CodeTree {
    static class Micro {
    	// 미생물의 고유 번호
        int idx;
        
        // 미생물을 구성하는 셀들의 좌표 정보
        ArrayList<int[]> arr;
        public Micro(int idx, ArrayList<int[]>arr) {
            this.idx = idx;
            this.arr = arr;
        }
    }
    static int[][] dir = {{0,1},{1,0},{-1,0},{0,-1}};
    static int n,t;
    static int[][] map;
    static int[][] new_map;
    static boolean[][] visit;
    static ArrayList<Micro> micros;
    static HashMap<Integer,Micro> micro_map = new HashMap<>();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringBuilder sb = new StringBuilder();
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        map = new int[n][n];
        for(int i = 1;i<=t;i++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken())-1;
            int c2 = Integer.parseInt(st.nextToken())-1;
            for(int j = r1;j<=r2;j++) {
                for(int k = c1;k<=c2;k++) {
                    map[j][k] = i;
                }
            }
            // 맵을 보고 군집을 파악해서 미생물 객체 생성
            make_micros();
            
            // 새로운 배양용기 생성
            new_map = new int[n][n];
            
            micro_map = new HashMap<>();
            
            
            // 가능한 원점에 가깝도록 배치
            push();
            
            // 새로운 배양 용기를 현재 배양용기로 설정
            map = new_map;
            visit = new boolean[n][n];
            int score = 0;
            
            // 만약에 제일 첫 턴이라면, 인근에 아무 미생물도 없을 것이기 때문에 그냥 pass
            if(i==1) {
            	sb.append(score).append("\n");
            	continue;
            }
            // 점수 계산
            for(int j = 0;j<n;j++){
                for(int k =0;k<n;k++){
                	// 새로운 군집을 발견
                    if(!visit[j][k]&&map[j][k]!=0){
                    	// 인근에 다른 종류의 미생물이 있는지 확인
                        score+=calculate_score(map[j][k],j,k);
                    }
                }
            }
            sb.append(score).append("\n");


        }
        System.out.println(sb);
    }
    // 군집을 보고 미생물 객체를 생성
    private static void make_micros() {
    	// 이미 한번 확인한 적이 있는 미생물인지 확인
        boolean[] checked = new boolean[n*n*t+1];
        
        // 이미 한번 제거된 미생물인지 확인 -> 반으로 갈라진 미생물이 있을 때, ArrayList에서 이 정보를 제거할 건데, 반으로 갈라지면 동일한 idx의 미생물이 2개의 군집으로 나누어지기 때문에 deleted 처리를 함으로써, 없는 idx를 ArrayList에서 제거하지 않도록 함
        boolean[] deleted = new boolean[n*n+t+1];
        visit = new boolean[n][n];
        micros = new ArrayList<>();
        for(int i = 0;i<n;i++) {
            for(int j = 0;j<n;j++) {
                if(map[i][j]>0&&!visit[i][j]) {
                    // 이미 해당 index를 앞에서 처리한 적이 있는데도 방문한 적이 없다 -> 새로운 군집이긴 하지만, 이미 앞에서 처리를 했던 군집이다.
                    if(checked[map[i][j]]) {
                    	// 만약에 이미 삭제된 미생물이면, 중복으로 제거하면 안됨 -> 그냥 넘어감
                        if(deleted[map[i][j]])
                            continue;
                        // 만약에 아직 삭제가 되지 않았으면, 이 미생물을 ArrayList에서 삭제
                        micros.remove(micro_map.get(map[i][j]));
                        deleted[map[i][j]] = true;
                        continue;
                    }
                    visit[i][j] = true;
                    checked[map[i][j]] = true;
                    // 여기까지 왔으면, 아직 탐사해보지 않은 미생물
                    Micro m = new Micro(map[i][j],bfs(map[i][j],i,j));
                    micros.add(m);
                    micro_map.put(map[i][j], m);
                }
            }
        }
        // 미생물을 구성하는 셀을 x가 작은 순으로 정렬
        for (Micro micro : micros) {
            micro.arr.sort(Comparator.comparingInt(o -> o[0]));
        }
        // 미생물들을 사이즈 크기 기준으로 정렬, 만약 사이즈가 같으면 idx 기준 정렬
        micros.sort((o1, o2) -> {
            if (o1.arr.size() == o2.arr.size()) {
                return o1.idx - o2.idx;
            }
            return o2.arr.size() - o1.arr.size();
        });

    }
    // 군집을 확인하는 함수
    static ArrayList<int[]> bfs(int code, int x, int y) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {x,y});
        ArrayList<int[]> temp = new ArrayList<>();
        temp.add(new int[] {x,y});
        visit[x][y] = true;
        while(!queue.isEmpty()) {
            int[]cur = queue.poll();
            for(int i = 0;i<4;i++) {
                int nx = cur[0] + dir[i][0];
                int ny = cur[1] + dir[i][1];
                if(nx<0||ny<0||nx>=n||ny>=n) continue;
                if(visit[nx][ny]) continue;
                if(map[nx][ny]!=code) continue;
                visit[nx][ny] = true;
                temp.add(new int[] {nx,ny});
                queue.add(new int[] {nx,ny});
            }
        }
        return temp;
    }
    
    // 0,0 ~ n,n까지 이동을 시켜보는 함수
    static void push() {
        m : for (Micro m : micros) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                	// {j,k}는 타겟 좌표 : micros의 각 arr의 기준 좌표(제일 앞의 좌표)를 0,0 ~ n,n까지 이동시켜봄 
                	int cnt = 0;
                	ArrayList<int[]> temp = new ArrayList<>();
                	int move_x = m.arr.get(0)[0] - j;
                	int move_y = m.arr.get(0)[1] - k;
                    for (int[] c : m.arr) {
                        int x = c[0] - move_x;
                        int y = c[1] - move_y;
                        if (x < 0 || y < 0 || x >= n || y >= n || new_map[x][y]!=0) {
                        	// 이동시키려고 하는데, 맵 밖이거나 이미 다른 종류의 셀이 자리잡고 있으면 안됨
                            break;
                        }
                        // 그렇지 않다면 temp에다가 해당 좌표를 넣고, 셀 저장 개수 증가
                        cnt++;
                        temp.add(new int[]{x, y});
                    }
                    // 만약에 arr의 크기만큼 cnt가 증가했다 -> 해당 미생물의 이동이 완료되었다.
                    if (cnt==m.arr.size()) {
                        for (int[] a : temp) {
                        	// 새 배양 용기에 미생물을 최종 저장하고 미생물의 셀 좌표 정보를 갱신
                            new_map[a[0]][a[1]] = m.idx;
                            m.arr = temp;
                            micro_map.put(m.idx,m);
                        }
                        continue m;
                    }
                }
            }
        }
    }
    // idx번 미생물의 주변에 다른 미생물이 있는지 확인
    private static int calculate_score(int idx, int x, int y){
        Queue<int[]> queue = new ArrayDeque<>();
        
        // 주변에 다른 미생물이 있는지를 확인하되, 한쌍만 발견해도 score를 갱신하고, 중복으로 갱신하면 안됨
        boolean[] checked = new boolean[n*n*t+1];
        checked[idx] = true;
        visit[x][y] = true;
        queue.add(new int[]{x,y});
        int sum = 0;
        while(!queue.isEmpty()){
            int[] cur = queue.poll();
            for(int i = 0;i<4;i++){
                int nx = cur[0] + dir[i][0];
                int ny = cur[1] + dir[i][1];
                if(nx<0||ny<0||nx>=n||ny>=n) continue;
                if(map[nx][ny]==0) continue;
                if(visit[nx][ny]) continue;
                // 만약에 종류가 다른 미생물을 발견하면, 큐에는 넣지 않지만 점수 계산 진행
                if(map[nx][ny]!=idx){
                	// 만약에 주변 미생물들을 탐색했는데 아직 score에 반영 안한 셀이면 score에 반영
                	if(!checked[map[nx][ny]]) {
                		checked[map[nx][ny]] = true;
                        sum+=micro_map.get(idx).arr.size()*micro_map.get(map[nx][ny]).arr.size();
                	}
                    continue;
                }
                // 같은 군집의 셀에 대해서는 다른 종류의 미생물을 찾으러 큐에 넣어야함
                visit[nx][ny] = true;
                queue.add(new int[]{nx,ny});
            }
        }
        return sum;
    }
}