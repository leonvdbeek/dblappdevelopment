package group10.partyfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements PartyListFragment.OnListFragmentInteractionListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private View view;

    //testing counter
    int counter = 2;

    //get the instance of our database
    private DBSnapshot DB = DBSnapshot.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // add menu button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // add drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // drawer items on click listeners
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        //Todo remove the testing fab after server communication testing is complete
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //this is printing from the local database, it is purely for testing
                Party party = new Party();
                party.setId(99);
                postParty(party);
                //openPartyViewActivity(counter);
                //counter++;

                //ArrayList<Party> myParties = DB.getMyParties();
                //if (myParties == null) {
                //    Snackbar.make(view, "no parties available yet, wait for update", Snackbar.LENGTH_LONG)
                //            .setAction("Action", null).show();
                //} else {
                //    for (Party party : myParties) {
                //        party.printParty();
                //        counter++;
                //    }
                //    Snackbar.make(view, "You have created " + counter + " party.", Snackbar.LENGTH_LONG)
                //            .setAction("Action", null).show();
                //    counter = 0;
                //}
            }
        });

        //Todo uncomment updateSnapshot once the server supports all calls
        //call the updateSnapshot method to "sync" the local snapshot with the server
        updateSnapshot();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    //update the database everytime the
    @Override
    protected void onResume() {
        super.onResume();
        updateSnapshot();
    }



    // A method to open a specified PartyView activity
    public void openPartyViewActivity(int n) {
        Intent i = new Intent("android.intent.action.PartyView");
        i.putExtra("ID", n);
        this.startActivity(i);
    }



    //code that will run when and option from the drop down menu is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //moving to mapview no longer works as it has been rebuild into an raction instead of an activity
        if (id == R.id.action_map) {
            Intent intent = new Intent(MainActivity.this,
                    MapsActivity.class);
            startActivity(intent); // startActivity allow you to move
        }
        // menu 
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //for the tabs
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CrashNowPage(), "Crash Now");
        adapter.addFragment(new DiscoverPage(), "Discover");
        viewPager.setAdapter(adapter);
    }

    //method to post a party to the server
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
        Log.d("my tag", "Post request body: " + party.getDate());
        call.enqueue(new Callback<Party>() {
            @Override
            public void onResponse(Call<Party> call, Response<Party> response){
                Log.d("my tag", "Post response code: " + response.code());
                Party party = response.body();
                Log.d("my tag", "Posted party id: " + party.getId());
                Log.d("my tag", "Contents" + DB.getAllParties().size()
                        + DB.getMyParties().size()
                        + DB.getSavedParties().size());

                DB.addHostedParty(party);
                openPartyViewActivity(party.getId());
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){

            }
        });
    }

    //updates the DBSnapshot overwriting all current data with fresh data from the server
    private void updateSnapshot(){
        Log.d("my tag", "Updating local DB");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        ApiClient client = retrofit.create(ApiClient.class);
        Call<ArrayList<Party>> call;

        //call for all parties
        call = client.getParties();
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response) {
                DB.setAllParties(response.body());
                setupViewPager(mViewPager);
            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });

        //call for users my parties
        call = client.usersMyParties(DB.getUserId());
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response) {
                DB.setMyParties(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });

        //call for users saved parties
        call = client.usersSavedParties(DB.getUserId());
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response) {
                DB.setSavedParties(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });

        //call for todays parties
        call = client.todayParties();
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response) {
                DB.setTodayParties(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });

        ////call for future parties
        call = client.futureParties();
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response) {
                DB.setFutureParties(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });
    }

    //method to remove a party
    private void deleteParty(Party party){
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
        Log.d("my tag", "Post request body: " + party.getDate());
        call.enqueue(new Callback<Party>() {
            @Override
            public void onResponse(Call<Party> call, Response<Party> response){
                Log.d("my tag", "Post response code: " + response.code());
                Party party = response.body();
                Log.d("my tag", "Posted party id: " + party.getId());
                Log.d("my tag", "Contents" + DB.getAllParties().size()
                        + DB.getMyParties().size()
                        + DB.getSavedParties().size());

                DB.addHostedParty(party);
                openPartyViewActivity(party.getId());
            }

            @Override
            public void onFailure (Call<Party> call, Throwable t){
                showDbLoadError();
            }
        });
    }

    //Todo add comments
    @Override
    public void onListFragmentInteraction(Party item) {

    }

    // A method to open the hostParty activity
    public void openHostPartyActivity(View v) {
        Intent i = new Intent("android.intent.action.HostParty");
        //i.putExtra("ID", 2);
        this.startActivity(i);
    }

    //method will open a error dialog to show
    public void showDbLoadError(){
        AlertDialog.Builder ADbuilderR = new AlertDialog.Builder(this);

        // Set up dialog
        ADbuilderR.setTitle("Update failed");
        ADbuilderR.setMessage("Could not connect to server. Would you like to retry?");
        ADbuilderR.setCancelable(true);
        ADbuilderR.setPositiveButton("Try again",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Accept button (right)
                // Retry updating the snapshot
                updateSnapshot();
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
        alertDialogRemove.show();}
}

