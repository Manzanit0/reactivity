import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class HotStreamsTests {

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
}
