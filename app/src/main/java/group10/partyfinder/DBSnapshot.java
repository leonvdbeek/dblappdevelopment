package group10.partyfinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        this.myParties.add(party);

        //compare the date of the created party
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(party.getStartAsDate());
        cal2.setTime(new Date());
        if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
                this.todayParties.add(party);
            }
            if (cal1.get(Calendar.DAY_OF_YEAR) >= cal2.get(Calendar.DAY_OF_YEAR)){
                this.futureParties.add(party);
            }
        } else if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) {
            this.futureParties.add(party);
        }
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
        //create a set of all list that need to be altered
        Set<ArrayList<Party>> groups = new HashSet<>();
        groups.add(myParties);
        groups.add(todayParties);
        groups.add(futureParties);
        groups.add(allParties);

        for(ArrayList<Party> group : groups){
            Party toRemove = null;
            //find the party in the current group
            for (Party oldParty : group){
                if (oldParty.getId() == party.getId()){
                    toRemove = oldParty;
                }
            }
            if (toRemove != null) {
                group.remove(toRemove);
                group.add(party);
            }
        }
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
