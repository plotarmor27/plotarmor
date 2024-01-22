import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The DatabaseQuery class provides methods for interacting with a database, particularly for user authentication
 * and manipulation of user-related and movie-related data in the "plotarmor" database.
 *
 * It includes methods for:
 * - Verifying user credentials (email and password) for login purposes.
 * - Creating a new user in the database with a unique email, username, and password.
 * - Updating user information such as email and username.
 * - Saving movie information, including title, overview, release date, duration, and genre, to the database.
 * - Loading a list of movie titles from the database into an ObservableList.
 * - Retrieving movie details such as description, duration, release date, and genre based on the movie title.
 *
 * Additionally, the class has a method to save user information retrieved from a ResultSet into a UserInformation singleton.
 * It uses JavaFX ObservableList to manage lists of movie titles, making it suitable for integration with JavaFX applications.
 *
 * Note: Proper exception handling is implemented to deal with potential SQLExceptions that may occur during database operations.
 */

public class DatabaseQuery {

    public boolean saveMoviesToDb(Connection connection, String title, String overview, Date release_date, int duration, String genre, double rating, String poster, String background) {
        String createMovieQuery = "INSERT INTO plotarmor.movies " +
                "(title, overview, release_date, duration_minutes, genres, rating, poster_path, background_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (connection) {
            // Check if the connection is null
            if (connection == null) {
                return false;
            }

            // Prepare a SQL query using a PreparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(createMovieQuery)) {
                // Set parameters in the prepared statement
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, overview);
                preparedStatement.setString(3, String.valueOf(release_date));
                preparedStatement.setInt(4,duration);
                preparedStatement.setString(5,genre);
                preparedStatement.setDouble(6,rating);
                preparedStatement.setString(7,poster);
                preparedStatement.setString(8,background);

