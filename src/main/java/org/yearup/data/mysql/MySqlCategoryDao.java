package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // Create an empty list to store the categories
        List<Category> categories = new ArrayList<>();

        try (Connection connection = getConnection();
             // Prepare the SQL statement to select all categories
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM categories");
             // Execute the SQL statement and get the result set
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Create a Category object by mapping the current row of the result set
                Category category = mapRow(resultSet);
                // Add the category to the list
                categories.add(category);
            }
        } catch (SQLException e) {
            // Throw a runtime exception if there is an error retrieving categories from the database
            throw new RuntimeException("Failed to retrieve categories from the database.", e);
        }

        // Return the list of categories
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        try (Connection connection = getConnection();
             // Prepare the SQL statement to select a category by ID
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM categories WHERE category_id = ?")) {

            // Set the category_id parameter in the prepared statement
            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Create a Category object by mapping the current row of the result set
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            // Throw a runtime exception if there is an error retrieving a category from the database
            throw new RuntimeException("Failed to retrieve category from the database.", e);
        }

        // Throw a runtime exception if the category is not found
        throw new RuntimeException("Category not found for the given category ID: " + categoryId);
    }

    @Override
    public Category create(Category category) {
        try (Connection connection = getConnection();
             // Prepare the SQL statement to insert a new category
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO categories (name, description) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set the values for the name and description parameters in the prepared statement
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            // Execute the SQL statement and get the number of affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                // Throw a runtime exception if the insertion failed
                throw new RuntimeException("Failed to create category in the database.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Get the auto-generated category ID and set it in the category object
                    int generatedCategoryId = generatedKeys.getInt(1);
                    category.setCategoryId(generatedCategoryId);
                    return category;
                }
            }
        } catch (SQLException e) {
            // Throw a runtime exception if there is an error creating a category in the database
            throw new RuntimeException("Failed to create category in the database.", e);
        }

        // Throw a runtime exception if the auto-generated category ID cannot be retrieved
        throw new RuntimeException("Failed to retrieve the auto-generated category ID.");
    }

    @Override
    public void update(int categoryId, Category category) {
        // Prepare the SQL statement to update a category by ID
        String sql = "UPDATE categories " +
                "SET name = ?, description = ? " +
                "WHERE category_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the values for the name, description, and category_id parameters in the prepared statement
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            // Execute the SQL statement and get the number of affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                // Throw a runtime exception if the update failed
                throw new RuntimeException("Failed to update category in the database.");
            }
        } catch (SQLException e) {
            // Throw a runtime exception if there is an error updating a category in the database
            throw new RuntimeException("Failed to update category in the database.", e);
        }
    }

    @Override
    public void delete(int categoryId) {
        // Prepare the SQL statement to delete a category by ID
        String sql = "DELETE FROM categories " +
                "WHERE category_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the category_id parameter in the prepared statement
            statement.setInt(1, categoryId);

            // Execute the SQL statement and get the number of affected rows
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                // Throw a runtime exception if the deletion failed
                throw new RuntimeException("Failed to delete category from the database.");
            }
        } catch (SQLException e) {
            // Throw a runtime exception if there is an error deleting a category from the database
            throw new RuntimeException("Failed to delete category from the database.", e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException {
        // Retrieve the values from the current row of the result set
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        // Create a new Category object and set its properties
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName(name);
        category.setDescription(description);

        return category;
    }
}

