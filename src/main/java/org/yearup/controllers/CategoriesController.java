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

    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    // Constructor injection of CategoryDao and ProductDao
    public CategoriesController(CategoryDao CategoryDao , ProductDao ProductDao){
        this.categoryDao = CategoryDao;
        this.productDao = ProductDao;
    }

    @GetMapping("")
    // Handle HTTP GET request to retrieve all categories
    public List<Category> getAll() {
        return this.categoryDao.getAllCategories();
    }

    @GetMapping("{id}")
    // Handle HTTP GET request to retrieve a category by its ID
    public Category getById(@PathVariable int id) {
        return this.categoryDao.getById(id);
    }

    // Handle HTTP GET request to retrieve all products in a specific category by category ID
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return this.productDao.listByCategoryId(categoryId);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // Handle HTTP POST request to add a new category
    public Category addCategory(@RequestBody Category category) {
        try {
            return categoryDao.create(category);
        } catch (Exception ex) {
            // Handle any exceptions and return an appropriate HTTP response status
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // Handle HTTP PUT request to update a category by its ID
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        try {
            categoryDao.update(id, category);
        } catch (Exception ex) {
            // Handle any exceptions and return an appropriate HTTP response status
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // Handle HTTP DELETE request to delete a category by its ID
    public void deleteCategory(@PathVariable int id) {
        try {
            var product = categoryDao.getById(id);

            if (product == null)
                // If the category is not found, return an appropriate HTTP response status
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            categoryDao.delete(id);
        } catch (Exception ex) {
            // Handle any exceptions and return an appropriate HTTP response status
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
