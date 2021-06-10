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

    public List<Product> getByCriteria(){
        

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
    
    public static void main(String[] args){
        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("HelloDB");
        Product prod1 = sqlTest.insertProduct(new Product("product1", 10.5, 5.5));
        System.out.println(prod1);
        Product prod2 = sqlTest.insertProduct(new Product("product2", 10, 5.5));
        System.out.println(prod2);
        Product prod3 = sqlTest.insertProduct(new Product("product3", 10, 5));
        System.out.println(prod3);
        System.out.println(sqlTest.getAllProducts());
    }
}
