public class Controller {
    private static final Controller instance = new Controller();

    private RequestList recRequestList;
    private RequestList disRequestList;
    private RequestList cleRequestList;
    private int commandNum;
    private boolean endTag;
    private boolean isEnd;

    public void initial() {
        this.recRequestList = new RequestList();
        this.disRequestList = new RequestList();
        this.cleRequestList = new RequestList();
        this.commandNum = 0;
        this.endTag = false;
        this.isEnd = false;
        //假设有5间房，每种员工各两位
        RoomTable.getInstance().initial(5);
        Receptionist receptionist1 = new Receptionist("0", "Bob", recRequestList);
        receptionist1.start();
        Receptionist receptionist2 = new Receptionist("1", "Mike", recRequestList);
        receptionist2.start();
        Dispatcher dispatcher1 = new Dispatcher("2", "Jack", disRequestList);
        dispatcher1.start();
        Dispatcher dispatcher2 = new Dispatcher("3", "Rose", disRequestList);
        dispatcher2.start();
        Cleaner cleaner1 = new Cleaner("4", "Jenny", cleRequestList);
        cleaner1.start();
        Cleaner cleaner2 = new Cleaner("5", "Alan", cleRequestList);
        cleaner2.start();
    }

    public static Controller getInstance() {
        return instance;
    }

    public synchronized void incCommand() {
        this.commandNum++;
    }

    public synchronized void finishCommand() {
        this.commandNum--;
        if (commandNum == 0 && endTag) {
            this.isEnd = true;
            notifyAllWorkers();
        }
    }

    public synchronized void setEndTag() {
        this.endTag = true;
        if (commandNum == 0) {
            this.isEnd = true;
            notifyAllWorkers();
        }
    }

    private void notifyAllWorkers() {
        synchronized (recRequestList) {
            recRequestList.notifyAll();
        }

        synchronized (disRequestList) {
            disRequestList.notifyAll();
        }
        synchronized (cleRequestList) {
            cleRequestList.notifyAll();
        }
    }

    public synchronized boolean isEnd() {
        return this.isEnd;
    }

    //Guest -> Receptionist

    public void ask2checkIn(String guestName) {
        recRequestList.addRequest(new Request(guestName, RequestTag.GUEST_REC_CHECKIN));
    }

    public void ask2checkOut(String guestName, int roomId) {
        recRequestList.addRequest(new Request(guestName, RequestTag.GUEST_REC_CHECKOUT,
                roomId));
    }

    public void ask2clean(String guestName, int roomId) {
        recRequestList.addRequest(new Request(guestName, RequestTag.GUEST_REC_CLEAN,
                roomId));
    }

    //Receptionist -> Dispatcher

    public void arrangeCheckIn(Request request) {
        disRequestList.addRequest(new Request(request.getGuestName(), RequestTag.REC_DIS_CHECKIN));
    }

    public void arrangeCheckOut(Request request) {
        disRequestList.addRequest(new Request(request.getGuestName(), RequestTag.REC_DIS_CHECKOUT,
                request.getRoomId()));
    }

    public void arrangeClean(Request request) {
        disRequestList.addRequest(new Request(request.getGuestName(), RequestTag.REC_DIS_CLEAN,
                request.getRoomId()));
    }

    //Dispatcher -> Cleaner

    public void clean4CheckOut(Request request) {
        cleRequestList.addRequest(new Request(request.getGuestName(), RequestTag.DIS_CLE_CLEAN_CHECKOUT,
                request.getRoomId()));
    }

    public void cleanOnly(Request request) {
        cleRequestList.addRequest(new Request(request.getGuestName(), RequestTag.DIS_CLE_CLEAN_ONLY,
                request.getRoomId()));
    }
    //Dispatcher -> Receptionist

    public void checkInSuccess(Request request, int roomId) {
        recRequestList.addRequest(new Request(request.getGuestName(), RequestTag.DIS_REC_CHECKIN_SUCCESS,
                roomId));
    }

    public void checkInFailed(Request request) {
        recRequestList.addRequest(new Request(request.getGuestName(), RequestTag.DIS_REC_CHECKIN_FAILED));
    }

    public void finishCheckOut(Request request) {
        recRequestList.addRequest(new Request(request.getGuestName(), RequestTag.DIS_REC_CHECKOUT_SUCCESS));
    }

    //Cleaner -> Dispatcher

    public void cleanFinish(Request request) {
        if (RequestTag.REC_DIS_CHECKOUT.equals(request.getRequestTag())) {
            disRequestList.addRequest(new Request(request.getGuestName(), RequestTag.CLE_DIS_CLEAN_CHECKOUT,
                    request.getRoomId()));
        } else if (RequestTag.DIS_CLE_CLEAN_ONLY.equals(request.getRequestTag())) {
            disRequestList.addRequest(new Request(request.getGuestName(), RequestTag.CLE_DIS_CLEAN_ONLY,
                    request.getRoomId()));
        }
    }


}
