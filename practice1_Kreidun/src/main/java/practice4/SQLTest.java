package practice4;

import practice1.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLTest {
    private Connection con;
        
    public void initialization(String name){
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite::memory:");
            PreparedStatement st = con.prepareStatement(
                    "create table if not exists 'product' "
                            + "('id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "'name' text, "
                            + "'price' double, "
                            + "'amount' double"
                            + ");");
            int result = st.executeUpdate();
        }catch(ClassNotFoundException e){
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }

    }

    // Or Create
    public Product insertProduct(Product product){
        try (PreparedStatement statement = con.prepareStatement("INSERT INTO product(name, price, amount) VALUES (?, ?, ?)")) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setDouble(3, product.getAmount());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            product.setId(resultSet.getInt("last_insert_rowid()"));

            return product;
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Product> getAllProducts(){
        try (
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM product")
        ) {
            List<Product> result = new ArrayList<>();
            int i = 0;
            while (res.next()) {
                Integer id = res.getInt("id");
                String name = res.getString("name");
                Double price = res.getDouble("price");
                Double amount = res.getDouble("amount");

                Product product = new Product(id, name, price, amount);
                // System.out.println(" |- " + product);
                result.add(product);
            }
            res.close();
            st.close();
            return result;
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return null;
    }

    // Also can be considered as Read method
    public List<Product> getByCriteria(ProductCriteria productCriteria) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM product WHERE ");

        if (productCriteria.getName() != null) {
            sb.append(" name like '%").append(productCriteria.getName()).append("%' and ");
        }

        if (productCriteria.getPriceFrom() != null) {
            sb.append(" price >= ").append(productCriteria.getPriceFrom()).append(" and ");
        }

        if (productCriteria.getPriceTo() != null) {
            sb.append(" price <= ").append(productCriteria.getPriceTo()).append(" and ");
        }

        if (productCriteria.getAmountFrom() != null) {
            sb.append(" amount >= ").append(productCriteria.getAmountFrom()).append(" and ");
        }

        if (productCriteria.getAmountTo() != null) {
            sb.append(" amount <= ").append(productCriteria.getAmountTo()).append(" and ");
        }

        sb.append(" 1 = 1");

        try (
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sb.toString())
        ) {
            List<Product> result = new ArrayList<>();
            int i = 0;
            while (res.next()) {
                Integer id = res.getInt("id");
                String name = res.getString("name");
                Double price = res.getDouble("price");
                Double amount = res.getDouble("amount");

                Product product = new Product(id, name, price, amount);
                // System.out.println(" |- " + product);
                result.add(product);
            }
            res.close();
            st.close();
            return result;
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return null;
    }

    public void updateProduct(Product product) {
        try (PreparedStatement statement = con.prepareStatement("UPDATE product SET name = ?, price = ?, amount = ? WHERE id = ?")) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setDouble(3, product.getAmount());
            statement.setInt(4, product.getId());

            System.out.println(statement.toString());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public void deleteProductByName(String name) {
        try (PreparedStatement statement = con.prepareStatement("DELETE FROM product WHERE name = '" + name + "';");) {
            System.out.println(statement.toString());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public void deleteProductById(int id) {
        try (PreparedStatement statement = con.prepareStatement("DELETE FROM product WHERE id = '" + id + "';");) {
            System.out.println(statement.toString());
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("HelloDB");
        Product prod1 = sqlTest.insertProduct(new Product("product1", 10.5, 5.5));
        //System.out.println(prod1);
        Product prod2 = sqlTest.insertProduct(new Product("product2", 10, 5.5));
        //System.out.println(prod2);
        Product prod3 = sqlTest.insertProduct(new Product("product3", 10, 5));
        //System.out.println(prod3);
        Product prod4 = sqlTest.insertProduct(new Product("some", 100, 7));

        ProductCriteria productCriteria = new ProductCriteria();
        System.out.println(sqlTest.getByCriteria(productCriteria));

        productCriteria = new ProductCriteria();
        productCriteria.setName("prod");
        System.out.println(sqlTest.getByCriteria(productCriteria));

        productCriteria = new ProductCriteria();
        productCriteria.setAmountFrom(5.5);
        System.out.println(sqlTest.getByCriteria(productCriteria));

        productCriteria = new ProductCriteria();
        productCriteria.setName("some");
        productCriteria.setAmountTo(8.0);
        System.out.println(sqlTest.getByCriteria(productCriteria));

        productCriteria = new ProductCriteria();
        productCriteria.setName("some");
        productCriteria.setAmountTo(6.0);
        System.out.println(sqlTest.getByCriteria(productCriteria));

        productCriteria = new ProductCriteria();
        productCriteria.setName("prod");
        productCriteria.setPriceTo(10.0);
        System.out.println(sqlTest.getByCriteria(productCriteria));

        productCriteria = new ProductCriteria();
        productCriteria.setPriceFrom(10.1);
        System.out.println(sqlTest.getByCriteria(productCriteria));

        System.out.println(sqlTest.getAllProducts());

        sqlTest.updateProduct(new Product(1, "someProd", 1000, 25));

        System.out.println(sqlTest.getAllProducts());

        sqlTest.deleteProductByName("someProd");

        System.out.println(sqlTest.getAllProducts());

        sqlTest.deleteProductById(4);

        System.out.println(sqlTest.getAllProducts());

    }
}
