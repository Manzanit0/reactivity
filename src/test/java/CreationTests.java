import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CreationTests {

    @Test
    public void createsStreamFromIndividualObjects() {
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void createsStreamFromRange() {
        List<Integer> elements = new ArrayList<>();

        Flux.range(1, 5)
                .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void createsStreamFromIterable() {
        List<String> elements = new ArrayList<>();

        Flux.fromIterable(List.of("red", "green", "blue"))
                .subscribe(elements::add);

        assertThat(elements).containsExactly("red", "green", "blue");
    }

    @Test
    public void createsStreamFromStream() {
        List<String> elements = new ArrayList<>();

        Stream<String> s = Stream.of("rum", "gin");
        Flux.fromStream(s).
                subscribe(elements::add);

        assertThat(elements).containsExactly("rum", "gin");
    }

    @Test
    public void streamsCanBeCreatedFromAnExceptionToo() {
        var list = new ArrayList<>();

        var exceptionThrown = false;
        try {
            Flux.error(new IllegalStateException())
                    .subscribe(list::add);
        }
        catch (Exception ex) {
            exceptionThrown = true;
        }

        assertThat(exceptionThrown).isEqualTo(true);
    }

    @Test
    public void streamsAreLazy() {
        long startTime = System.currentTimeMillis();

        Flux.create(subscriber -> {
            sleep(5000);
            subscriber.next(1);
        });

        long endTime   = System.currentTimeMillis();
        var elapsedTime = endTime - startTime;

        // Assert that not even 1 second has passed.
        assertThat(elapsedTime).isLessThan(1000);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
