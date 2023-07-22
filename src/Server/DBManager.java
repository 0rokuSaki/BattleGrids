package Server;

public interface DBManager {


    public void setUserName();

    public void setPassword(String password);

    public boolean isUserNameExist(String username);

    public boolean validLogIn(String password);






}
