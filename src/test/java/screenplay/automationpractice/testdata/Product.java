package testdata;

import java.util.function.Consumer;

public class Product {

  private final String price;
  private final String shortDescription;

  public Product(String price, String shortDescription) {
    this.price = price;
    this.shortDescription = shortDescription;
  }

  public String getPrice() {
    return this.price;
  }

  public String getShortDescription() {
    return this.shortDescription;
  }

  @Override
  public String toString() {
    return "ProductDetails{"
        + "price='"
        + this.price
        + '\''
        + ", shortDescription='"
        + this.shortDescription
        + '\''
        + '}';
  }

  public static class Builder {
    public String price;
    public String shortDescription;

    public Product.Builder with(Consumer<Product.Builder> builderFunction) {
      builderFunction.accept(this);
      return this;
    }

    public Product build() {
      return new Product(this.price, this.shortDescription);
    }
  }
}
