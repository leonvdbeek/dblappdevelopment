package group10.partyfinder;

        import android.util.Log;

/**
 * Created by Roy on 28/02/2018.
 *
 */

public class Party implements java.io.Serializable{
    private String name;
    private String info;
    private String date;
    private String theme;
    private String organizer;
    private String address;
    private String longitude;
    private String lattitude;

    public Party(String name, String info, String date, String theme, String organizer, String address, String longitude, String lattitude) {
        this.name = name;
        this.info = info;
        this.date = date;
        this.theme = theme;
        this.organizer = organizer;
        this.address = address;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public void printParty(){
        Log.d("my tag", "=========================================");
        Log.d("my tag", "name: "+name);
        Log.d("my tag", "info: "+info);
        Log.d("my tag", "date: "+date);
        Log.d("my tag", "theme: "+theme);
        Log.d("my tag", "organizer: "+organizer);
        Log.d("my tag", "address: "+address);
        Log.d("my tag", "longitude: "+longitude);
        Log.d("my tag", "lattitude: "+lattitude);
        Log.d("my tag", "=======================================");
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

    public String getOrganizer() {
        return organizer;
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
}
