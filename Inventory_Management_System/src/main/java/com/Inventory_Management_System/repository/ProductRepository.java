package com.Inventory_Management_System.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.Inventory_Management_System.model.Product;

@Repository
public class ProductRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;



    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setProductname(rs.getString("productname"));
        product.setStock(rs.getInt("stock"));
        product.setPrice(rs.getDouble("price"));
        product.setMinimumStock(rs.getDouble("minimum_stock"));
        product.setStatus(rs.getString("status"));
        return product;
    };

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Product product) {
        String sql = "INSERT INTO products (productname, stock, price, minimum_stock, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getProductname(), product.getStock(), 
                          product.getPrice(), product.getMinimumStock(), product.getStatus());
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, productRowMapper, id);
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public void update(Product product) {
        String sql = "UPDATE products SET productname = ?, stock = ?, price = ?, minimum_stock = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getProductname(), product.getStock(), 
                          product.getPrice(), product.getMinimumStock(), product.getStatus(), product.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public List<Product> findLowStockProducts(double threshold) {
        String sql = "SELECT * FROM products WHERE stock <= minimum_stock OR stock <= ?";
        return jdbcTemplate.query(sql, productRowMapper, threshold);
    }
}
