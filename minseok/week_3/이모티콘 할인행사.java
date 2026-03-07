class Solution {
    static int[][] users;
    static int[] emoticons;
    static int[] answer = new int[2];
    static int best_join = 0;
    static int best_sum = 0;
    static int[] temp;
    static int[] sales = {10,20,30,40};
    public int[] solution(int[][] users, int[] emoticons) {
        this.users = users;
        this.emoticons = emoticons;
        temp = new int[emoticons.length];
        dfs(0);
        answer[0] = best_join;
        answer[1] = best_sum;
        return answer;
    }
    public static void dfs(int cnt){
        if(cnt==emoticons.length){
            // 이모티콘에 대한 가격을 책정한 상태
            int total_sum = 0;
            int total_join = 0;
            m : for(int i = 0;i<users.length;i++){
                int cur_sum = 0;
                int target_sale = users[i][0];
                int limit_sum = users[i][1];
                for(int j = 0;j<emoticons.length;j++){
                    if(temp[j]>=target_sale){
                        cur_sum+=(int)(emoticons[j]*(100-temp[j])/100);
                    }
                    if(cur_sum>=limit_sum){
                        total_join++;
                        continue m;
                    }
                }
                total_sum+=cur_sum;
            }
            if(total_join>best_join){
                best_join = total_join;
                best_sum = total_sum;
            }
            else if(total_join==best_join){
                best_sum = Math.max(best_sum, total_sum);
            }
            return;
        }
        for(int i = 0;i<4;i++){
            temp[cnt] = sales[i];
            dfs(cnt+1);
        }
    }
}