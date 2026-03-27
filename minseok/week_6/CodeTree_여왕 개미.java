import java.util.*;
import java.io.*;
public class Main {
    public static class House {
        int idx;
        int pos;
        public House(int idx, int pos){
            this.idx =idx;
            this.pos = pos;
        }
    }
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args)throws IOException {
        int t = Integer.parseInt(br.readLine());
        ArrayList<House> houses = new ArrayList<>();
        int idx = 1;
        for(int q = 0;q<t;q++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            switch(command){
                case 100:{
                    int count = Integer.parseInt(st.nextToken());
                    for(int i = 0;i<count;i++){
                        houses.add(new House(idx++, Integer.parseInt(st.nextToken())));
                    }
                    break;
                }
                case 200:{
                    houses.add(new House(idx++, Integer.parseInt(st.nextToken())));
                    break;
                }
                case 300:{
                    int target = Integer.parseInt(st.nextToken());
                    for(int i = 0;i<houses.size();i++){
                        if(houses.get(i).idx==target){
                            houses.remove(i);
                            break;
                        }
                    }
                    break;
                }
                case 400:{
                    // 이분 탐색으로 최적의 t를 찾음
                    // t의 범위는 1 ~ 10^9
                    // 만약에 개미 k 마리를 적절히 배치해서 x 시간만에 모든 개미집을 커버할 수 있으면
                    // x 시간보다 더 짧을 수 있는지 확인함
                    // 어떻게 k마리를 적절히 배치하냐?
                    // 일단 k를 1 줄이고, 시작 (1번 집부터 개미를 박아야하기 때문)
                    // remain을 mid로 초기화하고 시작
                    // cur_house -> next_house로 이동을 함 (단, remain이 이 거리만큼 남아있을 때만)
                    // remain이 이 거리만큼 안 남아있다면, 다음 집으로 이동할 수 없음
                    // k를 1 줄임 (새로운 개미를 박음)
                    // remain을 다시 time으로 초기화

                    int k = Integer.parseInt(st.nextToken());
                    int start = 0;
                    int end = 1000000000;
                    int result = Integer.MAX_VALUE;
                    while(start<=end){
                        int mid = (start+end)/2;
                        if(can_cover(houses, mid,k)){
                            end = mid - 1;
                            result = mid;
                        }
                        else{
                            start = mid + 1;
                        }
                    }
                    System.out.println(result);
                }
            }
        }
    }
    public static boolean can_cover(ArrayList<House> houses, int time, int k){
        // 개미는 1번집부터 배치
        // step : 1번집에 위치한 개미가 time초 뒤에 위치할 좌표
        int step = houses.get(0).pos + time;
        k--;
        int idx = 0;
        while(true){
            // 개미가 커버 가능한 집까지 이동
            while(idx<houses.size()&&houses.get(idx).pos<=step){
                idx++;
            }
            // 다음 개미 배치
            k--;
            // 아직 집이 남아있는데 개미 배치가 다 끝났으면 불통
            if(idx<houses.size()&&k<0)
                return false;
            // 모든 집을 다 커버했으면 통과
            if(idx==houses.size())
                break;
            // 개미를 새로 배치했으니, 커버 좌표를 갱신
            step = houses.get(idx).pos + time;
        }
        return true;
    }
}
