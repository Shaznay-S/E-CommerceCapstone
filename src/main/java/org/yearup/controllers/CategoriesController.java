package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
// Specify the URL path for this controller
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController {

    // Controller class responsible for handling HTTP requests related to categories
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        // Constructor injection of CategoryDao and ProductDao
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping("")
    public List<Category> getAll() {
        // Retrieve all categories from the database
        return this.categoryDao.getAllCategories();
    }

    @GetMapping("{id}")
    public Category getById(@PathVariable int id) {
        // Retrieve a specific category by its ID
        return this.categoryDao.getById(id);
    }

    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        // Retrieve all products in a specific category by category ID
        return this.productDao.listByCategoryId(categoryId);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        // Add a new category to the database
        try {
            return categoryDao.create(category);
        } catch (Exception ex) {
            // Handle exceptions and return appropriate HTTP response status
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR");
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        // Update a category by its ID
        try {
            categoryDao.update(id, category);
        } catch (Exception ex) {
            // Handle exceptions and return appropriate HTTP response status
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int id) {
        // Delete a category by its ID
        try {
            var product = categoryDao.getById(id);

            if (product == null)
                // Return appropriate HTTP response status if category is not found
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            categoryDao.delete(id);
        } catch (Exception ex) {
            // Handle exceptions and return appropriate HTTP response status
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR");
        }
    }

}