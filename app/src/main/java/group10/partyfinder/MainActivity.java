package group10.partyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import group10.partyfinder.dummy.DummyContent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements PartyListFragment.OnListFragmentInteractionListener{

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

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

        //Todo remove the testing fab after server communication testing is complete
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //this is printing from the local database, it is purely for testing


                openPartyViewActivity(counter);
                counter++;

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
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSnapshot();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // A method to open the PartyView activity
    public void openPartyViewActivity(View v) {
        Intent i = new Intent("android.intent.action.PartyView");
        i.putExtra("ID", 2);
        this.startActivity(i);
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
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create());

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

            }
        });
    }

    //Todo add comments
    @Override
    public void onListFragmentInteraction(Party item) {

    }

}

