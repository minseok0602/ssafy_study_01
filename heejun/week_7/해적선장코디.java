package class_study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class 해적선장코디 {
	static int T;
	static int N;
	static class Boat{
		int id;
		int p;
		int r;
		boolean isready;
		public Boat(int id, int p, int r, boolean isready) {
			super();
			this.id = id;
			this.p = p;
			this.r = r;
			this.isready = isready;
		}

		
		
	}
	static class Time{
		int id;
		int r;
		public Time(int id, int r) {
			super();
			this.id = id;
			this.r = r;
		}
		
	}
	static PriorityQueue<Boat> ready;// 준비된 배 
	static HashMap<Integer, Boat> all; // 모든 배 관리
	static ArrayList<Time> table; // 대기하는 배 명단
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br =new BufferedReader(new  InputStreamReader(System.in));
		T= Integer.parseInt(br.readLine());
		ready = new PriorityQueue<>((a, b) -> {
		    if (a.p != b.p) return Integer.compare(b.p, a.p);
		    else return Integer.compare(a.id, b.id);
		});
		all=new HashMap<>();
		table=new ArrayList<>();
		for(int i=0;i<T;i++) {
			StringTokenizer st =new StringTokenizer(br.readLine());
			int cmd=Integer.parseInt(st.nextToken());
			
			// 명령 수행
			if(cmd==100) {
				N=Integer.parseInt(st.nextToken());
				for(int j=0;j<N;j++) {
					int id=Integer.parseInt(st.nextToken());
					int p=Integer.parseInt(st.nextToken());
					int r=Integer.parseInt(st.nextToken());
					Boat b =new  Boat(id,p,r,true);
					ready.add(b);
					all.put(id, b);
				}
			}
			else if(cmd==200) {// 배 추가
				int id=Integer.parseInt(st.nextToken());
				int p=Integer.parseInt(st.nextToken());
				int r=Integer.parseInt(st.nextToken());
				Boat b =new  Boat(id,p,r,true);
				ready.add(b);
				all.put(id, b);
			}
			else if(cmd==300) { // 공격력 교체
				int id=Integer.parseInt(st.nextToken());
				int pw=Integer.parseInt(st.nextToken());
				Boat b = all.get(id);
				if(b.isready) {
					ready.remove(b);
					b.p = pw;
					ready.add(b);
				}else {
					b.p=pw;
				}
				
				
			}
			else { //발사
				int n =ready.size();
				int sum=0;
				int cnt=0;
				ArrayList<Integer> ids=new ArrayList<>();
				for(int j=0;j<n;j++) {
					if(j==5)break;
					Boat cur =ready.poll();
					sum+=cur.p;
					cnt++;
					ids.add(cur.id);
					cur.isready=false;
					table.add(new Time(cur.id,cur.r));
				}
				System.out.printf("%d %d ",sum,cnt);
				for(int id : ids) {
					System.out.print(id+" ");
				}
				System.out.println();
			}
			
			//시간 체크
			int len=table.size();
			for(int j=len-1;j>=0;j--) {
				Time t=table.get(j);
				if(t.r-1==0) {
					ready.add(all.get(t.id));
					all.get(t.id).isready=true;
					table.remove(j);
				}
				else {
					t.r--;
				}
			}
			
		}
		
	}

}
