import java.io.*;
import java.util.*;

class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for(int i=0;i<=n;i++) graph.add(new ArrayList<>());

        // 본인으로 들어오는 노드의 개수를 셈
        // a->b : a가 b보다 앞서야 한다.
        // indegree[i] : 순서상 본인 앞에 와야하는 노드의 개수
        int[] indegree = new int[n+1];

        for(int i=0;i<m;i++){
            st = new StringTokenizer(br.readLine());
            int cnt = Integer.parseInt(st.nextToken());
            if(cnt == 0) continue;

            int prev = Integer.parseInt(st.nextToken());
            for(int j=1;j<cnt;j++){
                int next = Integer.parseInt(st.nextToken());
                graph.get(prev).add(next);
                indegree[next]++;
                prev = next;
            }
        }

        Queue<Integer> q = new LinkedList<>();
        for(int i=1;i<=n;i++){
            // 본인보다 앞서야하는 노드의 개수가 0 -> 본인이 제일 먼저 나가야함
            if(indegree[i]==0) q.add(i);
        }

        StringBuilder sb = new StringBuilder();
        // 정렬에 성공해서 꺼낸 노드의 개수
        int count = 0;

        while(!q.isEmpty()){
            int cur = q.poll();
            sb.append(cur).append("\n");
            count++;

            //graph.get(cur) : cur 뒤에 와야 하는 노드들의 리스트
            for(int i : graph.get(cur)){
                // i : cur 뒤에 와야 하는 노드 중 하나
                // i보다 앞서야 하는 노드들 중에 cur이 포함되어 있을 것
                indegree[i]--;
                // indegree가 0이 됨 -> 본인보다 더 앞서야 하는 노드가 이제 없음
                if(indegree[i]==0)
                    q.add(i);
            }
        }
        // 사이클이 발생하면, indegree가 0이 되는 상황이 안 오는 노드가 발생한다.
        // 사이클이 발생했다는 건, 서로가 서로의 indegree를 보장해주고 있다는 뜻
        if(count != n) System.out.println(0);  // 사이클
        else System.out.print(sb);
    }
}
