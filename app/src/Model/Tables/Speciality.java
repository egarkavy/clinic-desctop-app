package Model.Tables;

/**
 * Created by super on 5/26/2019.
 */
public class Speciality {
    private int Id;
    private String Name;

    public Speciality() {

    }

    public Speciality(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
