package by.arsy.adapters;

import by.arsy.game.TennisRunner;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JBDCAdapter {

    private static final String URL = PropertiesAdapter.get("db.url");
    private static final String USER_NAME = PropertiesAdapter.get("db.username");
    private static final String PASSWORD = PropertiesAdapter.get("db.password");

    private JBDCAdapter() {
    }

    public static void updateGoal(int newRecord) {
        String sql = """
                UPDATE tennis_game
                SET coins_record = ?
                WHERE id = ?
                """;
        try (var connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {

            var statement = connection.prepareStatement(sql);
            statement.setInt(1, newRecord);
            statement.setInt(2, TennisRunner.getGamerId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getRecord() {
        String sql = """
                SELECT *
                FROM tennis_game
                WHERE id = ?
                """;
        try (var connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {

            var statement = connection.prepareStatement(sql);
            statement.setInt(1, TennisRunner.getGamerId());
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coins_record");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    public static void createTable() {
        String sql = """
                CREATE TABLE tennis_game(
                    id              serial     primary key,
                    coins_record    int
                )
                """;
        try (var connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {

            var statement = connection.prepareStatement(sql);
            statement.execute();

        } catch (SQLException e) {
            System.out.println("File already create");
        }
    }

    public static int createNewGamer() {
        String sql = """
                INSERT INTO tennis_game(coins_record)
                VALUES (0)
                
                RETURNING id
                """;
        try (var connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {

            var statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}
