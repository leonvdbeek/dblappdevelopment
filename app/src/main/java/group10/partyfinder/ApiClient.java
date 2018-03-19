package group10.partyfinder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Roy on 12/03/2018.
 */

public interface ApiClient {

    //url is lenin.pythonanywhere.com/parties

    //ToDo remove the test endpoint
    //test endpoint, not to be used in final product
    @GET("/parties")
    Call<ArrayList<Party>> getParties();

    @GET("/organized/{user}")
    Call<ArrayList<Party>> usersMyParties(@Path("user") String user);

    @GET("/saved/{user}")
    Call<ArrayList<Party>> usersSavedParties(@Path("user") String user);

    @GET("/todayParty")
    Call<ArrayList<Party>> todayParties();

    @GET("/futureParty")
    Call<ArrayList<Party>> futureParties();

    @POST("/parties")
    Call<Party> hostParty(@Body Party party);

    //@POST("https://jsonplaceholder.typicode.com/posts")
    //Call<Post> testPost(@Body Post post);
}
