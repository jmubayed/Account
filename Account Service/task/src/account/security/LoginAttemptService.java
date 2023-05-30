package account.security;

public interface LoginAttemptService {
    void loginSucceeded(String username);
    void loginFailed(String username);
    boolean isBlocked(String username);
    void resetAttempts(String username);
}
