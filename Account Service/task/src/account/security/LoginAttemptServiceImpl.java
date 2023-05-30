package account.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptServiceImpl() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS) // Cache expiration time
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0; // Default value for new attempts
                    }
                });
    }

    @Override
    public void loginSucceeded(String username) {
        attemptsCache.invalidate(username);
    }

    @Override
    public void loginFailed(String username) {
        int attempts;
        try {
            attempts = attemptsCache.get(username);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(username, attempts);
    }

    @Override
    public boolean isBlocked(String username) {
        try {
            return attemptsCache.get(username) >= MAX_ATTEMPTS;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    @Override
    public void resetAttempts(String username) {
        attemptsCache.invalidate(username);
    }
}
