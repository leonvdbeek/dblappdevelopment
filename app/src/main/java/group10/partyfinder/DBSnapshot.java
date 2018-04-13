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



    private boolean ready;

    public static DBSnapshot getInstance() {
        return Instance;
    }

    private ArrayList<Party> placeHolder;

    /**
     * on initialization a placeholder party is created
     */
    private DBSnapshot() {
        placeHolder = new ArrayList<Party>();
        Party p = new Party();
        placeHolder.add(p);
    }

    private String defaultId = "114987278191137298218";
    private String userId;
    private ArrayList<Party> allParties;
    private ArrayList<Party> myParties;
    private ArrayList<Party> savedParties;
    private ArrayList<Party> todayParties;
    private ArrayList<Party> futureParties;

    /**
     *  a simple getter for the userId
     *
     * @return a string containting the current google user ID,
     *  if none is set a default is returned
     */
    public String getUserId() {
        if (userId != null){
            return userId;
        } else {
            return defaultId;
        }
    }

    public boolean isReady() {
        return ready;
    }

    public ArrayList<Party> getAllParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return allParties;
        }
    }

    /**
     *  a simple check to see if allparties is filled
     *
     * @return if allParties is not null
     */
    public Boolean isallReady(){
        return (this.allParties != null);
    }


    /**
     * simple getter method for my parties but returns a placeholder if myparties
     *       doesn exist
     *
     * @return an arrayList filled with the current logged in users made parties
     */
    public ArrayList<Party> getMyParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return myParties;
        }
    }

    /**
     * simple getter method for saved parties but returns a placeholder if savedparties
     *       doesn exist
     *
     * @return a arrayList of the current logged in users saved parties
     */
    public ArrayList<Party> getSavedParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return savedParties;
        }
    }

    /**
     * simple getter method for todays parties but returns a placeholder if todayparties
     *       doesn exist
     *
     * @return a arrayList of all partie happening today
     */
    public ArrayList<Party> getTodayParties() {
        if (allParties == null){
            return getPlaceHolder();
        } else {
            return todayParties;
        }
    }

    /**
     * simple getter method for future parties but returns a placeholder if futureparties
     *       doesn exist
     *
     * @return a arrayList containing all future parties
     */
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

    /**
     * a method to find a party based on its id
     *
     * @param id
     * @return {Party p : p.id == id}
     */
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

    /**
     * method to add a newly created prty to the local DB
     *
     * @param party
     * @post allParties.contains(party)
     *       myParties.contains(party)
     *       if party is today todayparties.contains(party) && futureparties.contains(party)
     *       if paty is in future futureparties.contains(party)
     */
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

    /**
     * a method to delete a party from the local DB
     *
     * @param party
     * @remove forall Party p : p.is == party.id
     */
    public void deleteHostedParty(Party party){
        //create a set of all list that need to be altered
        Set<ArrayList<Party>> groups = new HashSet<>();
        groups.add(myParties);
        groups.add(savedParties);
        groups.add(todayParties);
        groups.add(futureParties);
        groups.add(allParties);

        int targetId = party.getId();

        for(ArrayList<Party> group : groups){
            Party toRemove = null;
            //find the party in the current group
            for (Party oldParty : group){
                if (oldParty.getId() == targetId){
                    toRemove = oldParty;
                    continue;
                }
            }
            if (toRemove != null) {
                group.remove(toRemove);
            }
        }
    }

    /**
     * a method to alter all existing parties with the same id as party to party
     *
     * @param party
     * @post @forall Party p : p.id == party.id : = party
     */
    public void editHostedParty(Party party){
        //create a set of all list that need to be altered
        Set<ArrayList<Party>> groups = new HashSet<>();
        groups.add(myParties);
        groups.add(todayParties);
        groups.add(futureParties);
        groups.add(allParties);

        int targetId = party.getId();

        for(ArrayList<Party> group : groups){
            Party toRemove = null;
            //find the party in the current group
            for (Party oldParty : group){
                if (oldParty.getId() == targetId){
                    toRemove = oldParty;
                    continue;
                }
            }
            if (toRemove != null) {
                group.remove(toRemove);
                group.add(party);
            }
        }
    }

    /**
     *
     * @param party
     * @post savedParties.contains(party)
     */
    public void addToSaveList(Party party) {
        if (!this.savedParties.contains(party)){
            this.savedParties.add(party);
        }
    }

    /**
     *
     * @param party
     * @remove party from saved parties if savedparties contains party
     */
    public void removeFromSaveList(Party party) {
        if (this.savedParties.contains(party)){
            this.savedParties.remove(party);
        }
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
