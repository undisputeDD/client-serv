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

}