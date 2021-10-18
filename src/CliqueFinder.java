import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CliqueFinder<E> {

    GraphADT<String> graphForDegeneracy = new AdjacencyListGraph<String>();
    Vertex[] names;
    GraphADT<E> graph = new AdjacencyListGraph<E>();
    private double[][] wtable;
    String[] name;
    private Set<Vertex<E>> R, P, X;
    int max;
    List<Set<Vertex<E>>> maxs = new LinkedList<>();

    public CliqueFinder(Enum[] name, double[][] atable) {
        names = new Vertex[name.length];
        wtable = new double[name.length][name.length];
        this.name = new String[name.length];

        for (int i = 0; i < names.length; i++) {
            names[i] = graph.addVertex((E) name[i]);
            this.name[i] = String.valueOf(name[i]);
        }

        for (int i = 0; i < atable.length; i++) {

            for (int j = i + 1; j < atable.length; j++) {

                wtable[i][j] = -Math.log(atable[i][j]);

                if (i != j && atable[i][j] != 0) {
                    graph.addEdge(names[i], names[j], atable[i][j]);
                }
            }
        }



        R = new HashSet<Vertex<E>>();
        P = new HashSet<Vertex<E>>();
        X = new HashSet<Vertex<E>>();

        P = graph.vertexSet();
        Bron_KerboschWithPivot(R, P, X, "");


        for (int i = 0; i < maxs.size(); i++) {
            printClique(maxs.get(i));
        }

        System.out.println("Maximal clique size = " + max);
        System.out.println("==============================================");
        System.out.println(graph);

    }

    private void Bron_KerboschWithPivot(Set<Vertex<E>> R, Set<Vertex<E>> P, Set<Vertex<E>> X, String pre) {

        System.out.print(pre + " " + printSet(R) + ", " + printSet(P) + ", " + printSet(X));
        if ((P.size() == 0) && (X.size() == 0)) {

            if (max <= R.size()) {
                if (max != R.size()) {
                    maxs.clear();
                }
                max = R.size();
                Set<Vertex<E>> result = new HashSet<Vertex<E>>();
                for (Vertex<E> v : R) {

                    result.add(v);
                }
                maxs.add(result);

                System.out.println();
                return;
            }
        }
        System.out.println();

        Set<Vertex<E>> P1 = new HashSet<Vertex<E>>(P);

        for (Vertex<E> v : P) {
            R.add(v);
            Bron_KerboschWithPivot(R, intersect(P1, v.adjacentVertices()), intersect(X, v.adjacentVertices()), ///
                    pre + "\t");
            R.remove(v);
            P1.remove(v);
            X.add(v);
        }

    }

   

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

}
