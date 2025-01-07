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

  public static InParallel theActors(Actor... actors) {
    return new InParallel(Arrays.asList(actors));
  }

  /**
   * InParallel.perform( () -> actor1.attemptsTo(Open.browser("http://www.dress.shop"),
   * Find.productDetails("Printed Chiffon Dress")), () ->
   * actor2.attemptsTo(Open.browser("http://www.other.shop"), Find.productDetails("Other
   * Product")));
   */
  public static void perform(FailableRunnable<?>... interactions) {
    Streams.failableStream(Arrays.stream(interactions).parallel()).forEach(FailableRunnable::run);
  }

  /**
   * InParallel.theActors(actor1, actor2) .eachAttemptTo( Open.browser("http://www.dress.shop"),
   * Find.productDetails("Printed Chiffon Dress"));
   */
  @SafeVarargs
  public final void eachAttemptTo(Interaction<Actor>... interactions) {
    this.actors.parallelStream().forEach(actor -> actor.attemptsTo(interactions));
  }
}
