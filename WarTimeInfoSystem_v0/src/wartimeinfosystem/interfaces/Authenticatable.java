package wartimeinfosystem.interfaces;

/**
 * Interface for authentication operations
 */
public interface Authenticatable {
    boolean authenticate(String username, String password);
    boolean isAuthorized();
}
