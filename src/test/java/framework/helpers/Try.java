package framework.helpers;

import java.util.Optional;
import org.apache.commons.lang3.function.Failable;
import org.apache.commons.lang3.function.FailableSupplier;

/**
 * Try.failable(() -> Get.account(account.id()).answeredBy(actor)).orElseGet(() ->
 * Create.account(accountFormData).answeredBy(actor))
 */
public class Try<T> {

  private T t;

  private Try(FailableSupplier<T, Throwable> failableSupplier) {
    try {
      this.t = Failable.asSupplier(failableSupplier).get();
    } catch (Throwable ignored) { // NOSONAR
    }
  }

  public static <T> Try<T> failable(FailableSupplier<T, Throwable> failableSupplier) {
    return new Try<>(failableSupplier);
  }

  public T orElse(FailableSupplier<T, Throwable> failableSupplier) {
    return Optional.ofNullable(this.t).orElseGet(() -> Failable.asSupplier(failableSupplier).get());
  }
}
