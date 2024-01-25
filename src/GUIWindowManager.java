/**
 * Manages the status of various GUI windows in the application.
 * Singleton class to ensure only one instance is created.
 */
public class GUIWindowManager {
    private static GUIWindowManager instance;

    private boolean accountSettingOpen = false;
    private boolean emailVerificationOpen = false;
    private boolean movieInformationControllerOpen = false;
    private boolean myRatedMoviesOpen = false;
    private boolean resetPasswordOpen = false;

    private GUIWindowManager() {

    }

    public static synchronized GUIWindowManager getInstance() {
        if (instance == null) {
            instance = new GUIWindowManager();
        }
        return instance;
    }

    public boolean isAccountSettingOpen() {
        return accountSettingOpen;
    }

    public void setAccountSettingOpen(boolean open) {
        accountSettingOpen = open;
    }

    public boolean isEmailVerificationOpen() {
        return emailVerificationOpen;
    }

    public void setEmailVerificationOpen(boolean open) {
        emailVerificationOpen = open;
    }

    public boolean isMovieInformationControllerOpen() {
        return movieInformationControllerOpen;
    }

    public void setMovieInformationControllerOpen(boolean open) {
        movieInformationControllerOpen = open;
    }

    public boolean isMyRatedMoviesOpen() {
        return myRatedMoviesOpen;
    }

    public void setMyRatedMoviesOpen(boolean open) {
        myRatedMoviesOpen = open;
    }

    public boolean isResetPasswordOpen() {
        return resetPasswordOpen;
    }

    public void setResetPasswordOpen(boolean open) {
        resetPasswordOpen = open;
    }
}
