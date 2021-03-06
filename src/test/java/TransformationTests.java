import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransformationTests {

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
    public void collectMap() {
        var subscriber = new HashMap<Integer, Integer>();

        Flux.just(1, 2, 3, 4)
                .collectMap(k -> k * 2)
                .subscribe(subscriber::putAll);

        assertThat(subscriber).containsKeys(2, 4, 6, 8);
        assertThat(subscriber).containsValues(1, 2, 3, 4);
    }

    @Test
    public void collectList() {
        var subscriber = new ArrayList<Integer>();

        Flux.just(1, 2, 3, 4)
                .collectList()
                .subscribe(subscriber::addAll);

        assertThat(subscriber).contains(1, 2, 3, 4);
    }

    @Test
    public void reducer() {
        final Integer[] sum = {0};

        Flux.just(1, 2, 3, 4)
                .reduce((accumulator, value) -> accumulator += value)
                .subscribe( x -> sum[0] = x);

        assertThat(sum[0]).isEqualTo(10);
    }

    @Test
    public void distinct() {
        var elements = new ArrayList<Integer>();

        Flux.just(1, 2, 2, 2, 3)
                .distinct()
                .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3);
    }
}
