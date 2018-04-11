package group10.partyfinder;

import android.util.Log;

import com.google.api.client.util.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public Party(int id, String name, String info, String start, String end, String theme,
                 String creator, String address, String longitude, String lattitude) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.start = convertStringToDate(start);
        this.end = convertStringToDate(end);
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

    public Date getStartAsDate() {
        return start;
    }

    public String getPartyViewEndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm");

        return dateFormat.format(end);
    }

    public String getPartyViewStartDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(start);
    }

    public String getPartyDayDiff() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        String today = new SimpleDateFormat("dd/MM/yyyy").format(
                Calendar.getInstance().getTime());
        Date firstDate = sdf.parse(today);

        Date secondDate = sdf.parse(getPartyViewStartDate());

        long diffInMillies = secondDate.getTime() - firstDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return String.valueOf(diff);
    }

    public long getPartyTimeMs(){
        return this.start.getTime();
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

    public String getPartyStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

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

    public void setString(String start) {
        this.start = convertStringToDate(start);
    }

    public void setEnd(String end) {
        this.end = convertStringToDate(end);
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

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public double getDistance() {
        // user location
        double lat1 = 51.447573;
        double lon1 = 5.487507;

        double lat2 = Double.parseDouble(this.lattitude);
        double lon2 = Double.parseDouble(this.longitude);

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        dist = Math.round(dist * 10.0) / 10.0;

        return (dist);
    }

    // converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private Date convertStringToDate(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            Log.d("my tag", "The input date string is invalid");
        }
        return date;
    }
}