                // Execute the query with executeUpdate() because of no return value and sql insert statement
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            // Handle SQLException
            ex.printStackTrace();
            System.err.println("Fehler beim Ausführen der Abfrage: " + ex.getMessage());
            return false; // Fehler beim Ausführen der Abfrage
        }

    }

    public String getMovieDescribtion(Connection connection, String movieName) throws SQLException {
            String query = "SELECT overview FROM plotarmor.movies WHERE title = ?";
            String movieDescription = null;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, movieName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    movieDescription = resultSet.getString("overview");
                    return movieDescription;
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception according to your needs
            }


            return movieDescription;
        }

    public String getMovieDuration(Connection connection, String movieName) throws SQLException {
        String query = "SELECT duration_minutes FROM plotarmor.movies WHERE title = ?";
        String movieDuration = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movieName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                movieDuration = resultSet.getString("duration_minutes");
                return movieDuration;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }


        return movieDuration;
    }

    public String getReleasedate(Connection connection, String movieName) throws SQLException {
        String query = "SELECT release_date FROM plotarmor.movies WHERE title = ?";
        String movieReleasedate = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movieName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                movieReleasedate = resultSet.getString("release_date");
                return movieReleasedate;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }


        return movieReleasedate;
    }

    public String getGenre(Connection connection, String movieName) throws SQLException {
        String query = "SELECT genres FROM plotarmor.movies WHERE title = ?";
        String movieGenre = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movieName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                movieGenre = resultSet.getString("genres");
                return movieGenre;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }


        return movieGenre;
    }

    public boolean getMovieName(Connection connection, ObservableList<String> movieList) throws SQLException {
        String query = "SELECT title FROM plotarmor.movies";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String movieName = resultSet.getString("title");
                movieList.add(movieName);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return false;
    }

    public boolean getRatings(Connection connection, ObservableList<String> movieRatingList) throws SQLException {
        String query = "SELECT rating FROM plotarmor.movies";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next() ) {

                String movieRating = resultSet.getString("rating");
                movieRatingList.add(movieRating);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return false;
    }

    public boolean getPosterPath(Connection connection, ObservableList<String> moviePosterPath) throws SQLException {
        String query = "SELECT poster_path FROM plotarmor.movies";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {

                String movieRating = resultSet.getString("poster_path");
                moviePosterPath.add(movieRating);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    return false;
    }

    public String getPoster(Connection connection, String movieName) throws SQLException {
        String query = "SELECT poster_path FROM plotarmor.movies WHERE title = ?";
        String moviePoster = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movieName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                moviePoster = resultSet.getString("poster_path");
                return moviePoster;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }


        return moviePoster;
    }

    public String getBackgroundPath(Connection connection, String movieName) throws SQLException {
        String query = "SELECT background_path FROM plotarmor.movies WHERE title = ?";
        String movieBackground = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movieName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                movieBackground = resultSet.getString("background_path");
                return movieBackground;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }


        return movieBackground;
    }

    public String getAverageVoting(Connection connection, String movieName) throws SQLException {
        String query = "SELECT rating FROM plotarmor.movies WHERE title = ?";
        String movieRating = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, movieName);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                movieRating = resultSet.getString("rating");
                return movieRating;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }


        return movieRating;
    }

    public boolean hasUserRatedMovie(Connection connection, int userid, String movieName) throws SQLException {

        String query = "SELECT COUNT(*) FROM ratedMovies WHERE userid = ? AND movieName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userid);
            preparedStatement.setString(2, movieName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    public boolean insertNewRatingToDb(Connection connection,int userid, String username, String movieName, int movieRated) {
        try {
            // Überprüfen, ob der Benutzer den Film bereits bewertet hat
            if (hasUserRatedMovie(connection, userid, movieName)) {
                return false; // Abbrechen, wenn der Benutzer den Film bereits bewertet hat
            }

            // Film bewerten und in die Datenbank einfügen
            String insertQuery = "INSERT INTO plotarmor.ratedMovies (userid, userName, movieName, movieRated) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, userid);
                insertStatement.setString(2, username);
                insertStatement.setString(3, movieName);
                insertStatement.setInt(4, movieRated);
                insertStatement.executeUpdate();
                System.out.println("Bewertung erfolgreich eingefügt.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in a real application
        }
        return false;
    }

    public HashMap<String, Integer> getMovieRatingsForUser(Connection connection, int userId) throws SQLException {
        String query = "SELECT movieName, movieRated FROM ratedMovies WHERE userid = ? ORDER BY movieRated DESC";
        HashMap<String, Integer> uniqueMovieRatings = new HashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, userId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String movieName = resultSet.getString("movieName");
                int movieRated = resultSet.getInt("movieRated");
                uniqueMovieRatings.put(movieName, movieRated);
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in a real application
        }

        return uniqueMovieRatings;
    }

    public boolean deleteUserRating(Connection connection,int userId,String movieName){
        String deleteQuery = "DELETE FROM plotarmor.ratedmovies WHERE userid = ? AND movieName = ?";

        try (
                // Create a PreparedStatement with the query
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)
        ) {
            // Set the parameters for the PreparedStatement
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, movieName);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if any rows were affected (if the rating was deleted)
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Handle any SQL exceptions (e.g., connection issues, syntax errors)
            e.printStackTrace(); // You may want to log the exception instead
            return false;
        }
    }

    public boolean updateRatingInDb(Connection connection, int userid, String movieName, int movieRated) {
        try {
            // update movie rating
            String insertQuery = "UPDATE plotarmor.ratedMovies SET movieRated = ? WHERE userid = ? AND movieName = ?";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, movieRated);
                insertStatement.setInt(2, userid);
                insertStatement.setString(3, movieName);

                insertStatement.executeUpdate();
                System.out.println("Bewertung erfolgreich geupdated.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in a real application
        }
        return false;

    }

}

