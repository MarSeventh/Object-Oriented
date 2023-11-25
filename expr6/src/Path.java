import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap;

public class Path implements Iterable<Integer> {
    //@ public instance model non_null int[] nodes;
    private final ArrayList<Integer> nodes;
    private final HashSet<Integer> distinct;

    public Path(final int... nodeList) {
        nodes = new ArrayList<>(nodeList.length);
        distinct = new HashSet<>(nodeList.length);
        for (final int x : nodeList) {
            nodes.add(x);
            distinct.add(x);
        }
        System.out.println(this.toString() + " Generated!");
    }

    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }

    @Override
    public /*@ pure @*/ int hashCode() {
        return nodes.hashCode();
    }

    //@ ensures \result == nodes.length;
    public /*@ pure @*/ int size() {
        return nodes.size();
    }

    /*@ public normal_behavior
      @ requires index >= 0 && index < size();
      @ assignable \nothing;
      @ ensures \result == nodes[index];
      @
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (IndexOutOfBoundsException e) index < 0 || index >= size();
      @*/
    public /*@ pure @*/ int getNode(final int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < size()) {
            return nodes.get(index);
        } else {
            System.err.println("Index not available in getNode(int index) !");
            throw new IndexOutOfBoundsException();
        }
    }

    //@ ensures \result == (\exists int i; i >= 0 && i < size(); nodes[i] == nodeId);
    public /*@ pure @*/ boolean containsNode(final int nodeId) {
        return distinct.contains(nodeId);
    }

    //@ ensures \result == (\sum int i; 0 <= i && i < nodes.length && nodes[i] == node; 1);
    public /*@ pure @*/ int getNodeCount(int node) {
        int count = 0;
        for (Integer i : nodes) {
            if (i == node) {
                count++;
            }
        }
        return count;
    }

    /*@ ensures (\exists int[] arr;
      @            (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
      @            (\forall int i; 0 <= i && i < arr.length; this.containsNode(arr[i]))
      @            && (\forall int i; 0 <= i && i < nodes.length;
      @                 (\exists int j; 0 <= j && j < arr.length; nodes[i] == arr[j]))
      @            && \result == arr.length);
      @*/
    public /*@ pure @*/ int getDistinctNodeCount() {
        return distinct.size();
    }

    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Path;
      @ assignable \nothing;
      @ ensures \result == (((Path) obj).nodes.length == nodes.length) &&
      @                      (\forall int i; 0 <= i && i < nodes.length;
      @                              nodes[i] == ((Path) obj).nodes[i]);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Path);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    public /*@ pure @*/ boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Path)) {
            return false;
        }
        Path objPath = (Path) obj;
        if (objPath.hashCode() != this.hashCode()) {
            return false;
        }
        if (objPath.size() != this.size() ||
                objPath.getDistinctNodeCount() != this.getDistinctNodeCount()) {
            return false;
        }
        Iterator<Integer> iterator = objPath.iterator();
        Iterator<Integer> myIterator = this.iterator();
        while (iterator.hasNext() || myIterator.hasNext()) {
            if (!iterator.next().equals(myIterator.next())) {
                return false;
            }
        }
        return true;
    }

    /*@ ensures \result == (nodes.length >= 2) &&
      @         ((\sum int i; 0 <= i && i < nodes.length - 1 &&
      @         (\exists int j; i < j && j < nodes.length; nodes[i] == nodes[j]);1)<= 1);
      */
    public /*@ pure @*/ boolean isValid() {
        return (size() >= 2 && nodes.size() - distinct.size() <= 1);
    }

    /*@ public normal_behavior
      @ assignable \nothing;
      @ requires (\exists int i,j; 0 <= i && i < j && j < nodes.length; nodes[i] == nodes[j]) && isValid();
      @ ensures \result instanceof Path &&
      @           (\exists int i; 0 <= i && i < nodes.length-1;
      @           (\forall int j; 0 <= j && j < \result.nodes.length;
      @           (i + j) < nodes.length && \result.nodes[j] == nodes[i + j]))
      @           && \result.nodes[0] == \result.nodes[\result.nodes.length - 1];
      @
      @ also
      @ public normal_behavior
      @ requires (\forall int i, j; 0 <= i && i < j && j < nodes.length; nodes[i] != nodes[j]) || !isValid();
      @ assignable \nothing;
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Path extractLoopPath() {
        if (nodes.size() == distinct.size() || !isValid()) {
            return null;
        }
        HashMap<Integer, Integer> nodeToIndex = new HashMap<>();
        int index;
        int nodeFirstAppearIndex = -1;
        int loopStartIndex;
        int loopLastIndex;
        for (index = 0; index < nodes.size() &&
                !nodeToIndex.containsKey(nodes.get(index)); index++) {
            nodeFirstAppearIndex = index;
            nodeToIndex.put(nodes.get(index), nodeFirstAppearIndex);
        }
        loopStartIndex = nodeFirstAppearIndex;
        loopLastIndex = index;

        int[] loopList = new int[loopLastIndex - loopStartIndex + 1];
        for (int i = loopStartIndex; i <= loopLastIndex; i++) {
            loopList[i - loopStartIndex] = nodes.get(i);
        }
        return new Path(loopList);
    }

    /*@ ensures \result == (\exists int i, j; 0 <= i && i < j && j < nodes.length; nodes[i] == nodes[j]);
      @*/
    public /*@ pure @*/ boolean containsLoop() {
        return nodes.size() != distinct.size();
    }

    public /*@ pure &*/ boolean isUglyPath() {
        return isValid() && containsLoop() && nodes.get(0) != nodes.get(nodes.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Path : ");
        Iterator<Integer> iterator = this.iterator();
        while (iterator.hasNext()) {
            res.append(iterator.next().toString());
            if (iterator.hasNext()) {
                res.append("->");
            }
        }
        return res.toString();
    }
}
