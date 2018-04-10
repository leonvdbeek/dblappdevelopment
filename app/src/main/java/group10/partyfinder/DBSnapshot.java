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
    private boolean ready;

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

    public Boolean isallReady(){
        return (this.allParties != null);
    }

    public Boolean isDBReady(){
        return this.ready;
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

    public void deleteHostedParty(Party party){
        if(this.allParties.contains(party)){
            this.allParties.remove(party);
        }
        if(this.savedParties.contains(party)){
            this.savedParties.remove(party);
        }
        if(this.myParties.contains(party)){
            this.myParties.remove(party);
        }
        if(this.todayParties.contains(party)){
            this.todayParties.remove(party);
        }
        if(this.futureParties.contains(party)){
            this.futureParties.remove(party);
        }
    }

    public void editHostedParty(Party party){
        //local list to store parties that are to be removed
        Party toRemove = null;
        //edit the party in allParties
        for (Party oldParty : allParties){
            if (oldParty.getId() == party.getId()){
                toRemove = oldParty;
            }
        }
        if (toRemove != null) {
            this.allParties.remove(toRemove);
        }
        toRemove = null;
        this.allParties.add(party);

        //edit the party in myParties
        for (Party oldParty : myParties){
            if (oldParty.getId() == party.getId()){
                toRemove = oldParty;
            }
        }
        if (toRemove != null) {
            myParties.remove(toRemove);
        }
        toRemove = null;
        myParties.add(party);
    }

    public void addToSaveList(Party party) {
        if (!this.savedParties.contains(party)){
            this.savedParties.add(party);
        }
    }

    public void removeFromSaveList(Party party) {
        if (this.savedParties.contains(party)){
            this.savedParties.remove(party);
        }
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
