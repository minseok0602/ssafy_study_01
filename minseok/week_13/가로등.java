import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Main {
    static int Q;
    static int N,M;
    static int currentId;

    static HashMap<Integer,Long> idToPo;
    static TreeSet<Long> lights;
    static PriorityQueue<Gap> pq;

    static class Gap {
        long left;
        long right;
        long dist;

        public Gap(long left, long right) {
            this.left = left;
            this.right = right;
            this.dist = right - left;
        }
    }

    public static void main(String[] args) throws IOException {
        // Please write your code here.
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        Q=Integer.parseInt(br.readLine());
        idToPo= new HashMap<>();
        lights=new TreeSet<>();
        pq=new PriorityQueue<>((a,b)->{
            if(a.dist !=b.dist){
                return Long.compare(b.dist,a.dist);
            }
           return Long.compare(a.left, b.left);
        });

        currentId=1;

        while (Q-->0){
            StringTokenizer st= new StringTokenizer(br.readLine());
            int cmd =Integer.parseInt(st.nextToken());

            if(cmd==100){
                N=Integer.parseInt(st.nextToken());
                M=Integer.parseInt(st.nextToken());
                long pre=-1;
                for(int i=1;i<=M;i++){
                    int a=Integer.parseInt(st.nextToken());
                    idToPo.put(currentId++, (long) a);
                    lights.add((long) a);

                    if (i > 1) {
                        pq.offer(new Gap(pre, a));
                    }
                    pre=(long) a;
                }

            }
            else if(cmd==200){
                Gap cur =getValidMaxGap();
                long position =(cur.left+cur.right+1)/2;
                lights.add(position);
                idToPo.put(currentId++, position);
                pq.offer(new Gap(cur.left,position));
                pq.offer(new Gap(position,cur.right));
            }
            else if(cmd==300){
                int targetId =Integer.parseInt(st.nextToken());
                long targetPo = idToPo.get(targetId);

                Long left =lights.lower(targetPo);
                Long right =lights.higher(targetPo);

                lights.remove(targetPo);
                idToPo.remove(targetId);

                if (left != null && right != null) {
                    pq.offer(new Gap(left, right));
                }

            }
            else{
                long answer = binarySearch();
                System.out.println(answer);
            }
        }
    }

    private static long binarySearch() {
        long left = 0;
        long right=100_000_000;
        long ans =right;

        Gap maxGap= getValidMaxGap();
        long maxDist =maxGap.dist;

        while (left < right) {
            long mid = (left + right) / 2;

            if (can(mid, maxDist)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    private static boolean can(long r, long maxDist) {
        long left=lights.first();
        long right=lights.last();

        if(2*(left-1)>r){
            return false;
        }
        if(2*(N-right)>r){
            return false;
        }

        if(maxDist>r){
            return false;
        }

        return true;
    }


    static Gap getValidMaxGap() {
        while (!pq.isEmpty()) {
            Gap gap = pq.peek();

            if (isValid(gap)) {
                return gap;
            }

            pq.poll();
        }

        return new Gap(0, 0);
    }

    static boolean isValid(Gap gap) {
        if (!lights.contains(gap.left)) {
            return false;
        }

        if (!lights.contains(gap.right)) {
            return false;
        }

        Long next = lights.higher(gap.left);

        return next != null && next.equals(gap.right);
    }
}