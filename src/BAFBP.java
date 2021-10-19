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

        boolean[] visited = new boolean[names.length]; 
        double distance[] = new double[names.length]; 

    
        for (int i = 0; i < names.length; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        distance[starting] = 0; 
        visited[starting] = true; 
        saveRoute[starting] = name[starting]; 


        for (int i = 0; i < names.length; i++) {

            if (!visited[i] && wtable[starting][i] != 0) {
                distance[i] = wtable[starting][i];
                saveRoute[i] = name[starting]; 
            }
        }

        for (int i = 0; i < names.length - 1; i++) {
            double minDistance = Integer.MAX_VALUE; 
            int minVertex = -1; 
            for (int j = 0; j < names.length; j++) {
                
                if (!visited[j] && distance[j] != Integer.MAX_VALUE) {
                    if (distance[j] < minDistance) {
                        minDistance = distance[j];
                        minVertex = j;
                    }
                }
            }
            visited[minVertex] = true; 
            for (int j = 0; j < names.length; j++) {
              
                if (!visited[j] && wtable[minVertex][j] != 0) {
                    
                    if (distance[j] > distance[minVertex] + wtable[minVertex][j]) {
                        distance[j] = distance[minVertex] + wtable[minVertex][j];
                        saveRoute[j] = name[minVertex]; 
                    }
                }
            }
        }

        int ib = target;
        System.out.println("Weight of " + a + " to " + b + " : " + distance[ib]);
        System.out.println("Corresponding to an association strength : " + String.format("%.1f", Math.exp(distance[ib])));
        System.out.println("==================================");

        String route = "";
        System.out.println(name[starting] + " - " + name[ib]);
        int index = ib;
        while (true) {
            if (saveRoute[index] == name[index])
                break; 
            route += saveRoute[index] + " ";
            index = stringToint(saveRoute[index]); 
        }
        StringBuilder sb = new StringBuilder(route);
        System.out.println(sb + "" + name[ib]);
        System.out.println("==================================");
        System.out.println(graph);
    }

}
