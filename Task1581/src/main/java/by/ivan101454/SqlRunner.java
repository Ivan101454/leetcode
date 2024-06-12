package by.ivan101454;

import by.ivan101454.utility.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 */
public class SqlRunner {
    public static void main(String[] args) throws SQLException {
        String sqlCreate = """
                CREATE TABLE IF NOT EXISTS visits (
                visit_id INT PRIMARY KEY,
                customer_id INT
                );
                CREATE TABLE IF NOT EXISTS transactions (
                transaction_id INT PRIMARY KEY,
                visit_id INT REFERENCES visits(visit_id),
                amount INT);
                                """;

        String sqlInsert = """
                INSERT INTO visits (visit_id, customer_id) VALUES
                 (1, 23),
                 (2, 9),
                 (4, 30),
                 (5, 54),
                 (6, 96),
                 (7, 54),
                 (8, 54);
                
                INSERT INTO transactions (transaction_id, visit_id, amount) VALUES
                 (2, 5, 310),
                 (3, 5, 300),
                 (9, 5, 200),
                 (12, 1, 910),
                 (13, 2, 970);
                """;

        String sqlSelect = """
                SELECT v.customer_id, count(v.visit_id) AS count_no_trans
                FROM visits v
                LEFT JOIN  transactions t
                ON v.visit_id = t.visit_id
                WHERE t.transaction_id IS NULL
                GROUP BY v.customer_id; 
                """;
        try (var connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            connection.setSchema("leetcode");
            statement.execute(sqlCreate);
            statement.execute(sqlInsert);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("customer_id") + " " + resultSet.getInt("count_no_trans"));
            }
        }
    }
}
