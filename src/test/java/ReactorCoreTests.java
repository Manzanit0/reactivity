import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactorCoreTests {

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

    @Test
    public void transformationsAreExecutedUponSubscription() {
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .subscribe(elements::add);

        assertThat(elements).containsExactly(2, 4, 6, 8);
    }

    @Test
    public void eachTransformationReturnsANewStream() {
        List<Integer> doubledElements = new ArrayList<>();
        List<Integer> incrementedElements = new ArrayList<>();

        var doubled = Flux.just(1, 2, 3, 4)
                .map(i -> i * 2);

        var incremented = doubled
                .map(i -> i + 1);

        doubled.subscribe(doubledElements::add);
        incremented.subscribe(incrementedElements::add);

        assertThat(doubledElements).containsExactly(2, 4, 6, 8);
        assertThat(incrementedElements).containsExactly(3, 5, 7, 9);
    }

    @Test
    public void twoStreamsCanBeCombined() {
        List<String> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two))
                .subscribe(elements::add);

        assertThat(elements).containsExactly(
                "First Flux: 2, Second Flux: 0",
                "First Flux: 4, Second Flux: 1",
                "First Flux: 6, Second Flux: 2",
                "First Flux: 8, Second Flux: 3");
    }

    @Test
    public void hotStreamsDontPublishUnlessConnected() {
        ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        }).publish();

        var elements = new ArrayList<>();
        publish.subscribe(elements::add);

        assertThat(elements).isEmpty();

        // Once connected, it will stream infinitely.
        // publish.connect();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
