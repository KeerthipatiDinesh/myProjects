package com.Inventory_Management_System.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.Inventory_Management_System.exception.InvalidStockException;
import com.Inventory_Management_System.exception.ProductNotFoundException;
import com.Inventory_Management_System.model.Product;
import com.Inventory_Management_System.repository.ProductRepository;

@Service
@CacheConfig(cacheNames = "products")
public class InventoryService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@CachePut(key = "#product.id")
	public void addProduct(Product product) {
		validateProduct(product);
		productRepository.save(product);
	}

	@Cacheable(key = "#id")
	public Product getProduct(int id) {
		System.out.println("Fetching product from database for id: " + id);
		Product product = productRepository.findById(id);
		if (product == null) {
			throw new ProductNotFoundException("Product not found with ID: " + id);
		}
		return product;
	}

	@Cacheable(cacheNames = "allProducts", key = "'allProducts'")
	public List<Product> getAllProducts() {
		System.out.println("Fetching products from the database...");

		return productRepository.findAll();
	}

	@CacheEvict(cacheNames = "allProducts", key = "'allProducts'")
	@CachePut(key = "#product.id")
	public void updateProduct(Product product) {
		validateProduct(product);
		if (productRepository.findById(product.getId()) == null) {
			throw new ProductNotFoundException("Product not found with ID: " + product.getId());
		}
		productRepository.update(product);
		System.out.println("Product with ID " + product.getId() + " updated in cache.");

	}

	@CacheEvict(cacheNames = "allProducts", key = "'SimpleKey []'")
	public void deleteProduct(int id) {
		if (productRepository.findById(id) == null) {
			throw new ProductNotFoundException("Product not found with ID: " + id);
		}
		productRepository.delete(id);
		System.out.println("Product with ID " + id + " removed from cache and database.");

	}

	@Cacheable(cacheNames = "lowStockProducts", key = "#threshold")
	public List<Product> getLowStockProducts(double threshold) {
		return productRepository.findLowStockProducts(threshold);
	}

	private void validateProduct(Product product) {
		if (product.getStock() < 0) {
			throw new InvalidStockException("Stock cannot be negative");
		}
		if (product.getPrice() <= 0) {
			throw new IllegalArgumentException("Price must be greater than zero");
		}
		if (product.getMinimumStock() < 0) {
			throw new IllegalArgumentException("Minimum stock cannot be negative");
		}
	}
}
