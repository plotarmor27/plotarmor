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
    public void requestMovieData( ) throws IOException {
        Connection connect  = DatabaseConnection.connect();
        // Loop through pages to retrieve all results
        for(int page = 1; page < 501; page++){
            // Build URL for the Discover endpoint
            URL discoverMovieUrl = new URL(API_URL + "?api_key=" + API_KEY + PRIMARY_RELEASE_YEAR_PARAM + "&page=" + page);
            // Establish connection and get response code
            HttpURLConnection connection = (HttpURLConnection) discoverMovieUrl.openConnection();
            //Key-Word "Get" to request Movie Data
            connection.setRequestMethod("GET");
            //save the responseCode, 200 means the connection established without problems
            int responseCode = connection.getResponseCode();
            StringBuilder movieDataResponse = new StringBuilder();

            // Check if response is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response content
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    movieDataResponse.append(line);
                }
            }
            } else {
                System.out.println("Error retrieving API data. Response Code: " + responseCode);
            }


            // Process JSON data from the MovieData endpoint
            JSONObject MoviedataJSON = new JSONObject(movieDataResponse.toString());
            JSONArray moviePages  = MoviedataJSON.getJSONArray("results");
             System.out.println(page);
            // Loop through each movie page
            for (int i = 0; i < moviePages.length(); i++) {
                JSONObject moviePage = moviePages.getJSONObject(i);
                int movieId = moviePage.getInt("id");
                requestMovieDataSecondAPICall(movieId,connect);

            }

        }
    }

    public void requestMovieDataSecondAPICall(int movieId,Connection connect) throws IOException {
        // Second API call to retrieve additional details about movie
        String apiUrlMovieDetails = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        URL apiUrlDetailsPage = new URL(apiUrlMovieDetails);
        HttpURLConnection connectionDetailsPage = (HttpURLConnection) apiUrlDetailsPage.openConnection();
        connectionDetailsPage.setRequestMethod("GET");

        int responseCodeMovieDetails = connectionDetailsPage.getResponseCode();

        // Check if response is successful
        if (responseCodeMovieDetails == HttpURLConnection.HTTP_OK) {
            try (BufferedReader movieDetails = new BufferedReader(new InputStreamReader(connectionDetailsPage.getInputStream())))
            {
                StringBuilder detailsResponse = new StringBuilder();
                String detailsLine;
                while ((detailsLine = movieDetails.readLine()) != null)
                {
                    detailsResponse.append(detailsLine);
                }

                // Process JSON data from the MovieDataDetails endpoint
                JSONObject detailsJson = new JSONObject(detailsResponse.toString());
                //JSONArray genres = detailsJson.getJSONArray("genres");

                System.out.println(movieId);
                // Extract movie details
              //  int duration = detailsJson.getInt("runtime");
                String originalTitle = detailsJson.getString("title");
              //  String releaseDate = detailsJson.getString("release_date");
              //  String overview = detailsJson.getString("overview");
               // String genre = "";
               // double rating = detailsJson.getDouble("vote_average");
                String background_path = "";


                if (detailsJson.has("backdrop_path") && !detailsJson.isNull("backdrop_path")) {
                    // Überprüfen Sie, ob der Wert vom Typ String ist
                    if (detailsJson.get("backdrop_path") instanceof String) {
                        background_path = "https://image.tmdb.org/t/p/w500";
                        background_path += detailsJson.getString("backdrop_path");
                    } else {
                        // Handle den Fall, wenn der Wert nicht vom Typ String ist
                        System.out.println("Der Wert für backdrop_path ist nicht vom Typ String.");
                    }
                }

                String poster_path = "";
                if (detailsJson.has("poster_path") && !detailsJson.isNull("poster_path")) {
                    if (detailsJson.get("poster_path") instanceof String) {
                        poster_path = "https://image.tmdb.org/t/p/w500";
                        poster_path += detailsJson.getString("poster_path");
                    } else {
                        System.out.println("Der Wert für poster_path ist nicht vom Typ String.");
                    }
                }
                //loop to save all genres to string
              //  for(int l = 0; l < genres.length(); l++)
              //  {
              //      genre += " " + genres.getJSONObject(l).getString("name");

              //  }

                // Print movie details
                connect  = DatabaseConnection.connect();
                //dataQuery.saveMoviesToDb(connect,originalTitle,overview, Date.valueOf(releaseDate),duration,genre);
                //dataQuery.setRatingWhereTitle(connect,originalTitle, String.valueOf(rating));
              //  dataQuery.setPosterAndBackgroundPath(connect,background_path,poster_path,originalTitle);
               // System.out.println("Background: " + background_path + " poster: " + poster_path + " title:" + originalTitle + " movieId:" + movieId);
                //   System.out.println("Title: " + originalTitle +
              //          " | Overview: " + overview +
              //          " | Release Date: " + releaseDate +
             //           " | Duration: " + duration + " minutes" +
             //           " | Genres: " + genre);


            }
        }

    }

    //API CALL with HTTPRequest and HTTPClient library
    public void requestMovieDataWithHTTPRequest() throws URISyntaxException, IOException, InterruptedException {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(API_URL + "?api_key=" + API_KEY + PRIMARY_RELEASE_YEAR_PARAM + "&page=" + 1))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> getResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString() );

        System.out.println(getResponse.body());
        JSONObject jsonObject = new JSONObject(getResponse.body());
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        JSONObject title = jsonArray.getJSONObject(0);
        System.out.println(jsonObject.getJSONArray("results").getJSONObject(0).getString("title"));

    }

}
