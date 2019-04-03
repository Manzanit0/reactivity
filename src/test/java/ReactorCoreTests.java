import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCases {

    @Test
    public void listsCanSubscribeToFlux() {
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .subscribe(elements::add);

        assertThat(elements).containsExactly(1, 2, 3, 4);
    }
}
