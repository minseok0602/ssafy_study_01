package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class 민트초코우유 {
	static int N,T;
	static class Human{
		int b; //신앙심
		int mint;
		int cho;
		int milk;
		boolean protect;
		public Human(int b, int mint, int cho, int milk) {
			super();
			this.b = b;
			this.mint = mint;
			this.cho = cho;
			this.milk = milk;
		}
		public Human() {
			super();
		}
		
	}
	static Human[][] board;
	static int[] dr= {-1,1,0,0}; //위 아래 왼쪽 오른쪽
	static int[] dc= {0,0,-1,1}; //위 아래 왼쪽 오른쪽
	
	static class position{
		int r;
		int c;
		int b;
		public position(int r, int c,int b) {
			super();
			this.r = r;
			this.c = c;
			this.b=b;
		}
	}
	static class king{
		int r;
		int c;
		int b;
		int mint;
		int cho;
		int milk;
		public king(int r, int c, int b, int mint, int cho, int milk) {
			super();
			this.r = r;
			this.c = c;
			this.b = b;
			this.mint = mint;
			this.cho = cho;
			this.milk = milk;
		}
		
	}
	
    public static void main(String[] args) throws IOException {
    	BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st= new StringTokenizer(br.readLine());
    	N=Integer.parseInt(st.nextToken());
    	T=Integer.parseInt(st.nextToken());
    	board=new Human[N][N];
    	for(int i=0;i<N;i++) {
    		for(int j=0;j<N;j++) {
    			board[i][j]=new Human();
    		}
    	}
    	//신봉 입력받기
    	for(int i=0;i<N;i++) {
    		String line=br.readLine();
    		for(int j=0;j<N;j++) {
    			char a=line.charAt(j);
    			if(a=='T') board[i][j].mint=1;
    			if(a=='C') board[i][j].cho=1;
    			if(a=='M') board[i][j].milk=1;
    		}
    	}
    	//신앙심 입력받기
    	for(int i=0;i<N;i++) {
    		st= new StringTokenizer(br.readLine());
    		for(int j=0;j<N;j++) {
    			board[i][j].b=Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	
    	
    	while(T-->0) {
    		//초기화
    		ArrayList<king> kings =new ArrayList<>();
    		for(int i=0;i<N;i++) {
        		for(int j=0;j<N;j++) {
        			board[i][j].protect=false;
        		}
        	}
    		long mintChoMilk = 0;
    		long mintCho = 0;
    		long mintMilk = 0;
    		long choMilk = 0;
    		long milkOnly = 0;
    		long choOnly = 0;
    		long mintOnly = 0;
    		
    		// 아침
    		for(int i=0;i<N;i++) {
        		for(int j=0;j<N;j++) {
        			board[i][j].b+=1;
        		}
        	}

   
    		// 점심
    		boolean[][] visited =new boolean[N][N];
    		for(int i=0;i<N;i++) {
        		for(int j=0;j<N;j++) {
        			if(visited[i][j])continue;
        			ArrayList<position> group =new ArrayList<>();
        			// 같은 그룹 찾기
        			ArrayDeque<position> que =new ArrayDeque<>();
        			group.add(new position(i, j, board[i][j].b));
        			que.add(new position(i, j,board[i][j].b));
        			visited[i][j]=true;
        			while(!que.isEmpty()) {
        				position cur =que.poll();
        				int r=cur.r;
        				int c=cur.c;
        				for(int d=0;d<4;d++) {
        					int nr=r+dr[d];
        					int nc=c+dc[d];
        					if(nr<0||nr>=N||nc<0||nc>=N)continue;
        					if(visited[nr][nc])continue;
        					if(board[r][c].cho==board[nr][nc].cho && board[r][c].mint==board[nr][nc].mint && board[r][c].milk==board[nr][nc].milk) {
        						group.add(new position(nr, nc, board[nr][nc].b));
        						que.add(new position(nr, nc, board[nr][nc].b));
        						visited[nr][nc]=true;
        					}
        				}
        			}
        			
        			// 같은 그룹의 대장 정하고 로직 수행
        			
        			//그룹의 사람 정렬
        			group.sort((o1, o2) -> {
        			    if (o1.b != o2.b) return o2.b - o1.b; // b 내림차순
        			    if (o1.r != o2.r) return o1.r - o2.r; // r 오름차순
        			    return o1.c - o2.c;                   // c 오름차순
        			});
        			
        			int size = group.size();
        			position first = group.get(0);

        			board[first.r][first.c].b += size - 1;
        			for (int idx = 1; idx < size; idx++) {
        			    position p = group.get(idx);
        			    board[p.r][p.c].b -= 1;
        			}

        			int kr = first.r;
        			int kc = first.c;
        			int kb = board[kr][kc].b;
        			int kmint = board[kr][kc].mint;
        			int kcho = board[kr][kc].cho;
        			int kmilk = board[kr][kc].milk;

        			kings.add(new king(kr, kc, kb, kmint, kcho, kmilk));
        			
        			
      
        		}
        	}
    		
    	
    		// 저녁
    		//대표자 순서 정하기
    		kings.sort((o1, o2) -> {
                int g1 = getGroup(o1);
                int g2 = getGroup(o2);

                if (g1 != g2) return g1 - g2;   // 그룹 순서
                if (o1.b != o2.b) return o2.b - o1.b; // 신앙심 큰 순
                if (o1.r != o2.r) return o1.r - o2.r; // 행 작은 순
                return o1.c - o2.c;                   // 열 작은 순
            });
    		
    		// 순서대로 전파
    		for(king k :kings) {
    			int r =k.r;
    			int c=k.c;
    			if(board[r][c].protect)continue;
    			int desperation =k.b-1;
    			int dir =k.b % 4;
    			propagation(r,c,dir,k.mint,k.cho,k.milk,desperation);
    			board[r][c].b=1;
    		}
    		
    		//출력
    		for (int i = 0; i < N; i++) {
    		    for (int j = 0; j < N; j++) {
    		        Human h = board[i][j];
    		        if (h == null) continue;

    		        if (h.mint == 1 && h.cho == 1 && h.milk == 1) {
    		            mintChoMilk += h.b;
    		        } else if (h.mint == 1 && h.cho == 1 && h.milk == 0) {
    		            mintCho += h.b;
    		        } else if (h.mint == 1 && h.cho == 0 && h.milk == 1) {
    		            mintMilk += h.b;
    		        } else if (h.mint == 0 && h.cho == 1 && h.milk == 1) {
    		            choMilk += h.b;
    		        } else if (h.mint == 0 && h.cho == 0 && h.milk == 1) {
    		            milkOnly += h.b;
    		        } else if (h.mint == 0 && h.cho == 1 && h.milk == 0) {
    		            choOnly += h.b;
    		        } else if (h.mint == 1 && h.cho == 0 && h.milk == 0) {
    		            mintOnly += h.b;
    		        }
    		    }
    		}

    		System.out.println(mintChoMilk + " " + mintCho + " " + mintMilk + " " +
    		                   choMilk + " " + milkOnly + " " + choOnly + " " + mintOnly);
    		
    	}
    	
    }
    private static void propagation(int r, int c, int dir, int mint, int cho, int milk,int desperation) {
		int cnt=0;
		int x = desperation;
    	while(true) {
    		cnt++;
			int nr=r+dr[dir]*cnt;
			int nc=c+dc[dir]*cnt;
			if(nr<0||nr>=N||nc<0||nc>=N)break;
			if(x<=0)break;
			if(check(nr,nc,r,c))continue;
			int y =board[nr][nc].b;
			
			
			if(x>y) {// 강한 전파
				board[nr][nc].mint=mint;
				board[nr][nc].cho=cho;
				board[nr][nc].milk=milk;
				x-=y+1;
				board[nr][nc].b+=1;
				board[nr][nc].protect = true;
				if(x==0)break;
			}
			else {// 약한 전파
				board[nr][nc].mint|=mint;
				board[nr][nc].cho|=cho;
				board[nr][nc].milk|=milk;
				board[nr][nc].b+=x;
				board[nr][nc].protect = true;
				x=0;
				break;
			}
			
		}
		
	}
	private static boolean check(int nr, int nc, int r, int c) {
		if(board[nr][nc].milk!=board[r][c].milk||board[nr][nc].mint!=board[r][c].mint||board[nr][nc].cho!=board[r][c].cho) return false;
		return true;
	}
	static int getGroup(king k) {
	    int cnt = k.mint + k.cho + k.milk;

	    if (cnt == 1) return 1; // 단일 음식
	    if (cnt == 2) return 2; // 이중 조합
	    return 3;               // 삼중 조합
	}
    
}
