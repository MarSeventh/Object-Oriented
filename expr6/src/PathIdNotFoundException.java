class PathIdNotFoundException extends Exception {

    private static int count = 0;
    private int id;

    PathIdNotFoundException(final int pathId) {
        count = count + 1;
        this.id = pathId;
    }

    public void print() {
        System.err.println("PathId : " + id + " Not Found!" +
                String.format(" (This exception has occurred %d times)", count));
    }
}
