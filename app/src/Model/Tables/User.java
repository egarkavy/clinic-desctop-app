package Model.Tables;

public class User {
    private int Id;
    private String UserName;
    private String Password;
    private int RoleId;

    public User(int id, String userName, String password, int roleId) {
        Id = id;
        UserName = userName;
        Password = password;
        RoleId = roleId;
    }

    public User() {}

    public int getId() {
        return Id;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }
}
