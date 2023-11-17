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
            else if (e.w == w && e.dist != dist) {
                e.dist = dist;
                return;
            }
        }
        adjList[v].add(new Edge(w, dist));
    }

    public int distanceToVertex(int v) {
        DijkstraPath path = new DijkstraPath(0);
        return path.distTo[v];
    }

    public List<Integer> pathTo(int v) {
        DijkstraPath path = new DijkstraPath(0);
        List<Integer> pathList = new ArrayList<>();
        int L = v;
        while (L != 0) {
            pathList.add(path.edgeTo[L]);
            L = path.edgeTo[L];
        }
        Collections.reverse(pathList);
        return pathList;
    }

    private class Pair implements Comparable<Pair> {
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
            MinPQ minPQ = new MinPQ();
            for (int i = 0; i < V; i++) {
                Pair p = new Pair(i, distTo[i]);
                minPQ.add(p);
            }
            Pair smallest = minPQ.getSmallest();
            while(minPQ.size != 0) {
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
    private class MinPQ {
        private Pair[] arr;
        private int size = 0;
        private static final int INITIAL_SIZE = 10;

        public MinPQ() {
            arr = new Pair[INITIAL_SIZE];
            arr[0] = null;
        }

        private void swap(int child, int parent) {
            Pair copy = arr[parent];
            arr[parent] = arr[child];
            arr[child] = copy;
        }
        private int parentIndex(int index) {
            if (index/2 == 0) return 1;
            return index/2;
        }

        private int rightChildIndex(int index) {
            return (index * 2) + 1;
        }

        private int leftChildIndex(int index) {
            return (index * 2);
        }
        private void swimUp(int index) {
            if (index == 1);
            else if (arr[index].compareTo(arr[parentIndex(index)]) <= 0) {
                swap(index, parentIndex(index));
                swimUp(parentIndex(index));
            }
        }

        private void swimDown(int index) {
            if (arr[leftChildIndex(index)] == null && arr[rightChildIndex(index)] == null);
            else if (arr[leftChildIndex(index)] != null && arr[index].compareTo(arr[leftChildIndex(index)]) >= 0) {
                swap(leftChildIndex(index),index);
                swimDown(leftChildIndex(index));
            } else if (arr[rightChildIndex(index)] != null && arr[index].compareTo(arr[rightChildIndex(index)]) >= 0) {
                swap(rightChildIndex(index),index);
                swimDown(rightChildIndex(index));
            }
        }

        private void resizeUp() {
            Pair[] newArr = new Pair[arr.length * 2];
            System.arraycopy(arr, 0, newArr, 0, size + 1);
            arr = newArr;
        }
        private void resizeDown() {
            Pair[] newArr = new Pair[arr.length/2];
            System.arraycopy(arr, 0, newArr, 0, size + 1);
            arr = newArr;
        }
        public void add(Pair x) {
            if ((double)size/arr.length >= ((double)4/5)) {
                resizeUp();
            }
            arr[size + 1] = x;
            size += 1;
            int index = size;
            swimUp(index);
        }

        public Pair getSmallest() {
            return arr[1];
        }

        public Pair removeSmallest() {
            Pair smallest = getSmallest();
            swap(size, 1);
            arr[size] = null;
            swimDown(1);
            size--;
            if ((double)size/arr.length <= ((double)1/6)) resizeDown();
            return smallest;
        }

    }

}
