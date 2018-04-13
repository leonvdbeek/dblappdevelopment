package group10.partyfinder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private String[] ETSD;
    private String[] startDate;
    private String[] endDate;

    String longitude;
    String latitude;

    private Context context = this;

    Calendar myCalendar = Calendar.getInstance();
    Calendar mcurrentTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_party);

        //first check connectivity
        internetConnectionCheck();

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

        // Split start date for date picker
        ETSD = startTime[0].split("-");

        myCalendar.set(Calendar.YEAR, Integer.parseInt(ETSD[2]));
        // -1 to get the correct month
        myCalendar.set(Calendar.MONTH, Integer.parseInt(ETSD[1])-1);
        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ETSD[0]));

        // Set start time for time picker
        mcurrentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime[1].split(":")[0]));
        mcurrentTime.set(Calendar.MINUTE, Integer.parseInt(startTime[1].split(":")[1]));

        ETstartDate.setText(startTime[0]);
        ETstartTime.setText(startTime[1]);

        endTime= partyObject.getPartyViewEndDate().split(" ");

        ETendDate.setText(endTime[0]);
        ETendTime.setText(endTime[1]);

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
                new DatePickerDialog(EditParty.this,
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
                new DatePickerDialog(EditParty.this,
                        eDate,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ETstartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditParty.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(EditParty.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void startEditParty(View v) {

        //first check connectivity
        internetConnectionCheck();

        startDate = ETstartDate.getText().toString().split("-");
        endDate = ETendDate.getText().toString().split("-");

        saveStartTime = startDate[2] + "-" + startDate[1] + "-" + startDate[0]
                + "T" + ETstartTime.getText().toString() + ":00+02:00";
        saveEndTime = endDate[2] + "-" + endDate[1] + "-" + endDate[0]
                + "T" + ETendTime.getText().toString() + ":00+02:00";
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
            // Edit party
            Address address = addresses.get(0);

            DecimalFormat df = new DecimalFormat("###.#######");

            longitude = df.format(address.getLongitude()).replaceAll(",",".");
            latitude = df.format(address.getLatitude()).replaceAll(",",".");


            savePartyObject = new Party(
                    partyObject.getId(),
                    ETname.getText().toString(),
                    ETdescription.getText().toString(),
                    saveStartTime,
                    saveEndTime,
                    ETtheme.getText().toString(),
                    DB.getUserId(),
                    ETaddress.getText().toString(),
                    longitude,
                    latitude
            );

            savePartyObject.printParty();
            editParty(savePartyObject);
        }
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
            public void onResponse(Call<Party> call, Response<Party> response) {
                if (response.body() != null) {
                    Log.d("my tag", "Put response code: " + response.code());
                    Party partyNew = response.body();
                    Log.d("my tag", "Put party id: "
                            + party.getId()+" => " + partyNew.getId());
                    party.printParty();
                    DB.editHostedParty(partyNew);
                    if (response.code() == 200 || response.code() == 204) {
                        Snackbar.make(view, "The party has been edited!",
                                Snackbar.LENGTH_LONG).show();
                        finish();

                    } else {
                        Log.d("my tag editParty()", "Put response code: "
                                + response.code());
                        Snackbar.make(view, "Editing failed, please retry later",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("my tag editParty()", "Put returned empty. response code: "
                            + response.code());
                    Snackbar.make(view, "Editing failed, please retry later",
                            Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "Put party request has failed");
                Snackbar.make(view, "Editing failed, please retry later",
                        Snackbar.LENGTH_LONG).show();
                internetConnectionCheck();
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
