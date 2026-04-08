import java.util.*;
import java.io.*;
public class CodeTree1 {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringBuilder sb = new StringBuilder();
	static class Deck implements Comparable<Deck>{
		int idx;
		int power;
		int reload;
		int ready_time;
		int update_time;
		public Deck(int idx, int power, int reload,int time) {
			this.idx = idx;
			this.power = power;
			this.reload = reload;
			this.update_time = time;
			ready_time = time;
		}
		@Override
		public int compareTo(Deck o) {
			if(this.power==o.power) {
				return this.idx - o.idx;
			}
			return o.power - this.power;
		}
	}
	// 선박의 인덱스로, 바로 해당 인덱스의 선박 정보를 가져오기 위한 맵
	static HashMap<Integer,Deck> deck_map = new HashMap<>();
	
	// 특정 인덱스에 해당하는 덱의 업데이트 시간을 알기 위한 맵
	static HashMap<Integer,Integer> update_map = new HashMap<>();
	public static void main(String[] args) throws IOException {
        int n = Integer.parseInt(br.readLine());
        PriorityQueue<Deck> decks = new PriorityQueue<>();
        for(int c = 0;c<n;c++) {
        	StringTokenizer st =new StringTokenizer(br.readLine());
        	int command = Integer.parseInt(st.nextToken());
        	switch(command) {
	        	case 100:{
	        		int cnt = Integer.parseInt(st.nextToken());
	        		for(int i = 0;i<cnt;i++) {
	        			int idx = Integer.parseInt(st.nextToken());
	        			int power = Integer.parseInt(st.nextToken());
	        			int reload = Integer.parseInt(st.nextToken());
	        			Deck deck = new Deck(idx, power, reload,c);
	        			// 덱 pq에다 넣고
	        			decks.add(deck);
	        			
	        			// 덱 맵에다가 갱신해주고
	        			deck_map.put(idx, deck);
	        			
	        			// 업데이트 시간은, 현재 시간으로 갱신
	        			update_map.put(idx, c);
	        		}
	        		break;
	        	}
	        	case 200:{
	        		int idx = Integer.parseInt(st.nextToken());
        			int power = Integer.parseInt(st.nextToken());
        			int reload = Integer.parseInt(st.nextToken());
        			Deck deck = new Deck(idx, power, reload,c);
        			// 새로운 선박 넣는 것도 똑같음
        			decks.add(deck);
        			deck_map.put(idx, deck);
        			update_map.put(idx, c);
	        		break;
	        	}
	        	case 300:{
	        		int idx = Integer.parseInt(st.nextToken());
	        		int power = Integer.parseInt(st.nextToken());
	        		// 수정은 이미 있는 것에 대해서만 하기 때문에, map에서 바로 해당 덱의 정보를 가져옴
	        		Deck deck = deck_map.get(idx);
	        		
	        		// 새로운 덱을 기존 정보를 이용해서 만듦
	        		Deck new_deck = new Deck(idx,power,deck.reload,c);
	        		
	        		// 덱 맵도 갱신
	        		deck_map.put(idx, new_deck);
	        		
	        		// pq에도 추가함
	        		decks.add(new_deck);
	        		
	        		//업데이트 맵도 갱신
	        		update_map.put(idx, c);
	        		break;
	        	}
	        	case 400:{
	        		// 누적 데미지
	        		int damage = 0;
	        		
	        		// 공격한 선박의 수
	        		int cnt = 0;
	        		StringBuilder answer = new StringBuilder();
	        		ArrayList<Deck> temp = new ArrayList<>();
	        		while(true) {
	        			// 선박이 더이상 없으면 탈출
	        			if(decks.isEmpty())
	        				break;
	        			
	        			// 공격한 선박의 수가 5개가 됐으면 탈출
	        			if(cnt==5)
	        				break;
	        			// 기본적으로 파워가 가장 높은 덱이 나옴
	        			Deck deck = decks.poll();
	        			// 특정 덱의 최근 업데이트 시간보다 현재 덱에서 꺼낸 업데이트 시간이 더 작다 -> 이 덱은 300에 의해 업데이트가 된 덱이다 -> 옛날 버전 덱이기 때문에 반영하면 안됨
	        			if(update_map.get(deck.idx)>deck.update_time) continue;
	        			
	        			
	        			// 만약 장전이 되었으면?
	        			if(deck.ready_time<=c) {
	        				// 지금 발사를 해야하니까, 지금 시각 + 재장전 시간을 반영해서 다음 준비 시간을 갱신
	        				deck.ready_time= c + deck.reload;
	        				// 누적 데미지 갱신
	        				damage+=deck.power;
	        				cnt++;
	        			    answer.append(deck.idx).append(" ");
	        			}
	        			temp.add(deck);
	        		}
	        		sb.append(damage).append(" ").append(cnt).append(" ");
	        		for(Deck d : temp)
	        			decks.add(d);
	        		sb.append(answer);
	        		sb.append("\n");
	        		break;
	        	}
        	}
        }
        System.out.println(sb);
    }
	
}
