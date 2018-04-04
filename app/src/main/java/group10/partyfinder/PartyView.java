package group10.partyfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        //TODO add: if(isOwner() == true) then don't show the save and remove button
        if(isSaved() == true) {
            // Show remove button
            Bsave.setVisibility(View.GONE);
            Bremove.setVisibility(View.VISIBLE);
        } else {
            // Show save button
            Bsave.setVisibility(View.VISIBLE);
            Bremove.setVisibility(View.GONE);
        }

        // Show or hide edit and delete button
        Bedit = findViewById(R.id.Bedit);
        Bdelete = findViewById(R.id.Bdelete);

        if(isOwner()) {
            // Show edit and delete button
            Bedit.setVisibility(View.VISIBLE);
            Bdelete.setVisibility(View.VISIBLE);
        } else {
            // Hide edit and delete button
            Bedit.setVisibility(View.GONE);
            Bdelete.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

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

    }


    // On click method for save button
    public void clickOnSave(View v) {
        Snackbar.make(view, "Party is saved!", Snackbar.LENGTH_LONG).show();

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
        ADbuilderR.setMessage("Do you really want to remove the party from your saved parties list?");
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
        Snackbar.make(view, "Party is removed!", Snackbar.LENGTH_LONG).show();

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
        //Snackbar.make(view, "This button should open the edit activity.", Snackbar.LENGTH_LONG).show();
    }

    // On click method for delete button
    public void clickOnDelete(View v) {
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
        //Snackbar.make(view, "Party is deleted!", Snackbar.LENGTH_LONG).show();
        deleteParty();

        AlertDialog ADdelete = new AlertDialog.Builder(context).create();
        ADdelete.setTitle("Party deleted");
        ADdelete.setMessage("The party is deleted.");
        ADdelete.setCancelable(false);
        ADdelete.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        ADdelete.show();
    }

    //TODO create saveParty function
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
                ArrayList<Party> saved = DB.getSavedParties();
                Party party = DB.getParty(id_party);
                if (!saved.contains(party)){
                    saved.add(party);
                    DB.setSavedParties(saved);
                }
            }

            @Override
            public void onFailure (Call<Saved> call, Throwable t){
                Log.d("my tag", "Post save has failed: ");
            }
        });
    }

    //TODO create removeParty function
    // Remove the party from the saved list
    public void removeParty(int id_party) {
// Method to post a party to the server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        Log.d("my tag", "delete Post userid: " + DB.getUserId()
                + " and partyid: " + Integer.toString(id_party));
        //call
        Call<Saved> call = client2.deleteSavedParty(DB.getUserId(), Integer.toString(id_party));
        call.enqueue(new Callback<Saved>() {
            @Override
            public void onResponse(Call<Saved> call, Response<Saved> response){
                Log.d("my tag", "delete Post response code: " + response.code());
                ArrayList<Party> saved = DB.getSavedParties();
                Party party = DB.getParty(id_party);
                if (saved.contains(party)){
                    saved.remove(party);
                    DB.setSavedParties(saved);
                }
            }

            @Override
            public void onFailure (Call<Saved> call, Throwable t){

                Log.d("my tag", "delete Post has failed: ");
            }
        });
    }

    // Delete the party
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

            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                Log.d("my tag", "party delete request failed");
            }
        });
    }

    // Check if the user has saved the party
    public boolean isSaved() {
        if (DB.getSavedParties().contains(partyObject)){
            return true;
        }
        return false;
    }

    // Check if the user is the owner of the party
    public boolean isOwner() {
        //if (DB.getUserId() == partyObject.getCreator()) {
            return true;
        //}
        //return false;
    }

    // Called when go back arrow (in the left top) is pressed.
    // Go back to previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