class DatabaseQueryUser extends DatabaseQuery{
    public boolean isUserCredentialsValid(Connection connection, String email, String password) {
        // SQL query to check if there's a user with the given email and password
        String query = "SELECT * FROM plotarmor.user WHERE email = ?"; // AND password = ?
        try (connection) {
            // Check if the connection is null
            if (connection == null) {
                return false;
            }

            // Prepare a SQL query using a PreparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Explanation:
                // - This line creates a PreparedStatement object, which is a special type of statement used for executing parameterized SQL queries.
                // - It is created from the database connection (connection) and the SQL query (query) provided.


                // Set parameters in the prepared statement
                preparedStatement.setString(1, email);
                //preparedStatement.setString(2, "$2a$10$wkrZ9JD7WjsFtHL5XV/CFON2guERVeZ/OHVWW/J.xRMIQRrKgRapi");
                // Explanation:
                // - In the SQL query represented by query, there are placeholders denoted by ? (question marks).
                // - These placeholders are used to dynamically insert values into the query.
                // - The lines above set actual values for these placeholders in the prepared statement:
                //   - preparedStatement.setString(1, email): Sets the value of the first placeholder (?) to the value of the email variable.
                //   - preparedStatement.setString(2, password): Sets the value of the second placeholder (?) to the value of the password variable.
                // - The numbers 1 and 2 correspond to the positions of the placeholders in the SQL query.
                //   - The first placeholder is at position 1, and the second is at position 2.
                // By using a PreparedStatement, SQL injection is prevented:
                // - Automatic escape handling is provided by PreparedStatement for special characters in the input, enhancing security.

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && PasswordHashing.verifyPassword(password, resultSet.getString("password"))){
                        //System.out.println(PasswordHashing.verifyPassword(password, resultSet.getString("password")));
                            // User found, extract additional information (optional)
                            int id = resultSet.getInt("id");
                            String pw = resultSet.getString("password");
                            String email2 = resultSet.getString("email");
                            // Output additional information (optional)
                            System.out.println("ID: " + id + ", pw: " + pw + ", Email: " + email2);
                            saveUserInformation(resultSet);
                            UserInformation userInfo = UserInformation.getInstance();
                            setLastLoginToDb(connection, userInfo.getID());
                            return true;
                    } else {
                        // No user found with the given credentials
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            // Handle SQLException
            ex.printStackTrace();
            System.err.println("Fehler beim Ausführen der Abfrage: " + ex.getMessage());
            return false;
        }
    }
    // Method to save user information from a result
    public void saveUserInformation(ResultSet rSet) throws SQLException {
        UserInformation userInfo = UserInformation.getInstance();
        userInfo.setUsername(rSet.getString("username"));
        userInfo.setEmail(rSet.getString("email"));
        userInfo.setID(rSet.getInt("id"));
        userInfo.setPassword(rSet.getString("password"));
        userInfo.setRegisterDate(rSet.getString("registerdate"));
        userInfo.setLastLogin(rSet.getString("lastlogin"));

    }

    public void setLastLoginToDb(Connection connection, int userId ){
        String sql = "UPDATE plotarmor.user SET lastlogin = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(1, currentTimestamp);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create a new user
    public boolean createNewUser(Connection connection,String email, String username, String password) {
        String createNewUserQuery = "INSERT INTO plotarmor.user (email, username, password) VALUES (?, ?, ?)";


        try (connection) {
            // Check if the connection is null
            if (connection == null) {
                return false;
            }

            // Prepare a SQL query using a PreparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(createNewUserQuery)) {

                // Set parameters in the prepared statement
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);

                // Execute the query with executeUpdate() because of no return value and sql insert statement
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            // Handle SQLException
            ex.printStackTrace();
            System.err.println("Fehler beim Ausführen der Abfrage: " + ex.getMessage());
            return false; // Fehler beim Ausführen der Abfrage
        }

    }

