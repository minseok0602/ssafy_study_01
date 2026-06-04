import java.io.*;
import java.util.*;

public class StreepLampInstallation {

    static int N;
    static int M;

    // 위치 기준으로 가로등 정렬 (key : 가로등의 위치, value : 가로등 번호)
    static TreeMap<Integer, Integer> lamps = new TreeMap<>();
    // 가로등 번호 -> 위치로 저장해두는 배열
    static int[] idToPos;

    // 가장 큰 간격을 관리하는 pq
    static PriorityQueue<Gap> pq = new PriorityQueue<>();

    // 다음에 추가될 가로등 번호 관리
    static int nextId;

    static class Gap implements Comparable<Gap> {
        int left;
        int right;
        int dist;

        Gap(int left, int right) {
            this.left = left;
            this.right = right;
            this.dist = right - left;
        }

        @Override
        public int compareTo(Gap o) {
            // 간격이 큰 것 우선
            if (this.dist != o.dist) {
                return o.dist - this.dist;
            }

            // 간격이 같으면 왼쪽 좌표가 작은 것 우선
            return this.left - o.left;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int Q = Integer.parseInt(br.readLine());

        idToPos = new int[Q + 100000 + 5];

        for (int i = 0; i < Q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == 100) {
                init(st);
            } else if (cmd == 200) {
                addLamp();
            } else if (cmd == 300) {
                int id = Integer.parseInt(st.nextToken());
                deleteLamp(id);
            } else if (cmd == 400) {
                sb.append(getAnswer()).append('\n');
            }
        }

        System.out.print(sb);
    }

    
    // 100 명령: 초기 가로등 세팅
    static void init(StringTokenizer st) {
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for (int id = 1; id <= M; id++) {
            int pos = Integer.parseInt(st.nextToken());

            lamps.put(pos, id);
            idToPos[id] = pos;
        }

        nextId = M + 1;

        // 간격들을 PQ에 넣어서 최대 간격 관리
        Integer prev = null;

        for (int pos : lamps.keySet()) {
            if (prev != null) {
                pq.add(new Gap(prev, pos));
            }

            prev = pos;
        }
    }
    
    // 200 명령: 가로등 추가
    static void addLamp() {
        deleteTrash();

        Gap gap = pq.poll();

        int left = gap.left;
        int right = gap.right;

        int mid = left + (right - left + 1) / 2;

        int id = nextId++;
        lamps.put(mid, id);
        idToPos[id] = mid;

        // 기존 left ~ right 간격이 사라지고,
        // left ~ mid, mid ~ right 두 간격이 새로 생김
        pq.add(new Gap(left, mid));
        pq.add(new Gap(mid, right));
    }
    
    // 삭제 이후 남아있는 간격을 정리
    static void deleteTrash() {
        while (!pq.isEmpty()) {
            Gap gap = pq.peek();

            // 양쪽 가로등 중 하나라도 삭제됐다면 없애줌
            if (!lamps.containsKey(gap.left) || !lamps.containsKey(gap.right)) {
                pq.poll();
                continue;
            }

            // gap에서 왼쪽 가로등의 바로 오른쪽 가로등이 gap.right가 아니면 무효
            // 즉, 중간에 다른 가로등이 추가된 예전 간격이라는 뜻
            if (lamps.higherKey(gap.left) != gap.right) {
                pq.poll();
                continue;
            }

            // 여기까지 통과하면 현재 살아있는 진짜 인접 간격
            break;
        }
    }

    // 300 명령: 가로등 삭제
    static void deleteLamp(int id) {
        int pos = idToPos[id];

        Integer left = lamps.lowerKey(pos);
        Integer right = lamps.higherKey(pos);

        lamps.remove(pos);
        idToPos[id] = 0;

        // 삭제한 가로등 양쪽에 가로등이 있으면 left ~ right 새 간격이 생성
        if (left != null && right != null) {
            pq.add(new Gap(left, right));
        }
    }

    // 400 명령: 현재 필요한 최소 소비 전력 r * 2 계산
    static int getAnswer() {
        deleteTrash();

        int first = lamps.firstKey();
        int last = lamps.lastKey();

        // 1부터 첫 가로등 사이를 밝히기 위해 필요한 2r
        int leftNeed = 2 * (first - 1);

        // 마지막 가로등 부터 오른쪽 끝까지 밝히기 위해 필요한 2r
        int rightNeed = 2 * (N - last);

        // 가로등 사이를 밝히기 위해 필요한 2r
        int middleNeed = pq.peek().dist;
        
        return Math.max(middleNeed, Math.max(leftNeed, rightNeed));
    }
}