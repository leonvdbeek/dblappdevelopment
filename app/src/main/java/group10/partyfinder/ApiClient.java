package group10.partyfinder;

import java.util.ArrayList;

import group10.partyfinder.DataStructure.Party;
import group10.partyfinder.DataStructure.Saved;
import group10.partyfinder.DataStructure.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Roy on 12/03/2018.
 */

public interface ApiClient {
    //get a list of all parties in the DB
    @GET("/parties/")
    Call<ArrayList<Party>> getParties();

    //get a list of parties organized by the user
    @GET("/organized/{user}")
    Call<ArrayList<Party>> usersMyParties(@Path("user") String user);


    //get a list of parties today
    @GET("/todayParty/")
    Call<ArrayList<Party>> todayParties();

    //get a list of parties in the future
    @GET("/futureParty/")
    Call<ArrayList<Party>> futureParties();


    //call to post a party to the server
    @POST("/parties/")
    Call<Party> hostParty(@Body Party party);

    //call to delete a party from the server
    @DELETE("/parties/{id}")
    Call<Party> deleteParty(@Path("id") String id);

    //call to edit a party on the server
    @PUT("/parties/{id}")
    Call<Party> editParty(@Path("id") String id, @Body Party party);


    //get all parties saved by the user
    @GET ("/saved/{user}")
    Call<ArrayList<Saved>> usersSavedParties(@Path("user") String user);

    //call to add a party to a users saved list
    @POST("/save/")
    Call<Saved> saveParty(@Body Saved save);

    //call to remove a party from a users saved list
    @DELETE("/unsave/{userId}/{partyId}")
    Call<Saved> deleteSavedParty(@Path("userId") String userId, @Path("partyId") String partyId);


    //call to add a user to the user DB
    @POST("/userid/")
    Call<User> postUser(@Body User user);
}
