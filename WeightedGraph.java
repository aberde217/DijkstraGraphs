import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//Directed Graph
public class WeightedGraph {
    private final int V;
    private ArrayList<Edge>[] adjList;

    public WeightedGraph(int V) {
        this.V = V;
        adjList = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new ArrayList<>();
        }

    }

    private void createEdge(int v, int w, int dist) {
        for (Edge e: adjList[v]) {
            if (e.w == w && e.dist == dist) return;
        }
        adjList[v].add(new Edge(w, dist));
    }
    public int DijkstraSearch(int v) {
        DijkstraPath path = new DijkstraPath(0);
        return path.distTo[v];
    }

    public int AStarSearch(int v) {
        AStar path = new AStar(0, v);
        return path.distTo[v];
    }

    public List<Integer> pathTo(int v) {
        AStar path = new AStar(0, v);
        List<Integer> pathList = new ArrayList<>();
        int L = v;
        while (L != 0) {
            pathList.add(path.edgeTo[L]);
            L = path.edgeTo[L];
        }
        Collections.reverse(pathList);
        return pathList;
    }

    protected class Pair implements Comparable<Pair> {
        private int v, disTo;
        public Pair(int v, int distTo) {
            this.v = v;
            this.disTo = distTo;
        }

        @Override
        public int compareTo(Pair o) {
            if (disTo < o.disTo) return -1;
            else if (disTo > o.disTo) return 1;
            return 0;
        }
    }
    private class Edge {
        private int w;
        private int dist;
        public Edge(int w, int dist) {
            this.w = w;
            this.dist = dist;
        }
    }

    private class DijkstraPath {
        private int[] distTo;
        private int[] edgeTo;
        public DijkstraPath(int s) {
            distTo = new int[V];
            edgeTo = new int[V];
            for (int i = 1; i < V; i++) {
                distTo[i] = Integer.MAX_VALUE;
            }
            search(s);
        }
        private void search(int s) {
            PairMinPQ minPQ = new PairMinPQ();
            for (int i = 0; i < V; i++) {
                Pair p = new Pair(i, distTo[i]);
                minPQ.add(p);
            }
            Pair smallest = minPQ.getSmallest();
            while(minPQ.size() != 0) {
                minPQ.removeSmallest();
                for (Edge e: adjList[smallest.v]) {
                    Pair pair = relaxEdge(e, smallest.v);
                    if (pair == null) continue;
                    minPQ.add(pair);
                }
                smallest = minPQ.getSmallest();
            }
        }

        private Pair relaxEdge(Edge e, int v) {
            Pair p = null;
            if (distTo[v] + e.dist < distTo[e.w]) {
                distTo[e.w] = distTo[v] + e.dist;
                edgeTo[e.w] = v;
                p = new Pair(e.w, distTo[e.w]);
            }
            return p;
        }
    }

    public class AStar {
        private int[] distTo;
        private int[] edgeTo;
        private int[] distToTarget;
        public int s, t;

        public AStar(int source, int target) {
            s = source;
            t = target;
            edgeTo = new int[V];
            distTo = new int[V];
            distToTarget = new int[V];
            for (int i = 0; i < V; i++) {
                if (i == 0) distTo[i] = 0;
                else distTo[i] = Integer.MAX_VALUE;

                if (i == t) distToTarget[i] = 0;
                else distToTarget[i] = smallestWeight(i);
            }

            search(source);
        }
        private int smallestWeight(int v) {
            int smallestDist = Integer.MAX_VALUE;
            for (Edge e: adjList[v]) {
                if (e.dist < smallestDist && e.w > v) smallestDist = e.dist;
            }
            return smallestDist;
        }

        private void search(int s) {
            PairMinPQ minPQ = new PairMinPQ();
            for (int i = 0; i < V; i++) {
                minPQ.add(new Pair(i, distTo[i]));
            }
            int smallest = minPQ.getSmallest().v;
            while(minPQ.getSmallest().v != t) {
                minPQ.removeSmallest();
                for (Edge e : adjList[smallest]) {
                    Pair p = relaxEdge(e, smallest);
                    if (p != null)  minPQ.add(p);
                }
                smallest = minPQ.getSmallest().v;
            }
        }

        private Pair relaxEdge(Edge e, int v) {
            Pair p = null;
            if (distTo[v] + e.dist < distTo[e.w]) {
                distTo[e.w] = e.dist + distTo[v];
                edgeTo[e.w] = v;
                p = new Pair(e.w, distTo[e.w] + distToTarget[e.w]);
            }
            return p;
        }

    }
}
