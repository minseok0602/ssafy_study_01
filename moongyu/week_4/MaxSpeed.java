package week_4;

import java.util.*;

public class MaxSpeed {
    private static final int INF = 1_000_000_001;

    private static class Road {
        int x1, y1, x2, y2, limit;
        boolean horizontal;
        int midX, midY;

        public Road(int x1, int y1, int x2, int y2, int limit) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.limit = limit;

            this.horizontal = (y1 == y2);

            // 문제에서 길이가 짝수라고 했으므로 midpoint는 항상 정수 좌표가 된다.
            this.midX = (x1 + x2) / 2;
            this.midY = (y1 + y2) / 2;
        }
    }

    private static class State {
        int id;
        int speed;

        public State(int id, int speed) {
            this.id = id;
            this.speed = speed;
        }
    }

    private static Road[] roads;

    // point id -> 좌표 / 그 점의 카메라 제한속도
    private static ArrayList<Integer> xs;
    private static ArrayList<Integer> ys;
    private static ArrayList<Integer> limitAtPoint;

    // 좌표 -> point id
    private static HashMap<Long, Integer> pointIdMap;

    // 각 도로 위에 존재하는 "중요한 점들"
    private static ArrayList<Integer>[] pointsInRoad;

    // 최종 그래프
    private static ArrayList<Integer>[] graph;

    public int[] solution(int[][] city, int[][] road) {
        int n = city.length;
        int m = road.length;

        roads = new Road[m];
        for (int i = 0; i < m; i++) {
            roads[i] = new Road(
                    road[i][0], road[i][1],
                    road[i][2], road[i][3],
                    road[i][4]
            );
        }

        xs = new ArrayList<>();
        ys = new ArrayList<>();
        limitAtPoint = new ArrayList<>();
        pointIdMap = new HashMap<>();

        pointsInRoad = new ArrayList[m];
        for (int i = 0; i < m; i++) {
            pointsInRoad[i] = new ArrayList<>();
        }

        // -------------------------------------------------
        // STEP 1) 모든 도시를 정점으로 등록
        // cityNode[i] = i번 도시(0-index)의 point id
        // -------------------------------------------------
        int[] cityNode = new int[n];
        for (int i = 0; i < n; i++) {
            cityNode[i] = getPointId(city[i][0], city[i][1]);
        }

        // -------------------------------------------------
        // STEP 2) 각 도로의 카메라 위치(중점)를 정점으로 등록
        // 같은 위치에 카메라가 여러 개면 제한속도는 min
        // -------------------------------------------------
        for (int i = 0; i < m; i++) {
            Road r = roads[i];
            int camId = getPointId(r.midX, r.midY);

            limitAtPoint.set(camId, Math.min(limitAtPoint.get(camId), r.limit));
            pointsInRoad[i].add(camId);
        }

        // -------------------------------------------------
        // STEP 3) 각 도시가 어떤 도로 위에 있는지 검사
        // 그 도로의 중요한 점 목록에 추가
        // -------------------------------------------------
        for (int i = 0; i < m; i++) {
            Road r = roads[i];

            for (int c = 0; c < n; c++) {
                int x = city[c][0];
                int y = city[c][1];

                if (isOnRoad(x, y, r)) {
                    pointsInRoad[i].add(cityNode[c]);
                }
            }
        }

        // -------------------------------------------------
        // STEP 4) 모든 도로 쌍의 교차점 / 접점을 찾는다.
        // 찾은 점은 두 도로 모두의 중요한 점 목록에 추가
        // -------------------------------------------------
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                int[] cross = getIntersection(roads[i], roads[j]);

                if (cross == null) continue;

                int id = getPointId(cross[0], cross[1]);
                pointsInRoad[i].add(id);
                pointsInRoad[j].add(id);
            }
        }

        // -------------------------------------------------
        // STEP 5) point 개수가 확정됐으니 그래프 생성
        // -------------------------------------------------
        int pointCount = xs.size();
        graph = new ArrayList[pointCount];
        for (int i = 0; i < pointCount; i++) {
            graph[i] = new ArrayList<>();
        }

        // -------------------------------------------------
        // STEP 6) 각 도로마다 중요한 점들을 도로 방향대로 정렬하고,
        // 인접한 점끼리 그래프에 연결
        // -------------------------------------------------
        for (int i = 0; i < m; i++) {
            buildEdgesOnRoad(i);
        }

        // -------------------------------------------------
        // STEP 7) 1번 도시(= cityNode[0])에서 widest path 수행
        // best[v] = v까지 갈 수 있는 최고 일정 속도
        // -------------------------------------------------
        int[] best = widestPath(cityNode[0]);

        // -------------------------------------------------
        // STEP 8) 답 만들기
        // 카메라를 하나도 안 지나가면 INF 그대로이므로 0으로 바꾼다.
        // -------------------------------------------------
        int[] answer = new int[n - 1];
        for (int i = 1; i < n; i++) {
            answer[i - 1] = (best[cityNode[i]] >= INF) ? 0 : best[cityNode[i]];
        }

        return answer;
    }

    // 좌표를 long key로 압축
    private static long makeKey(int x, int y) {
        return (((long) x) << 32) ^ (y & 0xffffffffL);
    }

    // 좌표에 해당하는 point id를 가져온다.
    // 없으면 새로 만든다.
    private static int getPointId(int x, int y) {
        long key = makeKey(x, y);

        Integer id = pointIdMap.get(key);
        if (id != null) return id;

        int newId = xs.size();
        pointIdMap.put(key, newId);

        xs.add(x);
        ys.add(y);
        limitAtPoint.add(INF);   // 기본은 카메라 없음

        return newId;
    }

    // 점 (x, y)가 도로 r 위에 있는지 검사
    private static boolean isOnRoad(int x, int y, Road r) {
        if (r.horizontal) {
            return y == r.y1 && r.x1 <= x && x <= r.x2;
        }
        return x == r.x1 && r.y1 <= y && y <= r.y2;
    }

    // 두 도로의 교차점 / 접점을 구한다.
    // 없으면 null
    private static int[] getIntersection(Road a, Road b) {
        // 둘 다 가로
        if (a.horizontal && b.horizontal) {
            if (a.y1 != b.y1) return null;

            int left = Math.max(a.x1, b.x1);
            int right = Math.min(a.x2, b.x2);

            // 한 점에서만 만나는 경우만 intersection
            if (left == right) {
                return new int[] { left, a.y1 };
            }
            return null;
        }

        // 둘 다 세로
        if (!a.horizontal && !b.horizontal) {
            if (a.x1 != b.x1) return null;

            int down = Math.max(a.y1, b.y1);
            int up = Math.min(a.y2, b.y2);

            // 한 점에서만 만나는 경우만 intersection
            if (down == up) {
                return new int[] { a.x1, down };
            }
            return null;
        }

        // 하나는 가로, 하나는 세로
        Road h = a.horizontal ? a : b;
        Road v = a.horizontal ? b : a;

        if (h.x1 <= v.x1 && v.x1 <= h.x2 && v.y1 <= h.y1 && h.y1 <= v.y2) {
            return new int[] { v.x1, h.y1 };
        }

        return null;
    }

    // 한 도로 위의 중요한 점들을 정렬한 뒤, 인접한 점끼리 연결
    private static void buildEdgesOnRoad(int roadIdx) {
        Road r = roads[roadIdx];
        ArrayList<Integer> list = pointsInRoad[roadIdx];

        list.sort((a, b) -> {
            if (r.horizontal) {
                return Integer.compare(xs.get(a), xs.get(b));
            }
            return Integer.compare(ys.get(a), ys.get(b));
        });

        int prev = -1;

        for (int cur : list) {
            // 같은 점이 중복으로 들어간 경우 skip
            if (cur == prev) continue;

            if (prev != -1) {
                graph[prev].add(cur);
                graph[cur].add(prev);
            }

            prev = cur;
        }
    }

    // widest path:
    // best[v] = 1번 도시에서 v까지 갈 때 가능한 최고 일정 속도
    private static int[] widestPath(int start) {
        int n = graph.length;
        int[] best = new int[n];
        Arrays.fill(best, -1);

        PriorityQueue<State> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(b.speed, a.speed)
        );

        best[start] = INF;
        pq.offer(new State(start, INF));

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (best[cur.id] != cur.speed) continue;

            for (int next : graph[cur.id]) {
                // next 정점에 카메라가 있으면 그 제한도 지켜야 한다.
                int nextSpeed = Math.min(cur.speed, limitAtPoint.get(next));

                if (best[next] >= nextSpeed) continue;

                best[next] = nextSpeed;
                pq.offer(new State(next, nextSpeed));
            }
        }

        return best;
    }
}
