class Solution {
    public int solution(int[][] signals) {
        int answer = 0;
        // 각 신호등이 완전히 같은 상태로 돌아오는 공배수 -> MAX 뒤에는 똑같이 반복된다.
        int max = (int)Math.pow(20,signals.length);
        int[] visit = new int[max];
        for(int i = 0;i<signals.length;i++){
            int[] cur_sig = signals[i];
            // 처음 노란불이 된 이후에, 다시 노란불이 되는데 걸리는 시간 -> 노란불 시간 + 빨간불 시간 + 초록불 시간
            int wait_time = cur_sig[0] + cur_sig[1] + cur_sig[2];
            // 처음 노란불이 되는 시간 (초항) -> 초록불 시간이 지나고 1초 뒤
            int first_y = cur_sig[0] +1;
            // 노란불이 되는 타이밍 ~ 노란불 지속시간 만큼 카운팅을 해주고, wait_time만큼 기다리고, 다시 카운트하는 걸 반복
            int t = 0;
            while(first_y+cur_sig[1]+(wait_time)*t<max){
                for(int j = 0;j<cur_sig[1];j++){
                    visit[first_y+j+(wait_time)*t]++;
                    // 만약에 총 신호등 개수만큼 카운트가 되었으면, 그 시간에 노란불이 전부 다 켜져있는 것
                    if(visit[first_y+j+(wait_time)*t]==signals.length)
                        return first_y+j+(wait_time)*t;
                }
                t++;
            }
        }
        return -1;
    }
}