public class Request {
    private final String guestName;
    private final RequestTag requestTag;
    private int roomId;

    public Request(String guestName, RequestTag requestTag) {
        this.guestName = guestName;
        this.requestTag = requestTag;
        this.roomId = -1;
    }

    public Request(String guestName, RequestTag requestTag, int roomId) {
        this.guestName = guestName;
        this.requestTag = requestTag;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public RequestTag getRequestTag() {
        return requestTag;
    }

    public int getRoomId() {
        return roomId;
    }

}
