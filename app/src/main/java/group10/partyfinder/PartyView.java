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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_view);

        // Create toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create back button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get party details
        String pName = getIntent().getStringExtra("partyName");
        String pDate = getIntent().getStringExtra("partyDate");
        String pAddress = getIntent().getStringExtra("partyAddress");
        String pTheme = getIntent().getStringExtra("partyTheme");
        String pInfo = getIntent().getStringExtra("partyInfo");
        String pOrganizer = getIntent().getStringExtra("partyOrganizer");

        // Set title with the name of the party
        setTitle(pName);

        // Make references to text views
        TextView textViewName = (TextView) findViewById(R.id.PartyName);
        TextView textViewDate = (TextView) findViewById(R.id.PartyDate);
        TextView textViewAddress = (TextView) findViewById(R.id.PartyAddress);
        TextView textViewTheme = (TextView) findViewById(R.id.PartyTheme);
        TextView textViewInfo = (TextView) findViewById(R.id.PartyDescription);

        // Set text in text views
        textViewName.setText(pName);
        textViewDate.setText(pDate);
        textViewAddress.setText(pAddress);
        textViewTheme.setText(pTheme);
        textViewInfo.setText(pInfo);
    }

    // Method is called when on the go back arrow (in the left top) is pressed.
    // Close activity and go back to the previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Go back to previous activity
    public void goBack(View v) {
        finish();
    }
}
