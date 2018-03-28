package group10.partyfinder;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mark.
 *
 */

public class HostParty extends AppCompatActivity {

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
    private EditText ETlongitude;
    private EditText ETlattitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_party);

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
        ETlongitude = findViewById(R.id.ETlongitude);
        ETlattitude = findViewById(R.id.ETlattitude);

    }

    public void createParty(View v) {
        startTime = ETstartDate.getText().toString() + "T" + ETstartTime.getText().toString() + ":00+00:00";
        endTime = ETendDate.getText().toString() + "T" + ETendTime.getText().toString() + ":00+00:00";
        //Snackbar.make(view, startTime, Snackbar.LENGTH_LONG).show();

        partyObject = new Party(
                0,
                ETname.getText().toString(),
                ETdescription.getText().toString(),
                startTime,
                endTime,
                ETtheme.getText().toString(),
                "114987278191137298218",
                ETaddress.getText().toString(),
                ETlongitude.getText().toString(),
                ETlattitude.getText().toString()
        );
        partyObject.printParty();
        postParty(partyObject);
        Snackbar.make(view, "The party is created!", Snackbar.LENGTH_LONG).show();
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
            public void onResponse(Call<Party> call, Response<Party> response){
                Log.d("my tag", "Post response code: " + response.code());
                Party party = response.body();
                Log.d("my tag", "Posted party id: " + party.getId());
                Log.d("my tag", "Contents" + DB.getAllParties().size()
                        + DB.getMyParties().size()
                        + DB.getSavedParties().size());

                DB.addHostedParty(party);
                openPartyViewActivity(party.getId());
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){

            }
        });
    }

    // Method to post the edited party to the server
    private void editParty(Party party){
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

                DB.addHostedParty(party);
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "Put party request has failed");
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
