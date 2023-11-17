public class PairMinPQ {
    private WeightedGraph.Pair[] arr;
    private int size = 0;
    private static final int INITIAL_SIZE = 10;

    public PairMinPQ() {
        arr = new WeightedGraph.Pair[INITIAL_SIZE];
        arr[0] = null;
    }

    private void swap(int child, int parent) {
        WeightedGraph.Pair copy = arr[parent];
        arr[parent] = arr[child];
        arr[child] = copy;
    }

    private int parentIndex(int index) {
        if (index / 2 == 0) return 1;
        return index / 2;
    }

    private int rightChildIndex(int index) {
        return (index * 2) + 1;
    }

    private int leftChildIndex(int index) {
        return (index * 2);
    }

    private void swimUp(int index) {
        if (index == 1) ;
        else if (arr[index].compareTo(arr[parentIndex(index)]) <= 0) {
            swap(index, parentIndex(index));
            swimUp(parentIndex(index));
        }
    }

    private void swimDown(int index) {
        if (arr[leftChildIndex(index)] == null && arr[rightChildIndex(index)] == null) ;
        else if (arr[leftChildIndex(index)] != null && arr[index].compareTo(arr[leftChildIndex(index)]) >= 0) {
            swap(leftChildIndex(index), index);
            swimDown(leftChildIndex(index));
        } else if (arr[rightChildIndex(index)] != null && arr[index].compareTo(arr[rightChildIndex(index)]) >= 0) {
            swap(rightChildIndex(index), index);
            swimDown(rightChildIndex(index));
        }
    }

    private void resizeUp() {
        WeightedGraph.Pair[] newArr = new WeightedGraph.Pair[arr.length * 2];
        System.arraycopy(arr, 0, newArr, 0, size + 1);
        arr = newArr;
    }

    private void resizeDown() {
        WeightedGraph.Pair[] newArr = new WeightedGraph.Pair[arr.length / 2];
        System.arraycopy(arr, 0, newArr, 0, size + 1);
        arr = newArr;
    }

    public void add(WeightedGraph.Pair x) {
        if ((double) size / arr.length >= ((double) 4 / 5)) {
            resizeUp();
        }
        arr[size + 1] = x;
        size += 1;
        int index = size;
        swimUp(index);
    }

    public WeightedGraph.Pair getSmallest() {
        return arr[1];
    }

    public WeightedGraph.Pair removeSmallest() {
        WeightedGraph.Pair smallest = getSmallest();
        swap(size, 1);
        arr[size] = null;
        swimDown(1);
        size--;
        if ((double) size / arr.length <= ((double) 1 / 6)) resizeDown();
        return smallest;
    }

    public int size() {
        return size;
    }
}
