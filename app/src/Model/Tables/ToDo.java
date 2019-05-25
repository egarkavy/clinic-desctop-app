package Model.Tables;

import com.sun.xml.bind.v2.TODO;

/**
 * Created by super on 5/20/2019.
 */
public class ToDo {
    private int Id;
    private String What;
    private boolean IsDone;

    public ToDo() {

    }

    public ToDo(String what, boolean isDone) {
        What = what;
        IsDone = isDone;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getWhat() {
        return What;
    }

    public void setWhat(String what) {
        What = what;
    }

    public boolean isDone() {
        return IsDone;
    }

    public void setDone(boolean done) {
        IsDone = done;
    }
}
