package com.Inventory_Management_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory_Management_System.model.Product;
import com.Inventory_Management_System.service.InventoryService;

@RestController
@RequestMapping("/admin")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@PostMapping("/addproducts")
	public ResponseEntity<Void> addProduct(@RequestBody Product product) {
		inventoryService.addProduct(product);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id) {
		return ResponseEntity.ok(inventoryService.getProduct(id));
	}

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(inventoryService.getAllProducts());
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Void> updateProduct(@PathVariable int id, @RequestBody Product product) {
		product.setId(id);
		inventoryService.updateProduct(product);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
		inventoryService.deleteProduct(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/products/low-stock")
	public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "0") double threshold) {
		return ResponseEntity.ok(inventoryService.getLowStockProducts(threshold));
	}
}
