package testdata;

import java.util.function.Consumer;

public class ProductDetails {

    private final String price;
    private final String shortDescription;

    public ProductDetails(String price, String shortDescription) {
        this.price = price;
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
                "price='" + price + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                '}';
    }

    public static class Builder {
        public String price;
        public String shortDescription;

        public ProductDetails.Builder with(Consumer<ProductDetails.Builder> builderFunction) {
            builderFunction.accept(this);
            return this;
        }

        public ProductDetails create() {
            return new ProductDetails(price, shortDescription);
        }
    }
}
