import java.util.Arrays;
import java.util.List;

public class JvmHeap extends MyHeap<MyObject> {
    JvmHeap(int capacity) {
        super(capacity);
    }

    /*@ public normal_behavior
      @ requires objectId != null;
      @ assignable elements[*].referenced;
      @ ensures size == \old(size);
      @ ensures (\forall int i; 1 <= i && i <= size &&
      @          (\forall int j; 0 <= j && j < objectId.size(); elements[i].getId() != objectId.get(j));
      @           elements[i].equals(\old(elements[i])));
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          (\exists int j; 0 <= j && j < objectId.size();
      @            objectId.get(j) == elements[i].getId()) ==>  (!elements[i].isReferenced()));
      @*/
    public void setUnreferencedId(List<Integer> objectId) {
        for (int id : objectId) {
            for (int i = 1; i <= this.getSize(); i++) {
                MyObject myObject =  this.getElement(i);
                if (myObject.getId() == id) {
                    myObject.setUnreferenced();
                    setElementData(i, myObject);
                }
            }
        }
    }

    /*@ public normal_behavior
      @ assignable elements, size;
      @ ensures size == (\sum int i;i >= 1 && i <= \old(size) && \old(elements[i].isReferenced());1); 要求在调用方法后，heap的size等于之前elements数组中被引用的element的数量之和，必须使用"\sum"
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          \old(elements[i].isReferenced()) ==>
      @           (\exists int j; 1 <= j && j <= size; elements[j].equals(\old(getElement(i)))));
      @ ensures (\forall int i; 1 <= i && i <= \old(size);
      @          !(\old(elements[i].isReferenced())) ==>
      @           (\forall int j; 1 <= j && j <= size;
      @           !elements[j].equals(\old(elements[i]))));
      @ ensures (\forall int i; 1 <= i && i <= size;
      @          (\exists int j; 1 <= j && j <= \old(size);
      @          elements[i].equals(\old(elements[j]))));
      @*/
    public void removeUnreferenced() {
        Object[] elements = getElementData();
        int newSize = 0;
        for (int i = 1; i <= getSize(); ++i) {
            MyObject obj = ((MyObject) elements[i]);
            if (obj.isReferenced()) {
                elements[++newSize] = obj;
            }
        }
        setSize(newSize);
        if (newSize > 1) {
            Arrays.sort(elements, 1, newSize + 1);
        }
    }

    /*@ public normal_behavior
      @ requires size > 0;
      @ ensures (\forall int i; 1 <= i && i <= size; \result.compareTo(elements[i]) <= 0);
      @ ensures (\exists int i; 1 <= i && i <= size; \result == elements[i]);
      @ also
      @ public normal_behavior
      @ requires size == 0;
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ MyObject getYoungestOne() {
        if (getSize() == 0) {
            return null;
        }
        Object[] elements = getElementData();
        MyObject obj = (MyObject) elements[1];
        for(int i = 1;i <= getSize();i++){
            if(obj.compareTo((MyObject) elements[i]) >= 0){
                obj = (MyObject) elements[i];
            }
        }
        return obj;
    }
}