package group10.partyfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
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

public class CreateParty extends AppCompatActivity {

    // Get the instance of our database
    private DBSnapshot DB = DBSnapshot.getInstance();

    private View view;
    private Party partyObject;
    private EditText ETname;
    private EditText ETdescription;
    private EditText ETstartDate;
    private EditText ETstartTime;
    private String startTime;
    private EditText ETendDate;
    private EditText ETendTime;
    private String endTime;
    private EditText ETtheme;
    private EditText ETaddress;

    double longitude;
    double latitude;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        // Create toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title toolbar
        setTitle("Create party");

        // Make variable for snackbar
        view = findViewById(R.id.mainLayout);

        ETname = findViewById(R.id.ETname);
        ETdescription = findViewById(R.id.ETdescription);
        ETstartDate = findViewById(R.id.ETstartDate);
        ETstartTime = findViewById(R.id.ETstartTime);
        ETendDate = findViewById(R.id.ETendDate);
        ETendTime = findViewById(R.id.ETendTime);
        ETtheme = findViewById(R.id.ETtheme);
        ETaddress = findViewById(R.id.ETaddress);

    }

    public void createParty(View v) {
        startTime = ETstartDate.getText().toString() + "T" + ETstartTime.getText().toString() + ":00+00:00";
        endTime = ETendDate.getText().toString() + "T" + ETendTime.getText().toString() + ":00+00:00";
        //Snackbar.make(view, startTime, Snackbar.LENGTH_LONG).show();

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
            // Create party
            Address address = addresses.get(0);

            DecimalFormat df = new DecimalFormat("##.#######");
            longitude = Double.parseDouble(df.format(address.getLongitude()));
            latitude = Double.parseDouble(df.format(address.getLatitude()));


            partyObject = new Party(
                    0,
                    ETname.getText().toString(),
                    ETdescription.getText().toString(),
                    startTime,
                    endTime,
                    ETtheme.getText().toString(),
                    DB.getUserId(),
                    ETaddress.getText().toString(),
                    String.valueOf(longitude),
                    String.valueOf(latitude)
            );

            partyObject.printParty();
            postParty(partyObject);
            Snackbar.make(view, "The party is created!", Snackbar.LENGTH_LONG).show();
        }
    }



    // Called when go back arrow (in the left top) is pressed.
    // Go back to previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Method to post a party to the server
    private void postParty(Party party){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        //call for all parties
        Call<Party> call = client2.hostParty(party);
        Log.d("my tag", "Post request body: " + party.getId());
        call.enqueue(new Callback<Party>() {
            @Override
            public void onResponse(Call<Party> call, Response<Party> response) {
                if (response.body() != null) {
                    Log.d("my tag", "Post response code: " + response.code());

                    DB.addHostedParty(response.body());
                    openPartyViewActivity(response.body().getId());
                }
                Log.d("my tag", "Posting party resulted empty: " + response.code());
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "Posting party to DB has failed ");
            }
        });
    }



    // A method to open a specified PartyView activity
    public void openPartyViewActivity(int n) {
        Intent i = new Intent("android.intent.action.PartyView");
        i.putExtra("ID", n);
        this.startActivity(i);
    }
}
