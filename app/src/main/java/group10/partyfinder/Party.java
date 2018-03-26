package group10.partyfinder;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Roy on 28/02/2018.
 *
 */

public class Party implements java.io.Serializable{
    private int id;
    private String creator;
    private String name;
    private String info;
    private Date start;
    private Date end;
    private String theme;
    private String address;
    private String longitude;
    private String lattitude;

    public Party(){
        this.id = 0;
        this.name = "None";
        this.info = "None";
        this.start = new Date();
        this.end = new Date();
        this.theme = "None";
        this.creator = "114987278191137298218";
        this.address = "None";
        this.longitude = "0";
        this.lattitude = "0";
    }

    public Party(int id, String name, String info, Date date, String theme, String creator, String address, String longitude, String lattitude) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.start = date;
        this.end = date;
        this.theme = theme;
        this.creator = creator;
        this.address = address;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public void printParty(){
        Log.d("my tag", "=========================================");
        Log.d("my tag", "  party id: "+id);
        Log.d("my tag", "   creator: "+creator);
        Log.d("my tag", "      name: "+name);
        Log.d("my tag", "      info: "+info);
        Log.d("my tag", "start date: "+start);
        Log.d("my tag", "  end date: "+end);
        Log.d("my tag", "     theme: "+theme);
        Log.d("my tag", "   address: "+address);
        Log.d("my tag", " longitude: "+longitude);
        Log.d("my tag", " lattitude: "+lattitude);
        Log.d("my tag", "=======================================");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

        return dateFormat.format(start);
    }

    public String getPartyViewEndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");

        return dateFormat.format(end);
    }

    public String getTheme() {
        return theme;
    }

    public String getCreator() {
        return creator;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return Double.parseDouble(longitude);
    }

    public double getLattitude() {
        return Double.parseDouble(lattitude);
    }

    public String getPartyViewDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");

        return dateFormat.format(start);
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.start = start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public Party clone(){
        Party party = new Party();
        party.setId(this.id);
        party.setInfo(this.info);
        party.setName(this.name);
        party.setDate(this.start);
        party.setTheme(this.theme);
        party.setCreator(this.creator);
        party.setAddress(this.address);
        party.setLongitude(this.longitude);
        party.setLattitude(this.lattitude);
        return party;
    }
}
