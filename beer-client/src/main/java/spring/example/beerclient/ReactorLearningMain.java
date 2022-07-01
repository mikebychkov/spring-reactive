package spring.example.beerclient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class ReactorLearningMain {

    public static void main(String[] args) {
        //fun();
        //funGenerateStream();
        funTime();
    }

    private static void funTime() {

        Flux.range(1,100).delayElements(Duration.ofMillis(100)).elapsed().subscribe(t -> log.info("Elapsed {}: {}", t.getT1(), t.getT2()));

        //Flux.range(1,1000).para

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {}

    }

    private static void funGenerateStream() {

        Flux.push(sink -> {
                    IntStream.range(1000, 2000)
                            .forEach(sink::next);
                })
        //.delayElements(Duration.ofMillis(1))
        .take(10)
        .subscribe(e -> log.info("onNext: {}", e));

        //=========================================================================================
        log.info("=".repeat(100));

        Flux.generate(
                () -> Tuples.of(0L, 1L),
                (state, sink) -> {
                    log.info("Sink value: {}", state.getT2());
                    sink.next(state.getT2());
                    long nextValue = state.getT1() + state.getT2();
                    return Tuples.of(state.getT2(), nextValue);
                })
        //.delayElements(Duration.ofMillis(1))
        .take(10)
        .subscribe(e -> log.info("onNext: {}", e));

        //=========================================================================================
        log.info("=".repeat(100));

        //Flux.range(1, 100).onError

        //=========================================================================================
        log.info("=".repeat(100));

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {}

    }

    private static void fun() {

        Flux.range(1,5).subscribe((r) -> log.info("{}", r));
        Flux.range(1,5).scan(0, (a, i) -> a + i).subscribe((r) -> log.info("{}", r));

        //=========================================================================================
        log.info("=".repeat(100));

        Flux.range(1, 100).index()
                .scan(new int[2], (arr, el) -> {
                    log.info("=".repeat(50));
                    log.info("SCAN VALUES: {} + {}", Arrays.toString(arr), el.getT2());
                    int idx = (int) (el.getT1() % 2);
                    log.info("CALCULATED IDX: {}", idx);
                    arr[idx] = el.getT2();
                    return arr;
                })
                .skip(2)
                .map(arr -> {
                    log.info("ARRAY TO SUM: {}", Arrays.toString(arr));
                    return Arrays.stream(arr).sum();
                })
                .take(10)
                .subscribe(s -> log.info("SUM: {}", s));

        //=========================================================================================
        log.info("=".repeat(100));

        int[] arr1 = new int[100];
        arr1[1] = 1;

        Flux.range(2, 10)
                .scan(arr1, (arr, el) -> {
                    arr[el] = arr[el - 1] + arr[el - 2];
                    return arr;
                })
                .map(arr -> {
                    //log.info("ARRAY TO SUM: {}", Arrays.toString(arr));
                    return Arrays.stream(arr).max().getAsInt();
                })
                .subscribe(s -> log.info("FIB VAL: {}", s));

        //=========================================================================================
        log.info("=".repeat(100));

        log.info("{}", Thread.currentThread().getName());

        Flux.just(1, 2, 3).thenMany(Flux.just(4, 5)).subscribe(r -> {
            log.info("{}", Thread.currentThread().getName());
            log.info("{}", r);
        });

        //=========================================================================================
        log.info("=".repeat(100));

        Stream.of(List.of(1,2,3,4,5), List.of(21,22,23,24,25)).forEach(list -> list.stream().forEach(el -> log.info("{}", el)));

        //=========================================================================================
        log.info("=".repeat(100));

        Flux.range(1, 7)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd")
                .subscribe(group -> {
                    group.scan(new LinkedList<>(), (list, el) -> {
                                list.add(el);
                                if (list.size() > 2) {
                                    list.remove(0);
                                }
                                return list;
                            })
                            .filter(arr -> !arr.isEmpty())
                            .subscribe(data -> {
                                log.info("{}: {}", group.key(), data);
                            });
                });

        //=========================================================================================
        log.info("=".repeat(100));

        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20))
                .subscribe(e -> {
                    log.info("{}", Thread.currentThread().getName());
                    log.info("onNext: {}", e);
                });
        try {
            Thread.sleep(10000);
        } catch (Exception e) {}

    }
}
