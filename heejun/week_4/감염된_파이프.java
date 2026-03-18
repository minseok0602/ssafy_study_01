
import java.awt.event.AdjustmentListener;
import java.util.ArrayDeque;
import java.util.ArrayList;

class 감염된_파이프 {
	static int N,E,K;
	static class Node {
		int vertex;
		int type;
		public Node(int vertex, int type) {
			super();
			this.vertex = vertex;
			this.type = type;

		}
		
	}
	
	static ArrayList<Node>[] pipes;
	static int ans;
	public int solution(int n, int infection, int[][] edges, int k) {

		N=n; //정점수
		E=edges.length;//간선수
		K=k;
		ans=0;
		pipes=new ArrayList[N+1];// 그래프 표현
		for(int i=0;i<=N;i++) {
			pipes[i]=new ArrayList<>();
		}
		boolean[] isInfection=new boolean[N+1]; //현재 감염된 vertex 구분할 배열
		
		
		for(int i=0;i<E;i++) { // 그래프 만들기
			int start=edges[i][0];
			int end=edges[i][1];
			int type=edges[i][2];
			pipes[start].add(new Node(end,type));
			pipes[end].add(new Node(start,type));
		}
		
		isInfection[infection]=true; //처음 감염된 vertex처리
		com(0,isInfection);
	
        return ans;
    }
	private void com(int cnt,boolean[] isInfection) {
		if(cnt==K) {
			int count=0;
			for(int i=0;i<=N;i++) {
				if(isInfection[i])count++;
			}
			ans=Math.max(ans, count);
			return;
		}
		boolean[] copy;
		//A를 열때
		copy =simulation(1,isInfection);
		com(cnt+1,copy);
		//B를 열때
		copy =simulation(2,isInfection);
		com(cnt+1,copy);
		//C를 열때
		copy =simulation(3,isInfection);
		com(cnt+1,copy);
		
	}
	private boolean[] simulation(int pipe, boolean[] isInfection) {
		boolean[] copy=isInfection.clone();
		boolean[] visisted=new boolean[N+1];
		ArrayDeque<Integer> que =new ArrayDeque<>();
        
		for(int i=0;i<=N;i++) {// 감염된 vertex 큐에 넣기
			if(!isInfection[i])continue;
			que.add(i);
		}
		
		while(!que.isEmpty()) {
			int cur=que.poll();
			if(visisted[cur])continue;
			visisted[cur]=true;
			for(Node n:pipes[cur]){ //감염된 vertex와 연결된 vertex
				int vertex=n.vertex;
				int type=n.type;
				if(type==pipe) {// 현재 open한 pipe로 연결 돼 있으면
					copy[vertex]=true;// 감염
					que.add(vertex);
				}
				
			}
		}
		
		return copy;
	}
}