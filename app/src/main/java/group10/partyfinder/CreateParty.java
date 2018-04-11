package group10.partyfinder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String [] startDate;
    private String [] endDate;

    String longitude;
    String latitude;

    private Context context = this;

    Calendar myCalendar = Calendar.getInstance();
    Calendar mcurrentTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        //first check connection
        internetConnectionCheck();

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


        // Datepicker for start date
        DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Update
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                ETstartDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        ETstartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateParty.this,
                        sDate,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Datepicker for end date
        DatePickerDialog.OnDateSetListener eDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Update
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                ETendDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        ETendDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateParty.this,
                        eDate,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Timepicker for start time
        ETstartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateParty.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mcurrentTime.set(Calendar.MINUTE, selectedMinute);

                        // Update
                        ETstartTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        // Timpicker for end time
        ETendTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateParty.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mcurrentTime.set(Calendar.MINUTE, selectedMinute);

                        // Update
                        ETendTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }



    public void createParty(View v) {
        //first check connection
        internetConnectionCheck();

        startDate = ETstartDate.getText().toString().split("-");
        endDate = ETendDate.getText().toString().split("-");

        startTime = startDate[2] + "-" + startDate[1] + "-" + startDate[0]
                + "T" + ETstartTime.getText().toString() + ":00+02:00";
        endTime = endDate[2] + "-" + endDate[1] + "-" + endDate[0]
                + "T" + ETendTime.getText().toString() + ":00+02:00";
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
            ADFalseAdress.setMessage("The address is not recognized." +
                    " Change the address and try again.");
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

            DecimalFormat df = new DecimalFormat("###.#######");

            longitude = df.format(address.getLongitude()).replaceAll(",",".");
            latitude = df.format(address.getLatitude()).replaceAll(",",".");

            partyObject = new Party(
                    0,
                    ETname.getText().toString(),
                    ETdescription.getText().toString(),
                    startTime,
                    endTime,
                    ETtheme.getText().toString(),
                    DB.getUserId(),
                    ETaddress.getText().toString(),
                    longitude,
                    latitude
            );

            partyObject.printParty();
            postParty(partyObject);
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
                    Snackbar.make(view, "The party is created!", Snackbar.LENGTH_LONG).show();
                    openPartyViewActivity(response.body().getId());
                }
                Log.d("my tag", "Posting party resulted empty: " + response.code());
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "Posting party to DB has failed ");
                internetConnectionCheck();
            }
        });
    }



    // A method to open a specified PartyView activity
    public void openPartyViewActivity(int n) {
        Intent i = new Intent("android.intent.action.PartyView");
        i.putExtra("ID", n);
        this.startActivity(i);
    }

    //method will check for internet connection and will not leave until it finds one
    public void internetConnectionCheck(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return;
        }

        Log.d("my tag", "connecting to the server failed");
        AlertDialog.Builder ADbuilderR = new AlertDialog.Builder(this);

        // Set up dialog
        ADbuilderR.setTitle("No connection");
        ADbuilderR.setMessage("Could not connect to server." +
                " Check your internet connection and press 'Try again'.");
        ADbuilderR.setCancelable(false);
        ADbuilderR.setPositiveButton("Try again",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                internetConnectionCheck();
            }
        });

        // Create alert dialog
        AlertDialog alertDialogRemove = ADbuilderR.create();
        alertDialogRemove.show();
    }
}
