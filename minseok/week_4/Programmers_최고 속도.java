import java.util.*;

class Solution1 {

    // 카메라 제한속도의 최댓값이 1,000,000이므로
    // "카메라를 전혀 지나지 않는 경우"를 표현하기 위해 그보다 큰 값 사용
    static final int INF = 1000001;

    // 같은 좌표를 같은 Point id로 관리하기 위한 맵
    // key: "x,y" 형태의 문자열
    // value: 해당 좌표에 대응되는 point id
    static Map<String, Integer> point_map = new HashMap<>();

    // 전체 Point 목록
    // points.get(id)를 하면 해당 id의 실제 좌표를 알 수 있음
    static List<Point> points = new ArrayList<>();

    // 각 Point 위치에 카메라가 있다면 그 제한속도를 저장
    // 카메라가 여러 개 겹치면 가장 작은 제한속도를 저장
    // 카메라가 없으면 INF
    static List<Integer> point_camera = new ArrayList<>();

    // 최종 그래프
    // graph[u] = u 정점에서 갈 수 있는 간선들
    static ArrayList<Edge>[] graph;

    // 우선순위 큐에서 사용할 상태
    // node: 현재 정점 번호
    // speed: 시작점에서 이 정점까지 올 수 있는 "최대 가능한 속도"
    static class State {
        int node;
        int speed;

        State(int node, int speed) {
            this.node = node;
            this.speed = speed;
        }
    }

    // 그래프의 간선
    // to: 도착 정점
    // weight: 이 간선을 지날 때 허용되는 최대 속도
    static class Edge {
        int to;
        int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // 입력으로 주어진 "도로"를 표현하는 클래스
    // 이 도로는 나중에 여러 Point들로 잘린 뒤,
    // 인접한 Point들 사이의 간선으로 변환된다.
    static class Road {
        // 도로 양 끝점 (2배 좌표계 사용)
        long x1, y1, x2, y2;

        // 도로 중앙의 카메라 위치
        // 원래 좌표에서 중앙은 .5가 될 수 있으므로,
        // 전체 좌표를 2배해서 모두 정수로 다루기 위해 이렇게 저장한다.
        long mx, my;

        // 이 도로 중앙 카메라의 제한속도
        int limit;

        // 세로 도로 여부
        // true면 세로, false면 가로
        boolean is_vertical;

        // 이 도로 위에 존재하는 모든 Point id 목록
        // (시작점, 끝점, 카메라, 도시, 교차점 등)
        List<Integer> points = new ArrayList<>();

        // 같은 Point가 이 도로에 중복해서 들어가는 것을 막기 위한 Set
        Set<Integer> point_set = new HashSet<>();

        public Road(long x1, long y1, long x2, long y2, int limit) {

            // 모든 좌표를 2배해서 저장
            // 예를 들어 원래 중앙이 (1.5, 2)였다면
            // 2배 좌표계에서는 (3, 4)처럼 정수로 처리 가능
            this.x1 = 2L * x1;
            this.y1 = 2L * y1;
            this.x2 = 2L * x2;
            this.y2 = 2L * y2;

            // 2배 좌표계에서 중앙점은 x1+x2, y1+y2 로 바로 표현 가능
            this.mx = x1 + x2;
            this.my = y1 + y2;

            this.limit = limit;

            // 원래 입력 기준에서 x가 같으면 세로 도로
            this.is_vertical = (x1 == x2);

            // 도로의 시작점 / 카메라점 / 끝점을 Point로 등록
            int start_idx = get_point_idx(this.x1, this.y1);
            int middle_idx = get_point_idx(this.mx, this.my);
            int end_idx = get_point_idx(this.x2, this.y2);

            // 카메라가 있는 점의 제한속도 갱신
            // 같은 점에 여러 도로의 카메라가 겹칠 수 있으므로 최소값 저장
            point_camera.set(
                middle_idx,
                Math.min(point_camera.get(middle_idx), this.limit)
            );

            // 이 도로 위의 중요한 점들(시작점, 카메라, 끝점)을 등록
            if (this.point_set.add(start_idx)) this.points.add(start_idx);
            if (this.point_set.add(middle_idx)) this.points.add(middle_idx);
            if (this.point_set.add(end_idx)) this.points.add(end_idx);
        }
    }

