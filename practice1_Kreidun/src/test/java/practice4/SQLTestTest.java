package practice4;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice1.Product;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SQLTestTest {

    @Test
    void productInsertion() {
        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("HelloDB");

        int size = sqlTest.getAllProducts().size();
        Product product = new Product("test prod", 1, 1);
        product = sqlTest.insertProduct(product);
        assertThat(product.getId()).isNotNull();

        assertThat(sqlTest.getAllProducts().size() - 1).isEqualTo(size);
    }

    @Test
    void selectByCriteria() {
        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("HelloDB");

        Product prod1 = sqlTest.insertProduct(new Product("product1", 10.5, 5.5));
        Product prod2 = sqlTest.insertProduct(new Product("product2", 10, 5.5));
        Product prod3 = sqlTest.insertProduct(new Product("product3", 10, 5));
        Product prod4 = sqlTest.insertProduct(new Product("some", 100, 7));

        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setName("prod");
        assertThat(sqlTest.getByCriteria(productCriteria))
                .containsExactlyInAnyOrder(prod2, prod1, prod3);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void selectByCriteria(ProductCriteria productCriteria, List<Product> expectedProducts) {
        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("HelloDB");

        Product prod1 = sqlTest.insertProduct(new Product("product1", 10.5, 5.5));
        Product prod2 = sqlTest.insertProduct(new Product("product2", 10, 5.5));
        Product prod3 = sqlTest.insertProduct(new Product("product3", 10, 5));
        Product prod4 = sqlTest.insertProduct(new Product("some", 100, 7));

        assertThat(sqlTest.getByCriteria(productCriteria))
                .containsExactlyInAnyOrderElementsOf(expectedProducts);
    }

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(
                        new ProductCriteria("prod", null, null, null, null),
                        List.of(new Product(1, "product1", 10.5, 5.5),
                                new Product(2, "product2", 10, 5.5),
                                new Product(3, "product3", 10, 5))
                ),
                Arguments.of(
                        new ProductCriteria(),
                        List.of(new Product(1, "product1", 10.5, 5.5),
                                new Product(2, "product2", 10, 5.5),
                                new Product(3, "product3", 10, 5),
                                new Product(4, "some", 100, 7))
                )
        );
    }

}