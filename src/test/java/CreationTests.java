import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CreationTests {

    @Test
    public void createsFluxFromIndividualObjects() {
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void createsFluxFromRange() {
        List<Integer> elements = new ArrayList<>();

        Flux.range(1, 5)
                .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void createsFluxFromIterable() {
        List<String> elements = new ArrayList<>();

        Flux.fromIterable(List.of("red", "green", "blue"))
                .subscribe(elements::add);

        assertThat(elements).containsExactly("red", "green", "blue");
    }

    @Test
    public void createsFluxFromStream() {
        List<String> elements = new ArrayList<>();

        Stream<String> s = Stream.of("rum", "gin");
        Flux.fromStream(s).
                subscribe(elements::add);

        assertThat(elements).containsExactly("rum", "gin");
    }

    @Test
    public void fluxCanBeCreatedFromAnExceptionToo() {
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
    public void createSimpleFluxWithEvents() {
        var elements = new ArrayList<String>();
        var errors = new ArrayList<Throwable>();

        Flux<String> flux = Flux.create(subscriber -> {
            subscriber.next("Batman");
            subscriber.next("Green Lantern");
            throw new RuntimeException("Spiderman");
        });

        // Additionally, it can receive a third handler, for onComplete.
        flux.subscribe(elements::add, errors::add);

        assertThat(elements).containsExactly("Batman", "Green Lantern");
        assertThat(errors.get(0).getMessage()).isEqualTo("Spiderman");
    }

    @Test
    public void fluxAreLazy() {
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

    @Test
    public void addTwoSubscribersToFlux() {
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();

        var flux = Flux.range(1, 5);

        flux.subscribe(first::add);
        flux.subscribe(second::add);

        assertThat(first).containsExactly(1, 2, 3, 4, 5);
        assertThat(second).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void fluxesCanBeCancelled() {
        List<Integer> elements = new ArrayList<>();

        Flux<Integer> flux = Flux.create(subscriber -> {
            subscriber.next(1);
            subscriber.next(2);
            subscriber.next(3);

            subscriber.next(4);
            subscriber.next(5);
            subscriber.next(6);
        });

        // Take triggers the complete event.
        flux.take(3).subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
