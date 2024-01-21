/**
 * The UserInformation class represents a singleton object that holds information about the current user.
 * It stores user-specific data such as username, email, password, user ID, and the number of movies rated.
 * This class follows the Singleton design pattern to ensure only one instance exists during the application lifecycle.
 */
public class UserInformation {

    // Use a static method to get the instance
    private static UserInformation instance;
    public static UserInformation getInstance() {
        if (instance == null) {
            instance = new UserInformation();
        }
        return instance;
    }
    private String username,email,password,lastLogin,registerDate = "";
    private int moviesRated,id,ratedSum = 0;
    private double meanScore = 0;
    public void setRatedSum(int ratedSum) {

        this.ratedSum = ratedSum;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public double getMeanScore() {
        if(moviesRated == 0){
            meanScore = 0;
            return meanScore;
        }
        meanScore = (double) ratedSum / moviesRated;
        return meanScore;
    }

    public void setMoviesRated(int moviesRated) {
        this.moviesRated = moviesRated;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setID(int id){
        this.id = id;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getUsername(){
        return username != null ? username : "";
    }
    public String getEmail(){
        return email != null ? email : "";
    }
    public int getID(){
        return id;
    }
    public int getMoviesRated(){
        return moviesRated;
    }
    public String getPassword() {
        return this.password;
    }
    public int getRatedSum() {
        return this.ratedSum;
    }
}
