import java.util.*;
import java.io.*;

public class 가로등설치 {
    static int command_count;
    static int n;
    static int m;

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static int id = 1;

    static class Light {
        int idx;
        int location;
        boolean is_deleted = false;

        public Light(int idx, int location) {
            this.idx = idx;
            this.location = location;
        }
    }

    static class Edge implements Comparable<Edge> {
        Light x;
        Light y;
        int distance;

        public Edge(Light x, Light y) {
            this.x = x;
            this.y = y;
            this.distance = y.location - x.location;
        }

        @Override
        public int compareTo(Edge e) {
            if (this.distance != e.distance) {
                return e.distance - this.distance;
            }
            return this.x.location - e.x.location;
        }
    }
    // 특정 idx의 가로등을 바로 가져오기 위한 map
    static HashMap<Integer, Light> map = new HashMap<>();
    
    // 현재 가로등들의 간격 중에서, 가장 거리가 먼 간격을 바로 가져오기 위한 pq
    static PriorityQueue<Edge> edges = new PriorityQueue<>();

    // 현재 가로등 중에서, 제일 앞에 (뒤에) 있는 가로등의 idx
    static int start, end;
    // prev[i] : idx 가로등의 바로 앞에 있는 가로등의 idx
    // next[i] : idx 가로등의 바로 뒤에 있는 가로등의 idx
    static int[] prev, next;

    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        command_count = Integer.parseInt(st.nextToken());

        for (int i = 0; i < command_count; i++) {
            st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());

            switch (command) {
                case 100: {
                    n = Integer.parseInt(st.nextToken());
                    m = Integer.parseInt(st.nextToken());
                    prev = new int[m + command_count+1];
                    next = new int[m + command_count+1];

                    // 가로등의 idx는 전역으로, 생성자를 호출할 때마다 ++로 id 생성
                    Light pivot = new Light(id++, Integer.parseInt(st.nextToken()));
                    map.put(pivot.idx, pivot);
                    // 제일 처음 생긴 가로등을 start로 박고 시작
                    start = pivot.idx;

                    for (int j = 1; j < m; j++) {
                        int cur_location = Integer.parseInt(st.nextToken());

                        // 남은 m-1 동안 가로등을 생성하면서 map에 넣고
                        Light light = new Light(id++, cur_location);
                        map.put(light.idx, light);

                        // 바로 직전 가로등과의 간격을 edge로 만들어서 pq에 넣음
                        edges.add(new Edge(pivot, light));

                        //지금 가로등의 직전 가로등은, 바로 전 가로등으로 지정
                        prev[light.idx] = pivot.idx;
                        
                        // 직전 가로등의 바로 다음 가로등을 이 가로등으로 지정
                        next[pivot.idx] = light.idx;

                        pivot = light;
                    }
                    // 마지막 반복문까지 끝나면 m번째 가로등이 end가 됨
                    end = pivot.idx;
                    break;
                }

                case 200: {
                	// 현존하는 간격 중에 가장 간격이 먼 엣지를 가져옴
                    Edge e = getEdge();

                    // 이 간격의 중앙 위치에 새로운 가로등을 박아야함
                    int target=(int)Math.ceil((e.x.location + e.y.location)/2.0);

                    // 새로운 가로등을 만들고 map에다가 젛음
                    Light new_light = new Light(id++, target);
                    map.put(new_light.idx, new_light);

                    // 지금 가져온 간선은 e.x----------e.y임
                    // e.x------new_light------e.y가 된 상황
                    //new_right의 직전 가로등을 e.x로
                    //new_right의 직후 가로등을 e.y로
                    prev[new_light.idx] = e.x.idx;
                    next[new_light.idx] = e.y.idx;
                    
                    //e.x의 다음 가로등을 new_light로
                    //e.y의 직전 가로등을 new_light로
                    next[e.x.idx] = new_light.idx;
                    prev[e.y.idx] = new_light.idx;

                    // 새로 생긴 두 간선 추가
                    // e.x------new_light
                    // new_light------e.y
                    edges.add(new Edge(e.x, new_light));
                    edges.add(new Edge(new_light, e.y));

                    break;
                }

                case 300: {
                	// map을 이용해서 idx에 해당하는 가로등을 가지고 와서, is_deleted 표시
                    int target = Integer.parseInt(st.nextToken());

                    Light removed = map.get(target);
                    removed.is_deleted = true;
                    // 이 가로등이 사라지면, 이 가로등 직전 가로등이랑, 이 가로등 직후 가로등을 서로 연결해야함
                    int left = prev[target];
                    int right = next[target];

                    // 근데 이 가로등이 start엿거나 end면 예외처리 해줘야함
                    if (left == 0) {
                        // 시작 가로등 삭제
                        start = right;
                        prev[start] = 0;
                    } else if (right == 0) {
                        // 끝 가로등 삭제
                        end = left;
                        next[end] = 0;
                    } else {
                        // 중간 가로등 삭제
                        next[left] = right;
                        prev[right] = left;
                        
                        // start나 end가 아니었으면 새로운 간선을 만들어줘야함 (중간 것이 빠졌으니까)
                        edges.add(new Edge(map.get(left), map.get(right)));
                    }

                    break;
                }

                case 400: {
                    System.out.println(calculate_min_power());
                    break;
                }
            }
        }
    }

    static int calculate_min_power() {
        Edge edge = getEdge();

        // 가로등이 밝힐 수 있는 최소 전력은 원점---------start, end--------종점, 중간에 가로등 사이의 간격 중에 제일 큰 간격
        // 이 3가지의 값 중에 제일 큰 걸 가져와야함 
        int left_power = map.get(start).location - 1;
        int right_power = n - map.get(end).location;
        int mid_power = edge == null ? 0 : edge.distance;

        return Math.max(Math.max(2*left_power, 2*right_power), mid_power);
    }

    static Edge getEdge() {
        while (!edges.isEmpty()) {
            Edge edge = edges.peek();

            // 둘 중 하나라도 삭제된 가로등이면 무효
            if (edge.x.is_deleted || edge.y.is_deleted) {
                edges.poll();
                continue;
            }

            // 현재 연결 리스트상에서 x 다음이 y가 아니면 무효 (새로운 가로등이 생길 때의 예외)
            // 왜냐면 edges에는 옛날 버전의 간선들이 다 있기 때문에 prev, next 배열을 보고 상태를 파악해야함
            if (next[edge.x.idx] != edge.y.idx || prev[edge.y.idx] != edge.x.idx) {
                edges.poll();
                continue;
            }

            return edge;
        }

        return null;
    }
}
