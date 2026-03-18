import java.util.*;
class Solution {
    // 각 파이프 번호마다, 해당 파이프로 직접 연결되어있는 배양체 쌍들을 저장
    static HashMap<Integer,ArrayList<int[]>> pipe_map;
    static int answer = 1;
    int k;
    int n;
    public int solution(int n, int infection, int[][] edges, int k) {
        pipe_map = new HashMap<>();
        this.k = k;
        this.n = n;
        boolean[] is_infect = new boolean[n+1];
        is_infect[infection] = true;

        
        
        for(int i =0 ;i<edges.length;i++){
            if(!pipe_map.containsKey(edges[i][2])){
                pipe_map.put(edges[i][2],new ArrayList<>());
            }
            pipe_map.get(edges[i][2]).add(new int[]{edges[i][0],edges[i][1]});
            pipe_map.get(edges[i][2]).add(new int[]{edges[i][1],edges[i][0]});
        }
        dfs(0,1,is_infect);
        return answer;
    }
    // dfs로는, 총 k번 파이프를 열어보는 모든 경우의 수를 계산
    public void dfs(int depth,int sum,boolean[] is_infect){
        if(answer==n)
            return;
        answer = Math.max(answer,sum);
        if(depth==k){
            return;
        }
        // 파이프를 여는 경우의 수는 중복 순열 (순서 상관 있음)
        // 여기서 i는 파이프 번호
        for(int i = 1; i<=3;i++){
            // 해당 파이프가 없으면 그냥 건너뜀
            if(!pipe_map.containsKey(i))
                continue;
            boolean[]temp = new boolean[n+1];
            // is_infect에는, 현재 감염된 배양체들의 정보가 들어있음
            // 원복을 하기 까다로우니까 그냥 배열 하나 복사해서 상태 변경
            System.arraycopy(is_infect,0,temp,0,n+1);
            int cur_infect = bfs(i,temp);
            dfs(depth+1,sum+cur_infect,temp);
        }
        
    }
    // 특정 파이프를 열면 감염되는 수를 계산
    public int bfs(int pipe_num, boolean[] is_infect){
        Queue<Integer> queue = new ArrayDeque<>();
        // 지금 감염된 애들을 큐에 넣고 시작 -> 이 감염된 애들이 다른 배양체들을 감염시킬 거임
        for(int i = 1;i<=n;i++){
            if(is_infect[i]){
                queue.add(i);
            }
        }
        // 이번에 새로 감염되는 배양체의 수
        int cnt = 0;
        while(!queue.isEmpty()){
            // 이번에 꺼낸 감염체
            int cur_node = queue.poll();
            // 이번에 연 파이프들을 살펴봄
            for(int[] arr : pipe_map.get(pipe_num)){
                // 이번에 연 파이프의 배양체쌍의 한쪽이 감염체라면, 그 파이프로 직접 연결된 배양체를 감염시킴
                if(arr[0]==cur_node||arr[1]==cur_node){
                    int next_infect = arr[0]==cur_node?arr[1]:arr[0];
                    if(!is_infect[next_infect]){
                        is_infect[next_infect] = true;
                        // 감염된 새로운 배양체가, 지금 열린 파이프를 타고 또 다른 배양체를 감염시킬 수 있음
                        // ex) a--b--c 일 때, a-b 파이프랑 b-c 파이프의 종류가 모두 같은 상황에서, a가 b를 감염시킴으로써, b가 c도 바로 감염시킬 수 있음
                        queue.add(next_infect);
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }
}