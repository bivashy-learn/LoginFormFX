package com.bivashy.javafx.authentication.database.repository;

import com.bivashy.javafx.authentication.database.JDBCConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseJDBCRepository<T, ID> {

    protected abstract T mapRowToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void mapEntityToPreparedStatement(T entity, PreparedStatement preparedStatement) throws SQLException;

    private final JDBCConnectionPool connectionPool;
    private final String tableName;
    private final String idColumn;

    public BaseJDBCRepository(JDBCConnectionPool connectionPool, String tableName, String idColumn) {
        this.connectionPool = connectionPool;
        this.tableName = tableName;
        this.idColumn = idColumn;
    }

    protected Connection createConnection() throws SQLException {
        return connectionPool.createConnection();
    }

    public void save(T entity, String insertQuery) {
        try (Connection connection = createConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            mapEntityToPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving entity", e);
        }
    }

    public Optional<T> findById(ID id) {
        String query = String.format("SELECT * FROM %s WHERE %s = ?", tableName, idColumn);
        try (Connection connection = createConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToEntity(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding entity by ID", e);
        }
    }

    public List<T> findAll() {
        String query = String.format("SELECT * FROM %s", tableName);
        try (Connection connection = createConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(mapRowToEntity(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all entities", e);
        }
    }

    public void deleteById(ID id) {
        String query = String.format("DELETE FROM %s WHERE %s = ?", tableName, idColumn);
        try (Connection connection = createConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting entity by ID", e);
        }
    }

    public Optional<T> findOneByColumn(String columnName, Object value) {
        String query = String.format("SELECT * FROM %s WHERE %s = ?", tableName, columnName);
        try (Connection connection = createConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToEntity(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error finding entity in %s by %s = %s", tableName, columnName, value), e
            );
        }
    }

    public List<T> findAllByColumn(String columnName, Object value) {
        String query = String.format("SELECT * FROM %s WHERE %s = ?", tableName, columnName);
        try (Connection connection = createConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(mapRowToEntity(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error finding entities in %s by %s = %s", tableName, columnName, value), e
            );
        }
    }

}
