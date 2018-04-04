package group10.partyfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mark.
 *
 */

public class EditParty extends AppCompatActivity {

    // Get the database
    private DBSnapshot DB = DBSnapshot.getInstance();

    private int partyID;
    private Party partyObject;
    private Party savePartyObject;

    private View view;
    private EditText ETname;
    private EditText ETdescription;
    private EditText ETstartDate;
    private EditText ETstartTime;
    private String[] startTime;
    private EditText ETendDate;
    private EditText ETendTime;
    private String[] endTime;
    private EditText ETtheme;
    private EditText ETaddress;
    private String saveStartTime;
    private String saveEndTime;

    double longitude;
    double latitude;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_party);

        // Create toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title toolbar
        setTitle("Edit party");

        // Make variable for snackbar
        view = findViewById(R.id.mainLayout);

        // Get party object
        partyID = getIntent().getIntExtra("ID", 0);
        partyObject = DB.getParty(partyID);


        ETname = findViewById(R.id.ETname);
        ETdescription = findViewById(R.id.ETdescription);
        ETstartDate = findViewById(R.id.ETstartDate);
        ETstartTime = findViewById(R.id.ETstartTime);
        ETendDate = findViewById(R.id.ETendDate);
        ETendTime = findViewById(R.id.ETendTime);
        ETtheme = findViewById(R.id.ETtheme);
        ETaddress = findViewById(R.id.ETaddress);

        // Set values in editText
        ETname.setText(partyObject.getName(), TextView.BufferType.EDITABLE);
        ETaddress.setText(partyObject.getAddress(), TextView.BufferType.EDITABLE);
        ETtheme.setText(partyObject.getTheme(), TextView.BufferType.EDITABLE);
        ETdescription.setText(partyObject.getInfo(), TextView.BufferType.EDITABLE);

        startTime = partyObject.getPartyViewDate().split(" ");
        ETstartDate.setText(startTime[0], TextView.BufferType.EDITABLE);
        ETstartTime.setText(startTime[1], TextView.BufferType.EDITABLE);

        endTime= partyObject.getPartyViewEndDate().split(" ");
        ETendDate.setText(endTime[0], TextView.BufferType.EDITABLE);
        ETendTime.setText(endTime[1], TextView.BufferType.EDITABLE);
    }

    public void startEditParty(View v) {
        saveStartTime = ETstartDate.getText().toString() + "T" + ETstartTime.getText().toString() + ":00+00:00";
        saveEndTime = ETendDate.getText().toString() + "T" + ETendTime.getText().toString() + ":00+00:00";
        //Snackbar.make(view, saveStartTime, Snackbar.LENGTH_LONG).show();

        // Get longitude and latitude from address
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(ETaddress.getText().toString(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // If address is empty
        if (addresses.isEmpty()) {

            AlertDialog ADFalseAdress = new AlertDialog.Builder(context).create();
            ADFalseAdress.setTitle("Address not recognized");
            ADFalseAdress.setMessage("The address is not recognized. Change the address and try again.");
            ADFalseAdress.setCancelable(false);
            ADFalseAdress.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ADFalseAdress.cancel();
                        }
                    });
            ADFalseAdress.show();

        } else {
            // Edit party
            Address address = addresses.get(0);

            longitude = address.getLongitude();
            latitude = address.getLatitude();
            //Snackbar.make(view, "Long: " + String.valueOf(longitude) + " and Lat: " + String.valueOf(latitude), Snackbar.LENGTH_LONG).show();

            savePartyObject = new Party(
                    0,
                    ETname.getText().toString(),
                    ETdescription.getText().toString(),
                    saveStartTime,
                    saveEndTime,
                    ETtheme.getText().toString(),
                    "114987278191137298218",
                    ETaddress.getText().toString(),
                    String.valueOf(longitude),
                    String.valueOf(latitude)
            );

            partyObject.printParty();
            editParty(partyObject);
            Snackbar.make(view, "The party has been edited!", Snackbar.LENGTH_LONG).show();
        }
    }

    // Method to post the edited party to the server
    private void editParty(Party party){
        party.printParty();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        //call for all parties
        Call<Party> call = client2.editParty(Integer.toString(party.getId()), party);
        Log.d("my tag", "Put request body: " + party.getId());
        call.enqueue(new Callback<Party>() {
            @Override
            public void onResponse(Call<Party> call, Response<Party> response){
                Log.d("my tag", "Put response code: " + response.code());
                Party party = response.body();
                Log.d("my tag", "Put party id: " + party.getId());
                party.printParty();
                DB.editHostedParty(party);
                Snackbar.make(view, "Server response: OK!", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "Put party request has failed");
            }
        });
    }

    // Called when go back arrow (in the left top) is pressed.
    // Go back to previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
