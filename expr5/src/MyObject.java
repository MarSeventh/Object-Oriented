import java.util.Objects;

public class MyObject implements Comparable<MyObject> {
    private static int totalId = 0;
    private final /*@ spec_public @*/ int id;
    private /*@ spec_public @*/ boolean referenced;

    MyObject() {
        id = totalId;
        totalId++;
        referenced = true;
    }

    //@ ensures \result == id;
    public /*@ pure @*/ int getId() {
        return id;
    }

    /*@ public normal_behavior
      @ assignable referenced;
      @ ensures !referenced;
      @*/
    public void setUnreferenced() {
        this.referenced = false;
    }

    //@ ensures \result == referenced;
    public /*@ pure @*/ boolean isReferenced() {
        return referenced;
    }

    /*@ also
      @ public normal_behavior
      @ requires this == o;
      @ ensures \result == true;
      @ also
      @ requires this != o && !(o instanceof MyObject);
      @ ensures \result == false;
      @ also
      @ requires this != o && o instanceof MyObject;
      @ ensures \result == (id == ((MyObject) o).getId() &&
      @         referenced == ((MyObject) o).isReferenced();
      @*/
    @Override
    public /*@ pure @*/ boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyObject)) {
            return false;
        }
        MyObject myObject = (MyObject) o;
        return id == myObject.getId() && referenced == myObject.isReferenced();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referenced);
    }

    /*@ also
      @ public normal_behavior
      @ requires object != null;
      @ ensures (id < object.id) ==> (\result == -1);
      @ ensures (id > object.id) ==> (\result == 1);
      @ ensures (id == object.id) ==> (\result == 0);
      @ also
      @ public exceptional_behavior
      @ signals (NullPointerException e) object == null;
      @*/
    @Override
    public int compareTo(MyObject o) {
        return Integer.compare(id, o.getId());
    }
}