    // 그래프의 정점 하나를 표현
    // type(도시/카메라/교차점 등)은 따로 두지 않고,
    // 좌표와 id만 관리한다.
    static class Point {
        long x, y;
        int idx;

        public Point(int idx, long x, long y) {
            this.idx = idx;
            this.x = x;
            this.y = y;
        }
    }

    public int[] solution(int[][] city, int[][] road) {

        point_map.clear();
        points.clear();
        point_camera.clear();
        graph = null;

        // 전체 도로 목록
        List<Road> road_list = new ArrayList<>();

        // 입력 도로들을 Road 객체로 변환
        for (int[] r : road) {
            road_list.add(new Road(r[0], r[1], r[2], r[3], r[4]));
        }

        // 모든 도로 쌍에 대해 교차점(또는 만나는 점) 찾기
        for (int i = 0; i < road_list.size(); i++) {
            for (int j = i + 1; j < road_list.size(); j++) {
                long[] p = get_intersection(road_list.get(i), road_list.get(j));

                // 교차점이 존재하면 그 점을 양쪽 도로에 모두 등록
                if (p != null) {
                    int point_idx = get_point_idx(p[0], p[1]);

                    if (road_list.get(i).point_set.add(point_idx)) {
                        road_list.get(i).points.add(point_idx);
                    }
                    if (road_list.get(j).point_set.add(point_idx)) {
                        road_list.get(j).points.add(point_idx);
                    }
                }
            }
        }

        // 모든 도시를 해당 도로들 위의 Point로 등록
        for (int[] c : city) {
            long cx = 2L * c[0];
            long cy = 2L * c[1];

            for (Road r : road_list) {

                // 도시가 이 도로 선분 위에 있으면 등록
                if (cx >= r.x1 && cx <= r.x2 && cy >= r.y1 && cy <= r.y2) {
                    int point_idx = get_point_idx(cx, cy);

                    if (r.point_set.add(point_idx)) {
                        r.points.add(point_idx);
                    }
                }
            }
        }

        // 전체 Point 개수만큼 그래프 생성
        graph = new ArrayList[points.size()];
        for (int i = 0; i < points.size(); i++) {
            graph[i] = new ArrayList<>();
        }

        // 각 도로를 "점들의 정렬된 순서"로 보고,
        // 인접한 점들 사이에 간선을 추가
        for (Road r : road_list) {

            // 세로 도로면 y 기준 정렬, 가로 도로면 x 기준 정렬
            if (r.is_vertical) {
                r.points.sort((a, b) -> Long.compare(points.get(a).y, points.get(b).y));
            } else {
                r.points.sort((a, b) -> Long.compare(points.get(a).x, points.get(b).x));
            }

            // 정렬된 인접 점끼리 간선 생성
            for (int i = 0; i < r.points.size() - 1; i++) {
                int u = r.points.get(i);
                int v = r.points.get(i + 1);

                // u-v 구간을 이동할 때 적용되는 제한속도 계산
                // 이 구간의 양 끝점 중 카메라가 있는 점이 있으면 그 제한속도,
                // 둘 다 카메라가 없으면 INF
                int edge_limit = get_segment_limit(u, v);

                graph[u].add(new Edge(v, edge_limit));
                graph[v].add(new Edge(u, edge_limit));
            }
        }

        // best[x] = 시작 도시(1번)에서 x까지 도달할 수 있는 최대 속도
        int[] best = new int[points.size()];
        Arrays.fill(best, -1);

        // 시작점은 1번 도시
        int start = get_point_idx(2L * city[0][0], 2L * city[0][1]);
        best[start] = INF;

        // 최대 속도가 큰 상태를 먼저 꺼내기 위한 최대 힙
        PriorityQueue<State> pq = new PriorityQueue<>(
            (a, b) -> Integer.compare(b.speed, a.speed)
        );
        pq.add(new State(start, INF));

        // 최대 병목 경로(widest path) 탐색
        while (!pq.isEmpty()) {
            State cur = pq.poll();

            // 이미 더 좋은 값으로 처리된 정점이면 스킵
            if (cur.speed < best[cur.node]) continue;

            for (Edge e : graph[cur.node]) {
                int next = e.to;

                // 현재까지 가능한 속도와, 다음 간선의 제한속도 중 작은 값이
                // next까지 가는 경로의 가능한 속도
                int next_speed = Math.min(cur.speed, e.weight);

                // 더 좋은(더 큰) 속도로 next에 도달할 수 있으면 갱신
                if (next_speed > best[next]) {
                    best[next] = next_speed;
                    pq.add(new State(next, next_speed));
                }
            }
        }

        // 2번 도시부터 n번 도시까지 정답 작성
        int[] answer = new int[city.length - 1];
        for (int i = 1; i < city.length; i++) {
            int speed = best[get_point_idx(2L * city[i][0], 2L * city[i][1])];

            // 카메라를 전혀 지나지 않는 경우 INF로 남아 있으므로 0으로 변환
            answer[i - 1] = (speed == INF) ? 0 : speed;
        }

        return answer;
    }

