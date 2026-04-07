import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class 택배하차 {
    static int[][] map;
    static class Box implements Comparable<Box>{
        int idx; // 택배 고유 번호
        int width; // 택배 가로 길이
        int height; // 택배 세로 길이
        int col; // 택배 시작 좌표의 col값
        int row; // 택배 시작 좌표의 row값
        public Box(int idx, int height, int width, int col){
            this.row = 0;
            this.width = width;
            this.height = height;
            this.idx = idx;
            this.col = col;
            // {0,col} ~ {h,col+w}까지 arraylist에 추가
            for(int i = 0;i<height;i++){
                for(int j = col;j<col+width;j++){
                    map[i][j] = idx;
                }
            }
        }
        @Override
        public int compareTo(Box o) {
            return Integer.compare(this.idx, o.idx);
        }
    }
    static int n;
    static PriorityQueue<Box> boxes = new PriorityQueue<>();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringBuilder sb = new StringBuilder();
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        map = new int[n][n];
        for(int t = 0;t<m;t++){
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken());
            int height = Integer.parseInt(st.nextToken());
            int width = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken())-1;
            boxes.add(new Box(idx,height,width,col));
            // 택배 상자가 맵에 들어오면 중력 작용
            apply_gravity();
        }
        for(int i = 0;i<n;i++){
            move_simulate(true);
            move_simulate(false);
        }
        System.out.println(sb);
    }
    // 중력 적용시키는 함수
    static void apply_gravity(){
        // 일단 특정 row에서 범위는 항상 col ~ col + width임
        // 시작 row는 해당 박스의 row + height
        // 시작 row ~ n까지 내려가면서, col ~ col+ width까지 보면서, 만약에 map에 다른 게 있으면 바로 return하고 끝
        // 다른 게 없다면 box.row를 cur_row로 바꿈
        ArrayList<Box> temp = new ArrayList<>();
        for(Box b : boxes) {
            temp.add(b);
        }
        // 임시 배열을, 택배 바닥이 제일 낮은 걸 기준으로 정렬 -> 택배 바닥이 제일 낮은걸 먼저 중력 작용을 시켜야, 위에 있는 택배들이 연쇄적으로 떨어질 수 있음
        temp.sort((o1,o2)->Integer.compare(o2.row+o2.height, o1.row+o1.height));
        for(Box b : temp){
            // 택배의 바닥
            int bottom = b.row+b.height;
            // 제일 처음 상태일 때 택배 바닥 값을 저장
            int old_bottom = bottom;
            // 해당 박스가 밑으로 내려갈 때, 범위는 현재 넓이 만큼만 보면 됨
            int limit_c = b.col + b.width;
            m : for(;bottom<n;bottom++) {
                for (int c = b.col; c < limit_c; c++) {
                    // 바닥을 내려가면서 보다가, 0이 아닌 게 발견되면 거기 바로 위까지만 택배가 내려와야함
                    if (map[bottom][c] != 0) {
                        break m;
                    }
                }
            }
            // 실제 박스가 놓일 위치는, 위에서 탐지한 row의 바로 위임
            bottom--;

            // 여기까지 오면, 최종적으로 해당 박스가 위치할 바닥이 정해진 상황
            // 바닥에 맞게 row를 재설정
            b.row = bottom-b.height+1;
            // row 재설정하고 맵에 중력 작용
            for(int i = old_bottom-b.height;i<old_bottom;i++) {
                for(int j = b.col;j<b.col+b.width;j++){
                    map[i][j] = 0;
                }
            }
            for(int i = b.row;i<b.row+b.height;i++){
                for(int j = b.col;j<b.col+b.width;j++){
                    map[i][j] = b.idx;
                }
            }

        }

    }
    static void move_simulate(boolean is_left){
        ArrayList<Box> temp = new ArrayList<>();
        while(!boxes.isEmpty()) {
            // 박스들 중에 인덱스 번호가 가장 낮은 번호부터 확인
            Box cur_box = boxes.poll();
            boolean flag = (is_left)? can_out_left(cur_box) : can_out_right(cur_box);
            // 해당 박스가 왼쪽, 오른쪽으로 빠져나올 수 있는 상태라면?
            if(flag) {
                sb.append(cur_box.idx).append("\n");
                // 맵에서 해당 박스 영역을 지움
                for(int r = cur_box.row;r<cur_box.row+cur_box.height;r++) {
                    for(int c = cur_box.col;c<cur_box.col+cur_box.width;c++) {
                        map[r][c] = 0;
                    }
                }
                break;
            }
            // 만약 사라질 수 없는 박스면 임시 배열에 넣음
            else {
                temp.add(cur_box);
            }
        }
        // 원복을 위해서 boxes에 추가
        for(Box b : temp) {
            boxes.add(b);
        }
        // 중력 적용
        apply_gravity();

    }
    static boolean can_out_left(Box b) {
        // 왼쪽으로 빠질 수 있는지 봐야함
        // 현재 박스의 왼쪽에 아무것도 없어야함
        // 바운더리 설정 (박스의 시작 row부터, height 만큼 설정)
        int boundary_start = b.row;
        int boundary_end = b.row+b.height;
        // 현재 박스의 바로 앞 col -> 0(제일 왼쪽)으로 이동하면서 바운더리내에 다른 박스가 있는지 확인
        int cur_col = b.col-1;
        while(cur_col>=0) {
            for(int i = boundary_start;i<boundary_end;i++) {
                if(map[i][cur_col]!=0)
                    return false;
            }
            cur_col--;
        }
        return true;
    }
    static boolean can_out_right(Box b) {
        // 오른쪽으로 빠질 수 있는지 봐야함
        // 현재 박스의 오른쪽에 아무것도 없어야함
        // 바운더리 설정 (박스의 시작 row부터, height 만큼 설정)
        int boundary_start = b.row;
        int boundary_end = b.row+b.height;

        // 현재 박스의 바로 뒷 col -> n로 이동하면서, 바운더리 내에 다른 박스가 있는지 확인
        int cur_col = b.col+b.width;
        while(cur_col<n) {
            for(int i = boundary_start;i<boundary_end;i++) {
                if(map[i][cur_col]!=0)
                    return false;
            }
            cur_col++;
        }
        return true;
    }
}

