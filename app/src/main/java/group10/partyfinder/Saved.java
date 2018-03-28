package group10.partyfinder;

public class Saved {
    private String id_user;
    private Integer id_party;


    public Saved(String id_user, Integer id_party) {
        this.id_user = id_user;
        this.id_party = id_party;
    }

    public String getId_user() {
        return id_user;
    }

    public Integer getId_party() {
        return id_party;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public void setId_party(Integer id_party) {
        this.id_party = id_party;
    }
}
