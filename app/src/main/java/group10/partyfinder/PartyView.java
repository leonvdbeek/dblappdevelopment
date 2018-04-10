package group10.partyfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mark.
 *
 */

public class PartyView extends AppCompatActivity {

    // Get the database
    private DBSnapshot DB = DBSnapshot.getInstance();

    private int partyID;
    private Party partyObject;
    private Button Bsave;
    private Button Bremove;
    private Button Bedit;
    private Button Bdelete;
    private View view;

    TextView TVstartTime;
    TextView TVendTime;
    TextView TVaddress;
    TextView TVtheme;
    TextView TVinfo;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        internetConnectionCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_view);

        // Create toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Make references to text views
        TVstartTime = findViewById(R.id.TVstartTime);
        TVendTime = findViewById(R.id.TVendTime);
        TVaddress = findViewById(R.id.TVaddress);
        TVtheme = findViewById(R.id.TVtheme);
        TVinfo = findViewById(R.id.TVdescription);

        // Get party object
        partyID = getIntent().getIntExtra("ID", 0);
        partyObject = DB.getParty(partyID);

        // Set title with the name of the party
        setTitle(partyObject.getName());

        // Set text in text views
        TVstartTime.setText(partyObject.getPartyViewDate());
        TVendTime.setText(partyObject.getPartyViewEndDate());
        TVaddress.setText(partyObject.getAddress());
        TVtheme.setText(partyObject.getTheme());
        TVinfo.setText(partyObject.getInfo());

        // Make variable for snackbar
        view = findViewById(R.id.mainLayout);

        // Show save or remove button
        Bsave = findViewById(R.id.Bsave);
        Bremove = findViewById(R.id.Bremove);



        // Show or hide edit and delete button
        Bedit = findViewById(R.id.Bedit);
        Bdelete = findViewById(R.id.Bdelete);

        if(isOwner()) {
            // Show edit and delete button
            Bedit.setVisibility(View.VISIBLE);
            Bdelete.setVisibility(View.VISIBLE);

            Bsave.setVisibility(View.GONE);
            Bremove.setVisibility(View.GONE);
        } else {
            // Hide edit and delete button
            Bedit.setVisibility(View.GONE);
            Bdelete.setVisibility(View.GONE);

            if(isSaved()) {
                // Show remove button
                Bsave.setVisibility(View.GONE);
                Bremove.setVisibility(View.VISIBLE);
            } else {
                // Show save button
                Bsave.setVisibility(View.VISIBLE);
                Bremove.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        DB = DBSnapshot.getInstance();

        // Get party object
        partyObject = DB.getParty(partyID);

        // Set title with the name of the party
        setTitle(partyObject.getName());

        // Set text in text views
        TVstartTime.setText(partyObject.getPartyViewDate());
        TVendTime.setText(partyObject.getPartyViewEndDate());
        TVaddress.setText(partyObject.getAddress());
        TVtheme.setText(partyObject.getTheme());
        TVinfo.setText(partyObject.getInfo());

    }


    // On click method for save button
    public void clickOnSave(View v) {
        internetConnectionCheck();

        saveParty(partyID);

        // Show remove button
        Bsave.setVisibility(View.GONE);
        Bremove.setVisibility(View.VISIBLE);
    }

    // On click method for remove button
    public void clickOnRemove(View v) {

        AlertDialog.Builder ADbuilderR = new AlertDialog.Builder(context);

        // Set up dialog
        ADbuilderR.setTitle("Remove party");
        ADbuilderR.setMessage("Do you really want to" +
                " remove the party from your saved parties list?");
        ADbuilderR.setCancelable(true);
        ADbuilderR.setPositiveButton("Remove",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Accept button (right)
                // Remove the party from the saved list
                startRemoveParty();
            }
        });
        ADbuilderR.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Decline button (left)
                // Cancel action
                dialog.cancel();
            }
        });

        // Create alert dialog
        AlertDialog alertDialogRemove = ADbuilderR.create();
        alertDialogRemove.show();
    }

    // Start the actions to remove a party from the saved list
    public void startRemoveParty() {
        internetConnectionCheck();
        removeParty(partyID);

        // Show remove button
        Bremove.setVisibility(View.GONE);
        Bsave.setVisibility(View.VISIBLE);
    }

    // On click method for edit button
    public void clickOnEdit(View v) {
        Intent i = new Intent("android.intent.action.EditParty");
        i.putExtra("ID", partyID);
        this.startActivity(i);

    }

    // On click method for delete button
    public void clickOnDelete(View v) {
        internetConnectionCheck();
        AlertDialog.Builder ADbuilderD = new AlertDialog.Builder(context);

        // Set up dialog
        ADbuilderD.setTitle("Delete party");
        ADbuilderD.setMessage("Do you really want to delete the party?");
        ADbuilderD.setCancelable(true);
        ADbuilderD.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Accept button (right)
                // Remove the party from the saved list
                startDeleteParty();
            }
        });
        ADbuilderD.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Decline button (left)
                // Cancel action
                dialog.cancel();
            }
        });

        // Create alert dialog
        AlertDialog alertDialogDelete = ADbuilderD.create();
        alertDialogDelete.show();
    }

    // Start the actions to delete a party
    public void startDeleteParty() {
        deleteParty();

/*        AlertDialog ADdelete = new AlertDialog.Builder(context).create();
        ADdelete.setTitle("Party deleted");
        ADdelete.setMessage("The party is deleted.");
        ADdelete.setCancelable(false);
        ADdelete.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        ADdelete.show();*/
    }

    // Save the party to the saved list
    public void saveParty(int id_party) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        Saved save = new Saved(DB.getUserId(), id_party);

        //call
        Call<Saved> call = client2.saveParty(save);
        call.enqueue(new Callback<Saved>() {
            @Override
            public void onResponse(Call<Saved> call, Response<Saved> response){
                Log.d("my tag", "save Post response code: " + response.code());
                if(response.code() == 201) {
                    Snackbar.make(view, "Party is added to your saved parties list!",
                            Snackbar.LENGTH_LONG).show();
                    DB.addToSaveList(DB.getParty(response.body().getId_party()));
                } else {
                    Log.d("my tag",
                            "save returned empty response code: " + response.code());
                    Snackbar.make(view, "Saving the party has failed, please restart the app.",
                            Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure (Call<Saved> call, Throwable t){
                Log.d("my tag", "Post save has failed");
                internetConnectionCheck();
            }
        });
    }


    // Remove the party from the saved list
    public void removeParty(int id_party) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        //call
        Call<Saved> call = client2.deleteSavedParty(DB.getUserId(), Integer.toString(id_party));
        call.enqueue(new Callback<Saved>() {
            @Override
            public void onResponse(Call<Saved> call, Response<Saved> response){
                if(response.code() == 204) {
                    Log.d("my tag", "remove Post response code: " + response.code());
                    DB.removeFromSaveList(DB.getParty(id_party));
                    Snackbar.make(view, "Party is removed from your saved parties list.",
                            Snackbar.LENGTH_LONG).show();
                } else {
                    Log.d("my tag",
                            "remove Post resulted empty. respCode: " + response.code());
                    Snackbar.make(view, "remove failed, please restart the app",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure (Call<Saved> call, Throwable t){
                Log.d("my tag", "delete Post has failed");
                internetConnectionCheck();
            }
        });
    }

    // Delete the party entirely from the DB
    public void deleteParty() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        //call for all parties
        Call<Party> call = client2.deleteParty(Integer.toString(partyID));
        Log.d("my tag", "Delete request body: " + partyID);
        call.enqueue(new Callback<Party>() {
            @Override
            public void onResponse(Call<Party> call, Response<Party> response){
                Log.d("my tag", "Post response code: " + response.code());
                if(response.code() == 204){
                    DB.deleteHostedParty(DB.getParty(partyID));
                    Snackbar.make(view, "The party is deleted.", Snackbar.LENGTH_LONG).show();
                } else if(response.code() == 404){
                    Log.d("my tag", "response was 404, party could not be found");
                    Snackbar.make(view,
                            "The party does not exist, please restart the app to refresh.",
                            Snackbar.LENGTH_LONG).show();
                }

                finish();
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "party delete request failed");
                internetConnectionCheck();
            }
        });
    }

    // Check if the user has saved the party
    public boolean isSaved() {
        return (DB.getSavedParties().contains(partyObject));
    }

    // Check if the user is the owner of the party
    public boolean isOwner() {
        return (DB.getUserId().contentEquals(partyObject.getCreator()));
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
        ADbuilderR.setTitle("Update failed");
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
