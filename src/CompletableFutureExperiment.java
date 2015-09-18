import java.time.Duration;
import java.util.concurrent.*;

public class CompletableFutureExperiment {
    public CompletableFuture<String> getString(Duration duration) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        Executors.newScheduledThreadPool(1).schedule(() -> future.complete("GAURAV IS A GOD!!!"), duration.toMillis(), TimeUnit.MILLISECONDS);
        return future;
    }

    private CompletableFuture<String> timeoutAfter(Duration duration) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        Executors.newScheduledThreadPool(1).schedule(() -> future.completeExceptionally(new TimeoutException(
                "Don't kill Kennady. Osama is high.")), duration.toMillis(), TimeUnit.MILLISECONDS);
        return future;
    }

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        CompletableFutureExperiment experiment = new CompletableFutureExperiment();
        System.out.println(experiment.getString(Duration.ofSeconds(4))
                .applyToEither(experiment.timeoutAfter(Duration.ofSeconds(5)), p -> p).get());
    }
}
