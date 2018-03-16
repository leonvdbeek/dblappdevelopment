package group10.partyfinder;

/**
 * Created by Roy on 16/03/2018.
 */

public class Post {
    private int id;
    private String title;
    private String body;
    private int userId;

    public Post(String title, String body, int userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }
}
