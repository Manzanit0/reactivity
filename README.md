# Reactivity

This project contains a test suite which I personally used to get get acquainted
with reactive streams and how [Project Reactor](https://projectreactor.io/) API works.

### Creation

The tests to see how to create reactive streams from different sources can be found here:
[CreationTests.java](src/test/java/CreationTests.java).

### Transformation

The tests for applying different kind of transformations to a Flux instance can be found here:
[TransformationTests.java](src/test/java/TransformationTests.java).

The following have been covered, as well as some general concepts:

1. map
2. collectMap & collectList
3. reducer
4. distinct

### Hot streams

The main difference between a cold stream and a hot stream, according to the documentation:

>  [Cold streams] generate data anew for each subscription. If no subscription is created, 
then data never gets generated.

On the other hand, hot publishers:

> Hot publishers, on the other hand, do not depend on any number of subscribers. 
They might start publishing data right away and would continue doing so whenever a new Subscriber 
comes in (in which case said subscriber would only see new elements emitted after it subscribed). 
For hot publishers, something does indeed happen before you subscribe.

The tests for creating hot streams can be found here:
[HotStreamTests.java](src/test/java/HotStreamsTests.java).