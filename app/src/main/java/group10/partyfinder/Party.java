package group10.partyfinder;

import android.util.Log;

import java.util.Date;

/**
 * Created by Roy on 28/02/2018.
 *
 */

public class Party implements java.io.Serializable{
    private String id;
    private String creator;
    private String name;
    private String info;
    private String date;
    private String theme;
    private String address;
    private String longitude;
    private String lattitude;

    public Party(String name, String info, String date, String theme, String creator, String address, String longitude, String lattitude) {
        this.name = name;
        this.info = info;
        this.date = date;
        this.theme = theme;
        this.creator = creator;
        this.address = address;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public void printParty(){
        Log.d("my tag", "=========================================");
        Log.d("my tag", " party id: "+id);
        Log.d("my tag", "  creator: "+creator);
        Log.d("my tag", "     name: "+name);
        Log.d("my tag", "     info: "+info);
        Log.d("my tag", "     date: "+date);
        Log.d("my tag", "    theme: "+theme);
        Log.d("my tag", "  address: "+address);
        Log.d("my tag", "longitude: "+longitude);
        Log.d("my tag", "lattitude: "+lattitude);
        Log.d("my tag", "=======================================");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
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

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
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
}
