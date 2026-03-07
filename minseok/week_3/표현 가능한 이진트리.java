class Solution {
    static String tree;
    public int[] solution(long[] numbers) {
        String[]binaries = new String[numbers.length];
        int[] answer = new int[numbers.length];
        for(int i = 0;i<numbers.length;i++){
            StringBuilder cur_binary = new StringBuilder(Long.toBinaryString(numbers[i]));
            // 여기서 패딩을 해줘야함
            // 해당 바이너리의 깊이에 포화 개수 만큼을 채워줘야함
            int interval = 0;
            int cur_cnt = cur_binary.length();
            int cur_full_cnt = 2;
            //cur_full_cnt-1는 높이별 포화이진트리의 노드 개수임
            //cur_full_cnt-1의 높이가 h라고 가정,
            //cur_full_cnt-1보다 cur_cnt가 더 크다는 것은, h로는 numbers[i]를 포화이진트리 상태로 만들 수 없다는 것
            while(cur_full_cnt-1<cur_cnt){
                cur_full_cnt*=2;
            }
            // 현재 cur_full_cnt-1은 cur_cnt보다 크거나 같은 상태 -> numbers[i]를 포화이진트리로 만드려면 최소 cur_full_cnt-1만큼의 문자열은 되어야 판별 가능 -> 문자열의 앞에 의미없는 0을 패딩
            interval = cur_full_cnt-1-cur_cnt;
            StringBuilder sb = new StringBuilder();
            for(int j = 0;j<interval;j++){
                sb.append("0");
            }
            sb.append(cur_binary);
            tree = sb.toString();
            answer[i] = check(0,sb.length(),false)?1:0;
        }
        return answer;
    }
    // 루트부터 리프까지 이동하면서 is_dummy 활용
    // is_dummy값이 true : 루트에서 본인까지 오면서, 0인 노드가 있다.
    // is_dummy값이 false : 본인의 부모가 1이다.
    // check 메서드의 역할 : 해당 트리가 표현 가능한 이진트리인지 확인
    public static boolean check(int start, int end, boolean is_dummy){
        // 기저
        if(end-start==1){
            // 마지막까지 갔을 때, 부모에 1이 있으면, 리프가 0이든 1이든 상관 없음
            // 부모가 0이면, 본인도 무조건 0이어야함
            return !is_dummy||tree.charAt(start)=='0';
        }
        // 현재 tree의 루트 노드
        int mid = (start + end) / 2;
        boolean cur_flag = tree.charAt(mid)=='0';
        // 본인의 부모들 중에 0이 있음
        // 본인 + 본인의 서브 트리가 모두 0이어야함
        if(is_dummy){
            return cur_flag&&check(start,mid,is_dummy)&&check(mid+1,end,is_dummy);
        }
        // 본인의 부모들 중에 1이 있음
        // 서브 트리로 내려가서 체크 진행
        return check(start,mid,cur_flag)&&check(mid+1,end,cur_flag);
    }
}