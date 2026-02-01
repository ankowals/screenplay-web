package com.github.ankowals.framework.screenplay;

import com.github.ankowals.framework.screenplay.actor.Actor;

// Proposed naming convention
// TheCreation.of(CustomerTemplate customerTemplate)
// TheRegistration.of(Asset asset)
// TheDevice.details(String customerId)
@FunctionalInterface
public interface Question<T> {
  T answeredBy(Actor actor) throws Exception;
}
