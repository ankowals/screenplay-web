package framework.screenplay.helpers;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.stream.Streams;

public class InParallel {

  private final List<Actor> actors;

  private InParallel(List<Actor> actors) {
    this.actors = actors;
  }

  public static InParallel actors(Actor... actors) {
    return new InParallel(Arrays.asList(actors));
  }

  /**
   * InParallel.perform( () -> actor1.attemptsTo(Find.productDetails("Dress")), () ->
   * actor2.attemptsTo(Find.productDetails("Other Dress")));
   */
  public static void perform(FailableRunnable<?>... interactions) {
    Streams.failableStream(Arrays.stream(interactions).parallel()).forEach(FailableRunnable::run);
  }

  /** InParallel.theActors(actor1, actor2).eachAttemptsTo(Find.productDetails("Dress")); */
  public final void eachAttemptsTo(Interaction... interactions) {
    this.actors.parallelStream().forEach(actor -> actor.attemptsTo(interactions));
  }
}
