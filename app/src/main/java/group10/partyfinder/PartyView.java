package group10.partyfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mark.
 *
 */

public class PartyView extends AppCompatActivity {

    // Get the database
    private DBSnapshot DB = DBSnapshot.getInstance();
    private int partyID;
    private Party partyObject;

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
        TextView TVdate = (TextView) findViewById(R.id.TVdate);
        TextView TVaddress = (TextView) findViewById(R.id.TVaddress);
        TextView TVtheme = (TextView) findViewById(R.id.TVtheme);
        TextView TVinfo = (TextView) findViewById(R.id.TVdescription);

        // Get party object
        partyID = getIntent().getIntExtra("ID", 0);
        partyObject = DB.getParty(partyID);

        // Set title with the name of the party
        setTitle(partyObject.getName());

        // Set text in text views
        TVdate.setText(partyObject.getDate());
        TVaddress.setText(partyObject.getAddress());
        TVtheme.setText(partyObject.getTheme());
        TVinfo.setText(partyObject.getInfo());
    }

    // Called when go back arrow (in the left top) is pressed.
    // Go back to previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
