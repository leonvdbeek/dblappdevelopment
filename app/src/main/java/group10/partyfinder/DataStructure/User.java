package group10.partyfinder.DataStructure;

/*
 * a class used by the retrofit to communicate with the api
 */

public class User {
    private String id;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
