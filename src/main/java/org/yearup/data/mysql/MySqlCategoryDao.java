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

    // Constructor
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    // Retrieve all categories
    @Override
    public List<Category> getAllCategories() {
        List<Category> CategoryList = new ArrayList<>();

        // SQL query to retrieve all categories
        String query = "SELECT * FROM categories";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Execute the SQL query and retrieve the result set
            try(ResultSet set = preparedStatement.executeQuery()) {

                // Iterate through each row in the result set
                while(set.next()) {

                    // Create a Category object with data from the current row
                    Category category = new Category(
                            set.getInt("category_id"),
                            set.getString("name"),
                            set.getString("description")
                    );

                    // Add the category to the list
                    CategoryList.add(category);
                }
            }
        }
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        // Return the list of categories
        return CategoryList;
    }

    // Retrieve a category by its ID
    @Override
    public Category getById(int categoryId) {
        String query = "SELECT * FROM categories WHERE category_id = ?";
        Category category = new Category();

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the category_id parameter in the prepared statement
            preparedStatement.setInt(1, categoryId);

            // Execute the SQL query and retrieve the result set
            try(ResultSet set = preparedStatement.executeQuery()) {

                // If a category is found, create a Category object with the data from the result set
                if(set.next()) {
                    category = new Category(
                            set.getInt("category_id"),
                            set.getString("name"),
                            set.getString("description")
                    );
                }
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        // Return the category
        return category;
    }

    // Create a new category
    @Override
    public Category create(Category category) {
        String query = "INSERT INTO categories(name, description) VALUES (?, ?);";

        try(Connection connection = getConnection()) {

            // Create a prepared statement for the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // Set the values for the parameters in the prepared statement
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            // Execute the SQL query and retrieve the number of affected rows
            int rowsAffected = preparedStatement.executeUpdate();

            // If insertion is successful, retrieve the auto-generated ID and return the newly inserted category
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    return getById(orderId);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Return null if category creation fails
        return null;
    }

    // Update a category
    @Override
    public void update(int categoryId, Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?;";

        try (Connection connection = getConnection()) {

            // Create a prepared statement for the SQL query
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the values for the parameters in the prepared statement
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            // Execute the update query
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete a category by its ID
    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?;";

        try (Connection connection = getConnection()) {

            // Create a prepared statement for the SQL query
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the value for the parameter in the prepared statement
            statement.setInt(1, categoryId);

            // Execute the delete query
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Map a row from the result set to a Category object
    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        // Create a new Category object with the retrieved data
        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        // Return the Category object
        return category;
    }

}
