package group10.partyfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

    //get the instance of our database
    private DBSnapshot DB = DBSnapshot.getInstance();

    boolean todaySet = false;
    boolean futureSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //todo login logic starts her
//this initializes the preference object
        //SharedPreferences prefs = this.getSharedPreferences(
        //        "com.example.app", Context.MODE_PRIVATE);


        //if (prefs.getInt("userId", 0) == 0){
            //call to login
            //after login call this in the login activity
            //and replace {@code id} with the userId retrieved from google
            //SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
            //prefs.edit().putInt("userId", {@code id});
            //prefs.edit().apply();
        //} else {
        //    Integer userId = prefs.getInt("userId", 0);
        //    DB.setUserId(Integer.toString(userId));
        //}

//this is the logic for logging out
        //logout(){
        //    SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        //    prefs.edit().putInt("userId", 0);
        //    prefs.edit().apply();
        //}
        //todo login logic ends here


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

                        // CreateParty menu button
                        if(menuItem.getItemId() == R.id.createParty) {
                            Intent i = new Intent("android.intent.action.CreateParty");
                            startActivity(i);
                        }

                        //Login activity menu item
                        if(menuItem.getItemId() == R.id.Login) {
                            Intent i = new Intent("android.intent.action.Login");
                            startActivity(i);
                        }

                        //Saved Parties
                        if(menuItem.getItemId() == R.id.saved_parties) {
                            Intent i = new Intent("android.intent.action.SavedPartiesActivity");
                            startActivity(i);
                        }

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

                Log.d("my tag", " amount of today parties in DB: " + DB.getTodayParties().size());
                Log.d("my tag", "amount of future parties in DB: " + DB.getFutureParties().size());
                Log.d("my tag", "    amount of my parties in DB: " + DB.getMyParties().size());
                Log.d("my tag", " amount of saved parties in DB: " + DB.getSavedParties().size());


                 //this is printing from the local database, it is purely for testing
                //Party party = new Party();
                //party.setId(9);
                //editParty(party);
                //postParty(party);

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

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //call the updateSnapshot method to "sync" the local snapshot with the server
        updateSnapshot();

    }


    //update the database everytime the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        updateSnapshot();
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
                Log.d("my tag", "my parties responce body " + response.body() + " and code: " + response.code());
                DB.setMyParties(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });

        //call for users saved parties
        Call<ArrayList<Saved>> call2;
        Log.d("my tag", "DB userId: " + DB.getUserId());
        call2 = client.usersSavedParties(DB.getUserId());
        call2.enqueue(new Callback<ArrayList<Saved>>() {
            @Override
            public void onResponse(Call<ArrayList<Saved>> call, Response<ArrayList<Saved>> response) {

                new Thread(new Runnable() {
                    public void run() {
                        ArrayList<Party> parties = new ArrayList<Party>();
                        Log.d("my tag", "saved responce body " + response.body() + " and code: " + response.code());

                        while (!DB.isDBReady()){
                            try {
                                Log.d("my tag", "DB.allParties() is not available yet");
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                Log.d("my tag", "waiting failed apearantly ? :c");
                            }
                        }

                        for (Saved save : response.body()){
                            parties.add(DB.getParty(save.getId_party()));
                        }
                        DB.setSavedParties(parties);
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ArrayList<Saved>> call, Throwable t) {
                showDbLoadError();
            }
        });

        //call for todays parties
        call = client.todayParties();
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response) {
                DB.setTodayParties(response.body());
                todaySet = true;

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
                futureSet = true;

            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                showDbLoadError();
            }
        });
        for (int i = 0; i < 4; i ++){
            try {
               // Log.d("my tag", "DB.allParties() is not available yet");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //Log.d("my tag", "waiting failed apearantly ? :c");
            }
        }
        setupViewPager(mViewPager);


    }

    //Todo add comment
    @Override
    public void onListFragmentInteraction(Party item) {

    }

    // A method to open the hostParty activity
    public void openCreatePartyActivity(View v) {
        Intent i = new Intent("android.intent.action.CreateParty");
        //i.putExtra("ID", 2);
        this.startActivity(i);
    }

    //method will open a error dialog to show
    public void showDbLoadError(){
        AlertDialog.Builder ADbuilderR = new AlertDialog.Builder(this);

        // Set up dialog
        ADbuilderR.setTitle("Update failed");
        ADbuilderR.setMessage("Could not connect to server. Check your internet connection and then retry.");
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
        alertDialogRemove.show();
    }


    //add the user to the DB if it doesn't exsist yet
    public void postUser(User def) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient client2 = retrofit.create(ApiClient.class);

        //Log.d("my tag", "delete Post userid: " + DB.getUserId()
        //        + " and partyid: " + Integer.toString(id_party));
        //call
        Call<User> call = client2.postUser(def);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response){
                Log.d("my tag", "posted user Id to DB: with responce code " + response.code());
                DB.setUserId(def.getId());
            }

            @Override
            public void onFailure (Call<User> call, Throwable t){

                Log.d("my tag", "delete Post has failed: ");
            }
        });
    }
}