    // 좌표 (x, y)에 해당하는 Point id를 반환
    // 아직 없으면 새 Point를 생성해서 등록
    static int get_point_idx(long x, long y) {
        String key = x + "," + y;

        if (!point_map.containsKey(key)) {
            int id = points.size();
            point_map.put(key, id);
            points.add(new Point(id, x, y));

            // 새 점은 기본적으로 카메라가 없으므로 INF로 초기화
            point_camera.add(INF);
        }

        return point_map.get(key);
    }

    // 두 Point 사이 구간의 제한속도를 반환
    // 인접한 두 점을 잇는 구간에서는,
    // 양 끝점 중 카메라가 있는 점의 제한속도가 곧 그 구간의 제한속도
    // 둘 다 카메라가 없으면 INF
    static int get_segment_limit(int u, int v) {
        return Math.min(point_camera.get(u), point_camera.get(v));
    }

    // 두 도로가 교차하거나 끝점에서 만나는 경우 그 좌표를 반환
    // 아니면 null 반환
    static long[] get_intersection(Road a, Road b) {

        // a: 세로, b: 가로
        if (a.is_vertical && !b.is_vertical) {
            if (a.x1 >= b.x1 && a.x1 <= b.x2 &&
                b.y1 >= a.y1 && b.y1 <= a.y2) {
                return new long[]{a.x1, b.y1};
            }
        }

        // a: 가로, b: 세로
        else if (!a.is_vertical && b.is_vertical) {
            if (b.x1 >= a.x1 && b.x1 <= a.x2 &&
                a.y1 >= b.y1 && a.y1 <= b.y2) {
                return new long[]{b.x1, a.y1};
            }
        }

        // 둘 다 가로인 경우
        // 같은 y선 위에서 끝점 하나만 맞닿는 경우만 가능
        else if (!a.is_vertical && !b.is_vertical) {
            if (a.y1 != b.y1) return null;
            if (a.x2 == b.x1) return new long[]{a.x2, a.y1};
            if (b.x2 == a.x1) return new long[]{b.x2, b.y1};
        }

        // 둘 다 세로인 경우
        // 같은 x선 위에서 끝점 하나만 맞닿는 경우만 가능
        else {
            if (a.x1 != b.x1) return null;
            if (a.y2 == b.y1) return new long[]{a.x1, a.y2};
            if (b.y2 == a.y1) return new long[]{a.x1, b.y2};
        }

        return null;
    }
}