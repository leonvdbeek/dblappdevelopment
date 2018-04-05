package group10.partyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MyPartiesActivity extends AppCompatActivity implements PartyListFragment.OnListFragmentInteractionListener {

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
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        Bundle data = new Bundle();//create bundle instance
        data.putInt("list", 4);
        PartyListFragment fragment = new PartyListFragment();
        fragment.setArguments(data);
        fragmentTransaction.add(R.id.list_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Party item) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //ToDo make the list refresh it's contents
    }
}
