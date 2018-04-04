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

    private ArrayList<Party> placeHolder;

    private DBSnapshot() {
        placeHolder = new ArrayList<Party>();
        Party p = new Party();
        placeHolder.add(p);
    }

    //Todo remove the hardcode userId
    private String defaultId = "114987278191137298218";
    private String userId;
    private ArrayList<Party> allParties;
    private ArrayList<Party> myParties;
    private ArrayList<Party> savedParties;
    private ArrayList<Party> todayParties;
    private ArrayList<Party> futureParties;

    public String getUserId() {
        if (userId == null){
            return defaultId;
        } else {
            return userId;
        }
    }


    public ArrayList<Party> getAllParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return allParties;
        }
    }

    public Boolean isAllPartiesReady(){
        return (!(this.allParties == null));
    }

    public ArrayList<Party> getMyParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return myParties;
        }
    }

    public ArrayList<Party> getSavedParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return savedParties;
        }
    }

    public ArrayList<Party> getTodayParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return todayParties;
        }
    }

    public ArrayList<Party> getFutureParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return futureParties;
        }
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

    private ArrayList<Party> getPlaceHolder() {

        return placeHolder;
    }

    public void addHostedParty(Party party){
        this.allParties.add(party);
    }

    public void editHostedParty(Party party){
        Party old = getParty(party.getId());
        this.allParties.remove(old);
        this.allParties.add(party);
        this.myParties.remove(old);
        this.myParties.add(party);
        this.savedParties.remove(old);
        this.savedParties.add(party);
    }
}
