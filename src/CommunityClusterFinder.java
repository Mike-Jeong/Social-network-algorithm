import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommunityClusterFinder<E> {

    Vertex[] names;
    GraphADT<String> graph = new AdjacencyListGraph<String>();
    private double[][] wtable;
    String[] name;
    Edge<String>[] edarray;
    double[] warray;

    public CommunityClusterFinder(Enum[] name, double[][] atable) {
        names = new Vertex[name.length];
        wtable = new double[name.length][name.length];
        this.name = new String[name.length];

        for (int i = 0; i < names.length; i++) {
            names[i] = graph.addVertex(String.valueOf(name[i]));
            this.name[i] = String.valueOf(name[i]);
        }

        for (int i = 0; i < atable.length; i++) {

            for (int j = i; j < atable.length; j++) {

                wtable[i][j] = -Math.log(atable[i][j]);

                if (i != j && atable[i][j] != 0) {
                    graph.addEdge(names[i], names[j], atable[i][j]);
                }
            }
        }
        Map<Edge<String>, Double> mcluster = new HashMap<Edge<String>, Double>();
        
        for (Edge<String> es : graph.edgeSet()) {

            mcluster.put(es, es.setweight());

        }


        DisjointSetsADT<String> sets = new ForestDisjointSets<String>();

        edarray = new Edge[graph.edgeSet().size()];
        warray = new double[edarray.length];

        int i = 0;
        Iterator<Edge<String>> iter = graph.edgeSet().iterator();
        
        while (iter.hasNext()) {

            Edge<String> a  = iter.next();
            warray[i] = mcluster.get(a);
            i++;    
        }




        System.out.println("");
        System.out.println("Creating singleton sets for : " + graph.vertexSet());
        System.out.println("");


        for (Vertex<String> vertex : graph.vertexSet()) {
            sets.makeSet(vertex.getUserObject());
        }

        String tempset = "";

        for (int j = 0; j < graph.edgeSet().size(); j++) {
            
            Arrays.sort(warray);

            for (Edge<String> key  : mcluster.keySet()) {
                
                if (mcluster.get(key) == warray[0]) {
                    Edge<String> edge = key;
                    warray[0] = Double.MAX_VALUE;
                    mcluster.replace(key, warray[0]);

                    sets.union(edge.endVertices()[0].getUserObject(), edge.endVertices()[1].getUserObject());

                    if (!sets.toString().equals(tempset)) {  
                        tempset = sets.toString(); 
                        System.out.println(tempset);  
                        break;
                    }

                    
                    
                }
            }
  
        }

        System.out.println(graph);

    }

}