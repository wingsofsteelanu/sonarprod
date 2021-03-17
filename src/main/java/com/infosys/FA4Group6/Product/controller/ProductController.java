package com.infosys.FA4Group6.Product.controller;


//import java.util.ArrayList;
import java.util.List;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.FA4Group6.Product.dto.ProductDTO;
import com.infosys.FA4Group6.Product.dto.SubscriptionDTO;
import com.infosys.FA4Group6.Product.entity.Product;
import com.infosys.FA4Group6.Product.model.Message;
//import com.infosys.FA4Group6.Product.model.ProductId;
import com.infosys.FA4Group6.Product.service.ProductService;
import com.infosys.FA4Group6.Product.service.SubscriptionService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
 
    @Autowired
	Environment environment;
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public Object addProdcuct(@RequestBody ProductDTO product) {
        LOGGER.info("add product");
        return productService.saveProduct(product.convertToEntity());
    }


// Retriveing all Products      (WORKING)
    @GetMapping()
    public Object getProducts() {
        LOGGER.info("Retriveing all Products");

        List<Product> products = (List<Product>) productService.getProducts();

        return products;
    }

//    @PostMapping()
//    public Object getProductsByIds(@RequestBody ProductId productIds) {
//        LOGGER.info("Retriveing Selected Products");
//
//        List<Product> products = (List<Product>) productService.getProductsByIds(productIds);
//
//        return products;
//    }
    
 //FETCH PRODUCT BY SELLER ID

    @GetMapping("/seller/{sellerId}")
    public Object getProductsOfseller(@PathVariable(name = "sellerId") Integer sellerId) {
        LOGGER.info("Retriveing Products based on sellerId " + sellerId);

        List<Product> products = (List<Product>) productService.getProductsBySellerId(sellerId);
        return products;
    }
    
    
//FETCH PRODUCTS BY PRODID    (WORKING)
    @GetMapping("/{productId}")    
    public Object findProductById(@PathVariable(name = "productId") Integer productId) {
        LOGGER.info("Retriveing Products based on productId " + productId);
        return productService.getProductById(productId);
    }
  //Fetches products according to category  (WORKING)
  	@GetMapping(value= "/api/{category}/products")
  	public List<Product> getProductsByCategory(@PathVariable String category) {
  		LOGGER.info("Product details for Product name {}", category);
  		return productService.getProductsByCategory(category);
  	}
  	
  	//Fetches products according to product name    (WORKING)
  	@GetMapping(value = "/api/product/{productname}")
  	public List<Product>getProductsByName(@PathVariable String productname) {
  		LOGGER.info("Product details for product name {}",productname);
  		return productService.getProductsByName(productname);
  	}



  	//Deleting on basis of productid    (Working)
    @DeleteMapping("/remove/{productId}")
    public Object removeProduct(@PathVariable(name = "productId") @NonNull Integer productId) {
        try {
            productService.deleteProduct(productId);

            LOGGER.info("Removing Products based on productId " + productId);
            Message msg = new Message();
            msg.setMessage("Successfully Deleted");
            return msg;

        } catch (Exception e) {
            Message msg = new Message();
            msg.setMessage("Deletion Failed");
            return msg;
            }
        }
//    	LOGGER.info("Remove request for product with productid {}", productId);
//		ResponseEntity<String>response;
//		String successMessage = "Product removed successfully !!!!!!!";
//		String errorMessage = "No such product found";
//		if(productService.deleteProduct(productId)) {
//			response = new ResponseEntity<String>(successMessage, HttpStatus.OK);
//		}else {
//			response = new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
//		}
//		return response;
//
//    }
//UPDATING ALL ATTRIBUTES        (WORKING)
    @PutMapping("/update")
    public Object updateProduct(@RequestBody ProductDTO product) {
        Product productEntity = product.convertToEntity();
        LOGGER.info("Updating Product ", product.getProdId());
        return productService.updateProduct(productEntity);
    }

    // UPDATING STOCK AND CHECKING IF ITS LESS THEN 10    (WORKING)
    @PutMapping("/update/stock")
    public Object updateStock(@RequestBody ProductDTO product) {
        Product productEntity = product.convertToEntity();
        LOGGER.info("Updating Product Stock", product.getProdId());

        if (product.getStock() != null && product.getStock() < 10) {
            Message msg = new Message();
            msg.setMessage("The Stock should be at least 10");
            return msg;
        }

        return productService.updateStock(productEntity.getProdId(), product.getStock());
    }
    

}
