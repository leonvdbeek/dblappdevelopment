package group10.partyfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(MainActivity.this, "getting parties"
                        , Toast.LENGTH_LONG).show();
            }
        });

        getParties();

        Party party = new Party("appPosted", "this has been posted by the app", "today"
            , "tested", "114987278191137298218", "theCloud"
            , "123", "321");

        //hostParty(party);

        Post post = new Post("foo", "bar", 1);
        testPost(post);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getParties(){
        //retrofit code for in the api method
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RestApiHandler client = retrofit.create(RestApiHandler.class);
        Call<ArrayList<Party>> call = client.getParties();

        //running call.enqueue as this will cause the request to be made asynchronously
        call.enqueue(new Callback<ArrayList<Party>>() {

            @Override
            public void onResponse(Call<ArrayList<Party>> call, Response<ArrayList<Party>> response){
                ArrayList<Party> parties = response.body();
                //ToDo implement your desired action with the response here


                //e.g. print all parties to the debugger
                for (Party party : parties){
                    party.printParty();
                }
                //ToDo end
            }

            @Override
            public void onFailure (Call < ArrayList < Party >> call, Throwable t){
                Toast.makeText(MainActivity.this, "getting failed"
                        , Toast.LENGTH_LONG).show();
            }
        });
    }


    public void hostParty(Party party) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RestApiHandler client = retrofit.create(RestApiHandler.class);
        Call call = client.hostParty(party);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(MainActivity.this, "It should have worked" + response.code()
                        , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(MainActivity.this, "posting failed"
                        , Toast.LENGTH_LONG).show();
            }
        });
    }


    public void testPost(Post post) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://lenin.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RestApiHandler client = retrofit.create(RestApiHandler.class);
        Call<Post> call = client.testPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Toast.makeText(MainActivity.this, "It should have worked"+response.code()+response.body().getId()
                        , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(MainActivity.this, "posting failed"
                        , Toast.LENGTH_LONG).show();
            }
        });
    }
}
