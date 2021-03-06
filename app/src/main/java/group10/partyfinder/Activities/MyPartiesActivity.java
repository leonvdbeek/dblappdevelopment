package group10.partyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import group10.partyfinder.DataStructure.Party;
import group10.partyfinder.Fragments.Components.PartyListFragment;
import group10.partyfinder.R;

public class MyPartiesActivity extends AppCompatActivity
        implements PartyListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parties);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("android.intent.action.CreateParty");
                startActivityForResult(i, 123);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 4);
        PartyListFragment fragment = new PartyListFragment();
        fragment.setArguments(data);
        fragmentTransaction.replace(R.id.list_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Party item) {

    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 4);
        PartyListFragment fragment = new PartyListFragment();
        fragment.setArguments(data);
        fragmentTransaction.replace(R.id.list_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            try {
                int id = data.getIntExtra("ID", 0);
                Intent i = new Intent("android.intent.action.PartyView");
                i.putExtra("ID", id);
                this.startActivity(i);
            } catch (NullPointerException e) {
            }

        }
    }

    // Called when go back arrow (in the left top) is pressed.
    // Go back to previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
