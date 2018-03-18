package group10.partyfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PartyView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_view);

        // Get party details
        String pName = getIntent().getStringExtra("partyName");
        String pDate = getIntent().getStringExtra("partyDate");
        String pAddress = getIntent().getStringExtra("partyAddress");
        String pTheme = getIntent().getStringExtra("partyTheme");
        String pInfo = getIntent().getStringExtra("partyInfo");
        String pOrganizer = getIntent().getStringExtra("partyOrganizer");

        // Set title
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

    // Go back to previous activity
    public void goBack(View v) {
        finish();
    }
}
