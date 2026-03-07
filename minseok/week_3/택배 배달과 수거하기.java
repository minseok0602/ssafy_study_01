import java.util.*;
class Solution {
    public long solution(int cap, int n, int[] deliveries, int[] pickups) {
        // 멀리까지 가면서 배달을 하고, 오는 길에 수거하기
        Stack<int[]> del_stack = new Stack<>();
        Stack<int[]> pick_stack = new Stack<>();
        for(int i = 0;i<n;i++){
            if(deliveries[i]!=0)
                del_stack.push(new int[]{i+1,deliveries[i]});
            if(pickups[i]!=0)
                pick_stack.push(new int[]{i+1,pickups[i]});
        }
        long answer = 0;
        while(!del_stack.isEmpty()||!pick_stack.isEmpty()){
            // 처음에 들고 가는 박스
            int cnt = cap;
            // 만약 배달할 박스가 없으면?
            if(del_stack.isEmpty()){
                // 수거할 것이 있는 가장 먼 집으로 감
                answer += 2*pick_stack.peek()[0];
            }
            // 만약 수거할 박스가 없으면?
            else if(pick_stack.isEmpty()){
                // 배달할 것이 있는 가장 먼 집으로 감
                answer += 2*del_stack.peek()[0];
            }
            // 수거랑 배달이랑 모두할 게 남아있다면, 둘 중에 더 먼곳으로 감
            else{
                answer += 2*Math.max(pick_stack.peek()[0],del_stack.peek()[0]);
            }
            // 가능한 제일 먼 집으로 가서 배달을 하고, 여유가 있다면 그 앞집 것도 배달해줌
            // 어차피 제일 먼 집 가는 길에 그 앞집도 있으니까 가능
            while(!del_stack.isEmpty()){
                if(cnt==0)
                    break;
                int[] target_del = del_stack.pop();
                if(target_del[1]>cnt){
                    target_del[1]-=cnt;
                    del_stack.push(target_del);
                    cnt = 0;
                }
                else{
                    cnt-=target_del[1];
                }
            }
            // del_stack에서 배달을 할 집이 있든 없든, 일단 지금은 무조건 빈손임
            // 다시 공장으로 되돌아가면서 택배를 수거하는데, 수거하는 것도 공장에서부터 제일 먼 거리의 집부터 차례대로 수거
            // 수거하는 것도 최대 cap 만큼 수거 가능하기 때문에 cnt를 cap으로 초기화
            cnt = cap;
            while(!pick_stack.isEmpty()){
                if(cnt==0)
                    break;
                int[] target_pick = pick_stack.pop();
                if(target_pick [1]>cnt){
                    target_pick [1]-=cnt;
                    pick_stack.push(target_pick);
                    cnt = 0;
                }
                else{
                    cnt-=target_pick[1];
                }
            }
        }
        return answer;
    }
}