import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CliqueFinder<E> {

    GraphADT<String> graphForDegeneracy = new AdjacencyListGraph<String>();
    Vertex[] names;
    GraphADT<E> graph = new AdjacencyListGraph<E>();
    private double[][] wtable;
    String[] name;
    private Set<Vertex<E>> R, P, X;
    int max;
    Set<Vertex<E>> maxs;

    public CliqueFinder(Enum[] name, double[][] atable) {
        names = new Vertex[name.length];
        wtable = new double[name.length][name.length];
        this.name = new String[name.length];
       
        for (int i = 0; i < names.length; i++) {
            names[i] = graph.addVertex((E)name[i]);
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

        printClique(maxs);
        System.out.println(max);
           
        

    }

    private void Bron_KerboschWithPivot(Set<Vertex<E>> R, Set<Vertex<E>> P, Set<Vertex<E>> X, String pre) {

        System.out.print(printSet(R) + ", " + printSet(P) + ", " + printSet(X));
        if ((P.size() == 0) && (X.size() == 0)) {

            if (max < R.size()) {

                max = R.size();
                maxs = new HashSet<Vertex<E>>();
                for (Vertex<E> v : R) {
                   
                    maxs.add(v);  
                }
                System.out.println();
                return;   
            }
        }
        System.out.println();

        Set<Vertex<E>> P1 = new HashSet<Vertex<E>>(P);

        //Vertex<E> u = getMaxDegreeVertex(union(P, X));//

        //P = removeNbrs(P, u);

        for (Vertex<E> v : P) {
            R.add(v);
            Bron_KerboschWithPivot(R, intersect(P1, v.adjacentVertices()), 
                     intersect(X, v.adjacentVertices()),///
                    pre + "\t");
            R.remove(v);
            P1.remove(v);
            X.add(v);
        }
    }

    Set<Vertex<E>> union(Set<Vertex<E>> setFirst, Set<Vertex<E>> setSecond) {
        Set<Vertex<E>> setHold = setFirst;
        setHold.addAll(setSecond);
        return setHold;
    }

    Vertex<E> getMaxDegreeVertex(Set<Vertex<E>> g) {

        Vertex<E> a = null;
        String[] b = new String[g.size()];
        Vertex<E>[] v = new Vertex[g.size()];
        int i = 0;

        
        Iterator<Vertex<E>> iter = g.iterator(); 
        System.out.println();
        while (iter.hasNext()) { 

            v[i] = iter.next();
            b[i] = String.valueOf(v[i].getUserObject());
            i++;
        }

        Arrays.sort(b);

        for (Vertex<E> vertex : v) {

            if (vertex.getUserObject().toString() == b[g.size()-1]) {

                a = vertex;
                
            }

            }
            
        return a;
    }

    Set<Vertex<E>> removeNbrs(Set<Vertex<E>> arlFirst, Vertex<E> v) {
        Set<Vertex<E>> arlHold = new HashSet<>(arlFirst);
        arlHold.removeAll(v.adjacentVertices());
        return arlHold;
    }

    
    private Set<Vertex<E>> intersect(Set<Vertex<E>> set1, Set<Vertex<E>> set2) {
        Set<Vertex<E>> intersect = new HashSet<Vertex<E>>(set1);
        intersect.retainAll(set2);
        return intersect;
    }

    ArrayList<Vertex> getDegeneracyOrdering() {

        GraphADT<String> s = new AdjacencyListGraph<String>();
        ArrayList<Vertex> graphSorted = new ArrayList<Vertex>();
        Set<Vertex<String>> t = graphForDegeneracy.vertexSet();
        String[] c = new String[t.size()];
        int i = 0;

        Iterator<Vertex<String>> iter = t.iterator(); 
        while (iter.hasNext()) { 

            c[i] = iter.next().getUserObject();
            i++;
        }

        Arrays.sort(c);

        for (int j = 0; j < c.length; j++) {
            graphSorted.add(s.addVertex(c[j]));
        }

        return graphSorted;
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
