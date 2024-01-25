import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.html.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;

/**
 * The MovieDataLoader class is responsible for fetching and processing movie data
 * from The Movie Database (TMDb) API. It performs two API calls: one to the Discover
 * endpoint to retrieve a list of movies released in a specific year, and another to
 * the Movie Details endpoint for each movie to gather additional information such as
 * duration, genres, and release date.
 */
public class MovieDataLoader {

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String API_KEY = "55224fca1b5c1cd9c992943b5c33203a";

    // Query parameter for filtering by primary release year
    private static final String PRIMARY_RELEASE_YEAR_PARAM = "&primary_release_year=2023";
    DatabaseQuery dataQuery = new DatabaseQuery();

    //API CALL with HTTPRequest and HTTPClient library
    public void requestMovieDataWithHTTPRequest() throws URISyntaxException, IOException, InterruptedException {

        Connection connect  = DatabaseConnection.connect();
        // Iterate through multiple pages of the API
        for(int page = 2; page < 501; page++){
            // Construct the URI for the HTTP request
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(API_URL + "?api_key=" + API_KEY + PRIMARY_RELEASE_YEAR_PARAM + "&page=" + page))
                    .GET()
                    .build();

            // Create an HTTP client
            HttpClient client = HttpClient.newHttpClient();
            // Send the HTTP request and get the response
            HttpResponse<String> getResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString() );
            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(getResponse.body());
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            // Iterate through the JSON array and extract movie data
            for(int i = 0; i < jsonArray.length(); i++){
                int id = jsonArray.getJSONObject(i).getInt("id");
                // Call a method to retrieve detailed movie data using a second API call
                requestMovieDataWithHTTPRequestSecondAPICall(id,connect);

            }
        }



    }
    public void requestMovieDataWithHTTPRequestSecondAPICall(int movieId,Connection connect) throws URISyntaxException, IOException, InterruptedException {
        connect  = DatabaseConnection.connect();
        String apiUrlMovieDetails = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(apiUrlMovieDetails))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(getResponse.statusCode() == 200) {
            JSONObject detailsAbountMovieJson = new JSONObject(getResponse.body());
            JSONArray genres = detailsAbountMovieJson.getJSONArray("genres");
            // Extract movie details
            String background_path = "";
            String poster_path = "";
            String genre = "";
            int duration = detailsAbountMovieJson.getInt("runtime");
            String originalTitle = detailsAbountMovieJson.getString("title");
            String releaseDate = detailsAbountMovieJson.getString("release_date");
            String overview = detailsAbountMovieJson.getString("overview");
            double rating = detailsAbountMovieJson.getDouble("vote_average");

            //loop to save all genres to string
            for (int l = 0; l < genres.length(); l++) {
                genre += " " + genres.getJSONObject(l).getString("name");

            }
            if (detailsAbountMovieJson.has("backdrop_path") && !detailsAbountMovieJson.isNull("backdrop_path")) {
                // Überprüfen Sie, ob der Wert vom Typ String ist
                if (detailsAbountMovieJson.get("backdrop_path") instanceof String) {
                    background_path = "https://image.tmdb.org/t/p/w500";
                    background_path += detailsAbountMovieJson.getString("backdrop_path");
                }
            }
            if (detailsAbountMovieJson.has("poster_path") && !detailsAbountMovieJson.isNull("poster_path")) {
                if (detailsAbountMovieJson.get("poster_path") instanceof String) {
                    poster_path = "https://image.tmdb.org/t/p/w500";
                    poster_path += detailsAbountMovieJson.getString("poster_path");
                }
            }

            System.out.println("Title: " + originalTitle +
                    " | Overview: " + overview +
                    " | Release Date: " + releaseDate +
                    " | Duration: " + duration + " minutes" +
                    " | Genres: " + genre + " | rating: " + rating
                    + " | poster_path: " + poster_path +
                    " | background_path: " + background_path);

            dataQuery.saveMoviesToDb(connect,originalTitle,overview, Date.valueOf(releaseDate),duration,genre,rating,poster_path,background_path);
        }
    }
}
