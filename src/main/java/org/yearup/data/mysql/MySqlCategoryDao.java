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
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        //Create an empty list to store the categories
        List<Category> categories = new ArrayList<>();

        //Try to establish a connection and execute the SQL query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM categories");
             ResultSet resultSet = statement.executeQuery()) {

            //Iterate through the result set
            while (resultSet.next()) {
                //Create a Category object by mapping the current row of the result set
                Category category = mapRow(resultSet);

                //Add the category to the list
                categories.add(category);
            }
        } catch (SQLException e) {
            //Handle any exceptions that occur during the database operations
            //Print the stack trace for debugging purposes
            e.printStackTrace();
        }

        //Return the list of categories
        return categories;
    }
    @Override
    public Category getById(int categoryId) {
        //Try to establish a connection and execute the SQL query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM categories WHERE category_id = ?")) {

            //Set the category_id parameter in the prepared statement
            statement.setInt(1, categoryId);

            //Execute the query and obtain the result set
            try (ResultSet resultSet = statement.executeQuery()) {
                //Check if a category is found
                if (resultSet.next()) {
                    //Create a Category object by mapping the current row of the result set
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            //Handle any exceptions that occur during the database operations
            //Print the stack trace for debugging purposes
            e.printStackTrace();
        }

        //If no category is found or an exception occurs, return null
        return null;
    }


    @Override
    public Category create(Category category) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO categories (name, description) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                // Insertion failed
                // Handle the exception or notify accordingly
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int categoryId = generatedKeys.getInt(1);
                    category.setCategoryId(categoryId);
                    return category;
                }
            }
        } catch (SQLException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
