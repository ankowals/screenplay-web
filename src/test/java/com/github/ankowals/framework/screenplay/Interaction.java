package com.github.ankowals.framework.screenplay;

import com.github.ankowals.framework.screenplay.actor.Actor;

// Proposed naming convention
// Delete.customer(Customer customer)
// Setup.device(Device device)
// Subscribe.to(Subscription subscription)
@FunctionalInterface
public interface Interaction {
  void performAs(Actor actor) throws Exception;
}
