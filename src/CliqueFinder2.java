import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CliqueFinder2<E> {

    private GraphADT<E> graph;
    private Map<Edge<E>, Double> weights;
    private Set<Vertex<E>> R, P, X;

    public CliqueFinder2(GraphADT<E> graph, Map<Edge<E>, Double> weights) {
        this.graph = graph;
        this.weights = weights;
        R = new HashSet<Vertex<E>>();
        P = new HashSet<Vertex<E>>();
        X = new HashSet<Vertex<E>>();
    }

    // Bron_Kerbosch algorithm without a Pivot 
    private void Bron_Kerbosch(Set<Vertex<E>> R, Set<Vertex<E>> P, Set<Vertex<E>> X) {

        System.out.print(printSet(R) + ", " + printSet(P) + ", " + printSet(X));
        if ((P.size() == 0) && (X.size() == 0)) {
            printClique(R);
            return;
        }
        System.out.println();

        Set<Vertex<E>> P1 = new HashSet<Vertex<E>>(P);

        for (Vertex<E> v : P) {
            R.add(v);
            Bron_Kerbosch(R, intersect(P1, v.adjacentVertices()),
                    intersect(X, v.adjacentVertices()));
            R.remove(v);
            P1.remove(v);
            X.add(v);
        }
    }
  
    // Intersection of two sets 
    private Set<Vertex<E>> intersect(Set<Vertex<E>> set1, Set<Vertex<E>> set2) {
        Set<Vertex<E>> intersect = new HashSet<Vertex<E>>(set1);
        intersect.retainAll(set2);
        return intersect;
    }

    private void printClique(Set<Vertex<E>> R) {
        System.out.print(" -------------- Maximal Clique: ");
        for (Vertex<E> v : R) {
            System.out.print(" " + (v.getUserObject()));
        }
        System.out.println();
    }

    private String printSet(Set<Vertex<E>> V) {
        StringBuilder s = new StringBuilder();
        s.append("{");
        for (Vertex<E> v : V) {
            s.append("" + (v.getUserObject()) + ",");
        }
        if (s.length() != 1) {
            s.setLength(s.length() - 1);
        }
        s.append("}");

        return s.toString();
    }

    public static void main(String[] args) {
        
        double[][] table
                = {{0, 0.5, 0.4, 0, 0, 0},
                {0.5, 0, 0, 0.4, 0, 0},
                {0.4, 0, 0, 0.3, 0.5, 0},
                {0, 0.4, 0.3, 0, 0.8, 0},
                {0, 0, 0.5, 0.8, 0, 0.7},
                {0, 0, 0, 0, 0.7, 0}};

        GraphADT<String> graph = new AdjacencyListGraph<String>(GraphADT.GraphType.UNDIRECTED);
        Vertex<String> a = graph.addVertex("Anna");
        Vertex<String> b = graph.addVertex("Bill");
        Vertex<String> c = graph.addVertex("Carl");
        Vertex<String> d = graph.addVertex("Dave");
        Vertex<String> e = graph.addVertex("Emma");
        Vertex<String> f = graph.addVertex("Fred");

        Map<Integer, Vertex<String>> vertices = new HashMap<Integer, Vertex<String>>();
        vertices.put(0, a);
        vertices.put(1, b);
        vertices.put(2, c);
        vertices.put(3, d);
        vertices.put(4, e);
        vertices.put(5, f);

        Map<Edge<String>, Double> weights = new HashMap<Edge<String>, Double>();

        for (int i = 0; i < table.length; i++) {
            for (int j = i; j < table.length; j++) {
                if (table[i][j] != 0) {
                    Edge<String> edge = graph.addEdge(vertices.get(i), vertices.get(j),0);
                    weights.put(edge, -Math.log(table[i][j]));
                }
            }
        }

        System.out.println("Example Graph:\n" + graph);

        CliqueFinder2<String> clique = new CliqueFinder2<String>(graph, weights);
        clique.P = graph.vertexSet();
        clique.Bron_Kerbosch(clique.R, clique.P, clique.X);
    }

}

