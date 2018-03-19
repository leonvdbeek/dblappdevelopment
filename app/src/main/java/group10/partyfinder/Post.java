package group10.partyfinder;

/**
 * Created by Roy on 16/03/2018.
 */

public class Post {
    private int id;
    private String title;
    private String tite1;
    private String body;
    private int userId;
    private int userId2;

    public Post(String title, String body, int userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }
}
