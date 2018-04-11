package group10.partyfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainScreen extends AppCompatActivity
        implements PartyListFragment.OnListFragmentInteractionListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;

    //get the instance of our database
    private DBSnapshot DB = DBSnapshot.getInstance();

    boolean todaySet = false;
    boolean futureSet = false;

    //Google User ID which will be filled in after logging in
    public String GUSERID;

    //Header TextView
    private TextView header;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //check for an available internet connection
        internetConnectionCheck();

        //creating google sigin, so we can logout from the mainactivity
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // add menu button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // add drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // drawer items on click listeners
        //Creating NavView here already so i can load the login
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        header = (TextView)hView.findViewById(R.id.header);
        header.setText("User not logged in");

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        //MainActivity activity menu item
                        if(menuItem.getItemId() == R.id.Login) {
                            signOut();
                        }

                        //Saved Parties
                        if(menuItem.getItemId() == R.id.saved_parties) {
                            Intent i = new Intent(
                                    "android.intent.action.SavedPartiesActivity");
                            startActivity(i);
                        }

                        //My Parties
                        if(menuItem.getItemId() == R.id.my_parties) {
                            Intent i = new Intent(
                                    "android.intent.action.MyPartiesActivity");
                            startActivity(i);
                        }

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        //MainActivity functionality
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            finish();
        } else {
            //Update header to show GUSERID, for the case the user was already logged in
            String GID = account.getDisplayName();
            DB.setUserId(account.getId());
            header.setText("Signed in as: "+GID);
        }


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //call the updateSnapshot method to "sync" the local snapshot with the server
        //updateSnapshot();

    }

    //Signout function to sign out from the mainactivity
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(
                this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Intent i = new Intent("android.intent.action.MAIN");
                //startActivityForResult(i, 1);
                finish();
            }
        });
    }

    //update the database everytime the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        internetConnectionCheck();
        updateSnapshot();

        //Check if the user is still logged in, if not redirect to login page
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            //Intent i = new Intent("android.intent.action.MAIN");
            //startActivityForResult(i, 1);
            finish();
        } else {
        //Update header to show GUSERID, for the case the user was already logged in
        String GID = account.getDisplayName();
        header.setText("Signed in as: "+GID);
    }
    }


    //todo delete this if safe
    //code that will run when and option from the drop down menu is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //moving to mapview no longer works as it has been rebuild into
        // a fraction instead of an activity
        if (id == R.id.action_map) {
            Intent intent = new Intent(MainScreen.this,
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
            public void onResponse(Call<ArrayList<Party>> call,
                                   Response<ArrayList<Party>> response) {
                Log.d("my tag", "all parties responce body "
                        + response.body() + " and code: " + response.code());
                DB.setAllParties(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {
                internetConnectionCheck();
            }
        });

        //call for users my parties
        call = client.usersMyParties(DB.getUserId());
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call,
                                   Response<ArrayList<Party>> response) {
                Log.d("my tag", "my parties responce body "
                        + response.body() + " and code: " + response.code());
                DB.setMyParties(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {

            }
        });

        //call for users saved parties
        Call<ArrayList<Saved>> call2;
        Log.d("my tag", "DB userId: " + DB.getUserId());
        call2 = client.usersSavedParties(DB.getUserId());
        call2.enqueue(new Callback<ArrayList<Saved>>() {
            @Override
            public void onResponse(Call<ArrayList<Saved>> call,
                                   Response<ArrayList<Saved>> response) {

                new Thread(new Runnable() {
                    public void run() {
                        ArrayList<Party> parties = new ArrayList<Party>();
                        Log.d("my tag", "saved responce body "
                                + response.body() + " and code: " + response.code());
                        DB.setReady(true);
                        while (!DB.isallReady()){
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
                        DB.setReady(true);
                    }
                }).start();

                DB.setReady(true);
            }

            @Override
            public void onFailure(Call<ArrayList<Saved>> call, Throwable t) {

            }
        });

        //call for todays parties
        call = client.todayParties();
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call,
                                   Response<ArrayList<Party>> response) {
                Log.d("my tag", "today parties responce body "
                        + response.body() + " and code: " + response.code());
                DB.setTodayParties(response.body());
                todaySet = true;

            }

            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {

            }
        });

        ////call for future parties
        call = client.futureParties();
        call.enqueue(new Callback<ArrayList<Party>>() {
            @Override
            public void onResponse(Call<ArrayList<Party>> call,
                                   Response<ArrayList<Party>> response) {
                DB.setFutureParties(response.body());
                futureSet = true;
            }
            @Override
            public void onFailure(Call<ArrayList<Party>> call, Throwable t) {

            }
        });

        //waits until the local DB is loaded before oading the map and lists
        //while(!DB.isDBReady()){
        for(int i=0; i <= 4; i++){
            try {
                Log.d("my tag", "DB is not available yet");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.d("my tag", "waiting failed apearantly ? :c");
            }
        }
        setupViewPager(mViewPager);
    }


    //Todo add comment
    @Override
    public void onListFragmentInteraction(Party item) {
    }

    //method will check for internet connection and will not leave until it finds one
    public void internetConnectionCheck(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return;
        }

        Log.d("my tag", "connecting to the server failed");
        AlertDialog.Builder ADbuilderR = new AlertDialog.Builder(this);

        // Set up dialog
        ADbuilderR.setTitle("No connection");
        ADbuilderR.setMessage("Could not connect to server." +
                " Check your internet connection and press 'Try again'.");
        ADbuilderR.setCancelable(false);
        ADbuilderR.setPositiveButton("Try again",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                internetConnectionCheck();
            }
        });

        // Create alert dialog
        AlertDialog alertDialogRemove = ADbuilderR.create();
        alertDialogRemove.show();
    }

}

