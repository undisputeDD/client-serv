package practice4;

import org.junit.jupiter.api.Test;
import practice1.Product;

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

}