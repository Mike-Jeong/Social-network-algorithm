import java.util.Arrays;

public class BAFBP {
    Vertex[] names;
    GraphADT<String> graph = new AdjacencyListGraph<String>();
    private double[][] wtable;
    private String[] saveRoute;
    String[] name;

    public BAFBP(Enum[] name, double[][] atable) {
        names = new Vertex[name.length];
        wtable = new double[name.length][name.length];
        saveRoute = new String[name.length];
        this.name = new String[name.length];

        for (int i = 0; i < names.length; i++) {
            names[i] = graph.addVertex(String.valueOf(name[i]));
            this.name[i] = String.valueOf(name[i]);
        }

        for (int i = 0; i < atable.length; i++) {

            for (int j = i; j < atable.length; j++) {

                wtable[i][j] = -Math.log(atable[i][j]);

                if (i != j) {
                    graph.addEdge(names[i], names[j], atable[i][j]);
                }
            }
        }

    }

    public int stringToint(String s) {
        // 문자열을 int형으로 바꿔준다.
        int x = Arrays.asList(name).indexOf(s);
        return x;
    }

    public void algorithm(String a, String b) {

        int coma = stringToint(a);
        int comb = stringToint(b);
        int starting;
        int target;

        if (coma > comb) {

            starting = comb;
            target = coma;

        }

        else {
            starting = coma;
            target = comb;
        }

        boolean[] visited = new boolean[names.length]; // 각 꼭지점의 방문 여부
        double distance[] = new double[names.length]; // 시작 꼭지점에서부터 각 꼭지점까지의 거리

        // 시작 꼭지점 a에서부터 각 꼭지점까지의 모든 거리 초기화
        for (int i = 0; i < names.length; i++) {
            // 이산수학 교재 251쪽에서는 ∞로 초기화했지만
            // 여기에서는 int형의 가장 큰 값 2147483647로 초기화한다.
            distance[i] = Integer.MAX_VALUE;
        }

        distance[starting] = 0; // 시작 꼭지점 x의 거리를 0으로 초기화하고,
        visited[starting] = true; // 방문 꼭지점이므로 true값 저장
        saveRoute[starting] = name[starting]; // ★시작 꼭지점의 경로는 자기 자신을 저장

        // 시작 꼭지점 x에서부터 꼭지점 i까지의 거리를 갱신한다.
        for (int i = 0; i < names.length; i++) {
            // 방문하지 않았고 x에서 i까지의 가중치가 존재한다면, 거리 i에 x에서 i까지의 가중치 저장
            // 즉 x에서 인접한 꼭지점까지의 거리를 갱신함.
            // (x와 인접하지 않은 꼭지점에는 Integer.MAX_VALUE가 계속 저장되어 있을 것이다.)
            if (!visited[i] && wtable[starting][i] != 0) {
                distance[i] = wtable[starting][i];
                saveRoute[i] = name[starting]; // ★시작 꼭지점과 인접한 꼭지점의 경로에 시작 꼭지점을 저장
            }
        }

        for (int i = 0; i < names.length - 1; i++) {
            double minDistance = Integer.MAX_VALUE; // 최단거리 minDistance에 일단 가장 큰 정수로 저장하고,
            int minVertex = -1; // 그 거리값이 있는 인덱스 minIndex에 -1을 저장해둔다.
            for (int j = 0; j < names.length; j++) {
                // 방문하지 않았고 거리를 갱신한 꼭지점 중에서 가장 가까운 거리와 가장 가까운 꼭지점을 구한다.
                if (!visited[j] && distance[j] != Integer.MAX_VALUE) {
                    if (distance[j] < minDistance) {
                        minDistance = distance[j];
                        minVertex = j;
                    }
                }
            }
            visited[minVertex] = true; // 위의 반복문을 통해 도출된 가장 가까운 꼭지점에 방문 표시
            for (int j = 0; j < names.length; j++) {
                // 방문하지 않았고 minVertex와의 가중치가 존재하는(minVertex에서 연결된) 꼭지점이라면
                if (!visited[j] && wtable[minVertex][j] != 0) {
                    // 지금 그 꼭지점이 가지고 있는 거리값이 minVertex와 가중치를 더한 값보다 크다면 최단거리 갱신
                    if (distance[j] > distance[minVertex] + wtable[minVertex][j]) {
                        distance[j] = distance[minVertex] + wtable[minVertex][j];
                        saveRoute[j] = name[minVertex]; // 최단거리가 갱신된 꼭지점의 경로에 minVertex에 해당하는 꼭지점 저장
                    }
                }
            }
        }

        int ib = target;
        // 시작 꼭지점부터 특정 꼭지점까지의 거리 출력
        System.out.println("Weight of " + a + " to " + b + " : " + distance[ib]);
        System.out.println("Corresponding to an association strength : " + String.format("%.1f", Math.exp(distance[ib])));
        System.out.println("==================================");

        // 시작 꼭지점부터 특정 꼭지점까지의 경로 출력
        String route = "";
        System.out.println(name[starting] + " - " + name[ib]);
        int index = ib;
        while (true) {
            if (saveRoute[index] == name[index])
                break; // 시작 꼭지점일 경우 break
            route += saveRoute[index] + " ";
            index = stringToint(saveRoute[index]); // 결정적인 역할을 한 꼭지점을 int형으로 바꿔서 index에 저장
        }
        StringBuilder sb = new StringBuilder(route);
        System.out.println(sb + "" + name[ib]);
        System.out.println("==================================");
        System.out.println(graph);
    }

}
