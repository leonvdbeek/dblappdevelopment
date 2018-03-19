package group10.partyfinder;

import java.util.ArrayList;

/**
 * Created by Roy on 19/03/2018.
 */

public class DBSnapshot {
    private static final DBSnapshot Instance = new DBSnapshot();

    public static DBSnapshot getInstance() {
        return Instance;
    }

    private DBSnapshot() {}

    //Todo remove the hardcode userId
    private String userId = "114987278191137298218";
    private ArrayList<Party> allParties;
    private ArrayList<Party> myParties;
    private ArrayList<Party> savedParties;
    private ArrayList<Party> todayParties;
    private ArrayList<Party> futureParties;

    public String getUserId() {
        return userId;
    }

    public ArrayList<Party> getAllParties() {
        return allParties;
    }

    public ArrayList<Party> getMyParties() {
        return myParties;
    }

    public ArrayList<Party> getSavedParties() {
        return savedParties;
    }

    public ArrayList<Party> getTodayParties() {
        return todayParties;
    }

    public ArrayList<Party> getFutureParties() {
        return futureParties;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAllParties(ArrayList<Party> allParties) {
        this.allParties = allParties;
    }

    public void setMyParties(ArrayList<Party> myParties) {
        this.myParties = myParties;
    }

    public void setSavedParties(ArrayList<Party> savedParties) {
        this.savedParties = savedParties;
    }

    public void setTodayParties(ArrayList<Party> todayParties) {
        this.todayParties = todayParties;
    }

    public void setFutureParties(ArrayList<Party> futureParties) {
        this.futureParties = futureParties;
    }

    public Party getParty(int id){
        for (Party party : allParties){
            if (party.getId() == id){
                return party;
            }
        }
        return null;
    }
}
