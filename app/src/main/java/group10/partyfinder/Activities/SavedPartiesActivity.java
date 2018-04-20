package group10.partyfinder.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import group10.partyfinder.DataStructure.Party;
import group10.partyfinder.Fragments.Components.PartyListFragment;
import group10.partyfinder.R;

public class SavedPartiesActivity extends AppCompatActivity
        implements PartyListFragment.OnListFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_parties);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 3);
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

        //ToDo make the list refresh it's contents
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 3);
        PartyListFragment fragment = new PartyListFragment();
        fragment.setArguments(data);
        fragmentTransaction.replace(R.id.list_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    // Called when go back arrow (in the left top) is pressed.
    // Go back to previous activity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
