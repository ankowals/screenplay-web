package com.github.ankowals.framework.screenplay.abilities.cleanup;

import java.util.List;
import java.util.stream.Collectors;

public class CleanUpFailedException extends RuntimeException {
  CleanUpFailedException(List<Throwable> errors) {
    super(
        "Clean up has failed!. Please see log file for details. Following exceptions were thrown: %s"
            .formatted(
                errors.stream()
                    .map(Throwable::getMessage)
                    .collect(Collectors.joining(System.lineSeparator()))));
  }
}
