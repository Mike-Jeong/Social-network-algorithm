

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForestDisjointSets<E> implements DisjointSetsADT<E> {

    private List<Node<E>> repNodes; // root for each tree in forest
    private Map<Node<E>, Integer> setRanks; //each repNode gives set rank
    private Map<E, Node<E>> elementMap; // map of elements to locators

    public ForestDisjointSets() {
        repNodes = new ArrayList<Node<E>>();
        setRanks = new HashMap<Node<E>, Integer>();
        elementMap = new HashMap<E, Node<E>>();
    }

    public E makeSet(E x) {
        if (elementMap.containsKey(x)) {
            throw new IllegalArgumentException("element already used");
        }
        Node<E> node = new Node<E>(x);
        node.parentNode = node; // parent of the new node is itself  
        repNodes.add(node); // add the root of the new tree to the list
        setRanks.put(node, new Integer(0)); // initial rank is zero
        elementMap.put(x, node); // add the new element to the map
        return x;
    }

    public E union(E x, E y) {
        Node<E> nodeX = elementMap.get(x);
        Node<E> nodeY = elementMap.get(y);
        if (nodeX == null && nodeY == null) {
            return null; // neither element is in any set
        }
        if (nodeX == null) {
            return getRootNode(nodeY).x; // x was not in any set
        } else if (nodeY == null) {
            return getRootNode(nodeX).x; // y was not in any set
        }
        Node<E> repX = getRootNode(nodeX);
        Node<E> repY = getRootNode(nodeY);
        if (repX == repY) {
            return repX.x; // same set
        } else // add set with smaller rank to larger set for efficiency
        {
            int rankX = setRanks.get(repX).intValue();
            int rankY = setRanks.get(repY).intValue();
            if (rankX < rankY) {
                return link(repY, repX); // add set with x to set with y
            } else {
                return link(repX, repY); // add set with y to set with x
            }
        }
    }

    // helper method that returns root node of tree with specified node
    private Node<E> getRootNode(Node<E> node) {
        while (node.parentNode != node) {
            node = node.parentNode;
        }
        return node;
    }

    // helper method adds second non-empty set to first where each set
    // is specified by the node of its representative
    private E link(Node<E> repX, Node<E> repY) {  // add the tree rooted at repY as a child of tree rooted at repX
        repY.parentNode = repX;
        // update the map of set ranks and list of repNodes
        int rankX = setRanks.get(repX).intValue();
        int rankY = setRanks.get(repY).intValue();
        if (rankX == rankY) {
            setRanks.put(repX, new Integer(++rankX));//add 1 to setX rank
        }
        setRanks.remove(repY);
        repNodes.remove(repY); // setY no longer exists
        return repX.x;
    }

    public E findSet(E x) {
        Node<E> node = elementMap.get(x);
        if (node == null) {
            return null; // element not in any set
        } else // element is in a set
        {
            return pathCompress(node).x; // return representative of set
        }
    }

    // recursive helper method that path compresses path up from node
    // to root so all nodes along path are now children of root
    // returns the eventual parent node (root node) of node
    private Node<E> pathCompress(Node<E> node) {
        if (node.parentNode == node) {
            return node; // node is the root node
        }
        Node<E> rootNode = pathCompress(node.parentNode);
        node.parentNode = rootNode;
        return rootNode;
    }

    public String toString() {
        String output = "Sets: ";
        // determine each set
        Map<Node<E>, List<E>> setsMap = new HashMap<Node<E>, List<E>>();
        // create a list to hold each set
        for (Node<E> repNode : repNodes) {
            setsMap.put(repNode, new ArrayList<E>());
        }
        Set<Map.Entry<E, Node<E>>> entries = elementMap.entrySet();
        for (Map.Entry<E, Node<E>> entry : entries) {
            List<E> set = setsMap.get(getRootNode(entry.getValue()));
            set.add(entry.getKey());
        }
        for (Node<E> repNode : repNodes) {
            List<E> set = setsMap.get(repNode);
            output += "{";
            Iterator<E> iterator = set.iterator();
            while (iterator.hasNext()) {
                output += iterator.next().toString();
                if (iterator.hasNext()) {
                    output += ",";
                }
            }
            output += "}(rank=" + setRanks.get(repNode) + ") ";
        }
        return output + "\n";
    }

    // inner class that represents a node in one of the trees
    private class Node<E> {

        public E x; // element held in the node
        public Node<E> parentNode; // link to parent node in tree

        public Node(E x) {
            this.x = x;
            parentNode = null;
        }
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
                    Edge<String> edge = graph.addEdge(vertices.get(i), vertices.get(j),table[i][j]);
                    weights.put(edge, -Math.log(table[i][j]));
                }
            }
        }

        System.out.println("Example Graph:\n" + graph);

        DisjointSetsADT<String> sets = new ForestDisjointSets<String>();
        System.out.println("Creating singleton sets for Anna,Bill,Carl,Dave,Emma,Fred");

        for (Vertex<String> vertex : graph.vertexSet()) {
            sets.makeSet(vertex.getUserObject());
        }
        System.out.println(sets);

        System.out.println("Union {Dave} with {Emma}");
        String de = sets.union(d.getUserObject(), e.getUserObject());
        System.out.println(sets);

        System.out.println("Union {Dave,Emma} with {Fred}");
        String def = sets.union(de, f.getUserObject());
        System.out.println(sets);

        System.out.println("Union {Anna} with {Bill}");
        String ab = sets.union(a.getUserObject(), b.getUserObject());
        System.out.println(sets);

        System.out.println("Union {Carl} with {Dave,Emma,Fred}");
        String cdef = sets.union(c.getUserObject(), def);
        System.out.println(sets);
        
         System.out.println("Union {Anna,Bill} with {Carl,Dave,Emma,Fred}");
        String abcdef = sets.union(ab, cdef);
        System.out.println(sets);
    }
}