    public boolean changeEmail(Connection connection, int id, String newEmail) throws SQLException {
        // SQL query to update the email address
        String changeEmailQuery = "UPDATE plotarmor.user SET email = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(changeEmailQuery)) {
            // Set parameters in the prepared statement
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, id);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }

        }
    }

    public boolean changeUsername(Connection connection, int id, String username) throws SQLException {
        // SQL query to update the username
        String changeUsernameQuery = "UPDATE plotarmor.user SET username = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(changeUsernameQuery)) {
            // Set parameters in the prepared statement
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, id);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    public boolean changePassword(Connection dbConnection, int id, String newPassword) throws SQLException {
        String changeQuery = "UPDATE plotarmor.user SET password = ? WHERE id = ?";


        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(changeQuery)) {
            {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, id);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Fehler beim Aktualisieren des Passworts: " + ex.getMessage());
        }

        return false;
    }

    public boolean updateNewPassword(String newPassword, String email) throws SQLException {
        String updateQuery = "UPDATE plotarmor.user SET password = ? WHERE email = ?";

        try (Connection connection = DatabaseConnection.connect()) {
            // Check if the connection is null
            if (connection == null) {
                return false;
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                {
                    preparedStatement.setString(1, newPassword);
                    preparedStatement.setString(2, email);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.err.println("Fehler beim Aktualisieren des Passworts: " + ex.getMessage());
            }
        }
        return false;
    }

    public boolean userNameIsInUse(Connection connection,String username) throws SQLException {
        String query = "SELECT username FROM plotarmor.user WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return false;

    }

    public boolean emailIsInDb(String email) throws SQLException {

        try (Connection connect = DatabaseConnection.connect()) {
            // Check if the connection is null
            if (connect == null) {
                return false;
            }
            // Prepare a SQL query using a PreparedStatement
            String sqlQueryEmailcheck = "SELECT COUNT(*) FROM plotarmor.user WHERE email = ?";
            // Prepare a SQL query using a PreparedStatement
            try (PreparedStatement preparedStatement = connect.prepareStatement(sqlQueryEmailcheck)) {
                // Set the email parameter for the prepared statement
                preparedStatement.setString(1, email);

                // Execute the query with executeUpdate() because of no return value and sql insert statement
                ResultSet resultSet = preparedStatement.executeQuery();
                // Check if the email exists in the database
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true; // Email exists in the database
                } else {
                    return false; // Email does not exist in the database
                }
            }
        } catch (SQLException ex) {
            // Handle SQLException
            ex.printStackTrace();
            System.err.println("Fehler beim Ausführen der Abfrage: " + ex.getMessage());
            return false; // Fehler beim Ausführen der Abfrage
        }
    }


    public boolean increaseLogin_attempt(Connection connection, String email) {
        String increasLogin_attempQuery = "UPDATE plotarmor.user SET login_attempts = login_attempts + 1 WHERE email = ?";

        // Check if the connection is null
        if (connection == null) {
            return false;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(increasLogin_attempQuery)) {
            {
                preparedStatement.setString(1, email);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Fehler beim Aktualisieren des LoginAttempts: " + ex.getMessage());
        }

        return false;
    }

    public int getLoginAttempt(Connection connection, String email) throws SQLException {
        int login_attempt = 0;
        String getLoginAtemptQuery = "SELECT login_attempts FROM plotarmor.user WHERE email = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(getLoginAtemptQuery);
        preparedStatement.setString(1, email);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                login_attempt = resultSet.getInt("login_attempts");
                return login_attempt;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return login_attempt;
    }

    public boolean updateLogin_attempt(Connection connection, String email) {
        String updateLogin_attempQuery = "UPDATE plotarmor.user SET login_attempts = 0 WHERE email = ?";

        // Check if the connection is null
        if (connection == null) {
            return false;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateLogin_attempQuery)) {
            {
                preparedStatement.setString(1, email);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Fehler beim Aktualisieren des LoginAttempts: " + ex.getMessage());
        }

        return false;
    }

    public boolean lockUser(Connection connection, String email) {
        String lockUser = "UPDATE plotarmor.user SET locked = 1 WHERE email = ?";

        // Check if the connection is null
        if (connection == null) {
            return false;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(lockUser)) {
            {
                preparedStatement.setString(1, email);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Fehler beim Aktualisieren des lockUser Arguments: " + ex.getMessage());
        }

        return false;
    }

    public boolean getLockedStatus(Connection connection, String email) throws SQLException {

        boolean userLocked = false;
        String getLoginAtemptQuery = "SELECT locked FROM plotarmor.user WHERE email = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(getLoginAtemptQuery);
        preparedStatement.setString(1, email);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                userLocked = resultSet.getBoolean("locked");
                return userLocked;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return userLocked;
    }

    public boolean setLockedStatusToZero(Connection connection, String email) {
        String lockUser = "UPDATE plotarmor.user SET locked = 0 WHERE email = ?";

        // Check if the connection is null
        if (connection == null) {
            return false;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(lockUser)) {
            {
                preparedStatement.setString(1, email);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Fehler beim Aktualisieren des lockUser Arguments: " + ex.getMessage());
        }

        return false;
    }

}