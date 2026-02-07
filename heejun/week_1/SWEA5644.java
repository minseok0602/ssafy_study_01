import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;

public class SWEA5644 {
    static class Bc {
        int i, j, c, p; // dir: 0~3 (상하좌우)

        public Bc(int i, int j, int c, int p) {
            this.i = i;
            this.j = j;
            this.c = c;
            this.p = p;
        }
    }
    static int M;
    static int A;
    static int[] Amove;
    static int[] Bmove;
    static StringTokenizer st;
    static ArrayList<Bc> table;
    static int Asum,Bsum;
    static int[] dr={0,-1,0,1,0};
    static int[] dc={0,0,1,0,-1};
    static int A_i,A_j,B_i,B_j;
    static int N=10;
    static ArrayList<Bc>[][] board;
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        int T = Integer.parseInt(br.readLine());
        for(int test_case = 1; test_case <= T; test_case++){
            ///////////// 변수 초기화////////////////////
            Asum=0;
            Bsum=0;
            A_i=0;
            A_j=0;
            B_i=9;
            B_j=9;
            board = new ArrayList[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) board[i][j] = new ArrayList<>();
            }

            ///////////// 입력 받기//////////////////
            st=new StringTokenizer(br.readLine());
            M=Integer.parseInt(st.nextToken());
            A=Integer.parseInt(st.nextToken());

            Amove=new int[M];
            Bmove=new int[M];
            st=new StringTokenizer(br.readLine());
            for(int i=0;i<M;i++){
                Amove[i]=Integer.parseInt(st.nextToken());
            }
            st=new StringTokenizer(br.readLine());

            for(int i=0;i<M;i++){
                Bmove[i]=Integer.parseInt(st.nextToken());
            }
            table =new ArrayList<>();
            for(int i=0;i<A;i++){
                st=new StringTokenizer(br.readLine());
                int c=Integer.parseInt(st.nextToken());
                int r=Integer.parseInt(st.nextToken());
                int C=Integer.parseInt(st.nextToken());
                int p=Integer.parseInt(st.nextToken());
                Bc b=new Bc(r-1,c-1,C,p);
                drow(b);
                table.add(b);

            }

            /////////시뮬레이션 시작/////////////////////////////////////////
            charge();
            for(int i=0;i<M;i++){
                move(i);
                charge();

            }

            int total=Asum+Bsum;
            System.out.printf("#%d %d",test_case,total);
            System.out.println();
        }
    }

    private static void drow(Bc b) {
        int r=b.i;
        int c=b.j;
        int R=b.c;

        // N x N 격자에서 중심 (r,c), 반경 R 마름모 탐색
        for (int i = r - R; i <= r + R; i++) {
            int d = Math.abs(i - r);   // 중심과 행 거리
            int w = R - d;             // 이 행에서 좌우로 퍼질 수 있는 폭

            int start = c - w;
            int end   = c + w;

            for (int j = start; j <= end; j++) {
                if (i < 0 || i >= N || j < 0 || j >= N) continue;
                board[i][j].add(b);
            }
        }

    }



    private static void charge() {
        ArrayList<Bc> a=board[A_i][A_j];
        ArrayList<Bc> b=board[B_i][B_j];
        Bc useA=null;
        Bc useB=null;
        int maxA=0;
        int maxB=0;
        // p 기준 내림차순
        a.sort(Comparator.comparingInt((Bc c) -> c.p).reversed());
        b.sort(Comparator.comparingInt((Bc c) -> c.p).reversed());

        if(!a.isEmpty()){
            for(Bc bc : a){
                if(bc.p>maxA){
                    maxA=bc.p;
                    useA=bc;
                }
            }

        }
        if(!b.isEmpty()){
            for(Bc bc : b){
                if(bc.p>maxB){
                    maxB=bc.p;
                    useB=bc;
                }
            }

        }

        if(useA!=null &&useA.equals(useB)){

            if(a.size()==1 &&b.size()==1){
                if(!a.isEmpty()){
                    Asum+=useA.p;
                }

            }else if(a.size()==1){
                int bb=b.get(1).p+maxA;
                if(bb>maxA){
                    useB=b.get(1);
                }
                if(!a.isEmpty()){
                    Asum+=useA.p;
                }
                if(!b.isEmpty()){
                    Bsum+=useB.p;
                }
            }
            else if(b.size()==1)
            {
                int aa=a.get(1).p+maxA;
                if(aa>maxA){
                    useA=a.get(1);
                }
                if(!a.isEmpty()){
                    Asum+=useA.p;
                }
                if(!b.isEmpty()){
                    Bsum+=useB.p;
                }
            }
            else{
                int aa=a.get(1).p+maxB;
                int bb=b.get(1).p+maxA;
                if(aa>=bb&&aa>maxA){
                    useA=a.get(1);
                }
                else if(aa<=bb&&bb>maxA){
                    useB=b.get(1);
                }
                else{
                    Asum+=maxA;
                    return;
                }
                if(!a.isEmpty()){
                    Asum+=useA.p;
                }
                if(!b.isEmpty()){
                    Bsum+=useB.p;
                }

            }


        }else{
            if(!a.isEmpty()){
                Asum+=useA.p;
            }
            if(!b.isEmpty()){
                Bsum+=useB.p;
            }
        }




    }
    private static void move(int i) {
        A_i+=dr[Amove[i]];
        A_j+=dc[Amove[i]];
        B_i+=dr[Bmove[i]];
        B_j+=dc[Bmove[i]];
    }

}
