import reactor.core.publisher.Flux;

public class Main {
    public static void main(String[] args) {
        Flux<String> just = Flux.just("1", "2", "3");
    }
}
