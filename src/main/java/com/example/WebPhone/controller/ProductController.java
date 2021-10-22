package com.example.WebPhone.controller;

import com.example.WebPhone.dto.ProductDTO;
import com.example.WebPhone.entity.Product;
import com.example.WebPhone.service.IProductService;
import com.example.WebPhone.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ProductController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img";
    @Autowired
    IProductService IProductService;

    @GetMapping(value = {"/", "/product"})
    public String Products(Model model){
        List<Product> listProduct = IProductService.findAll();
        model.addAttribute("list", listProduct);
        model.addAttribute("product", new Product());
        model.addAttribute("productDTO", new ProductDTO());
        return "index";
    }

    @PostMapping("/product")
    public String saveProduct(@RequestParam("productImage")MultipartFile file,
                              @ModelAttribute("productDTO")ProductDTO productDTO,
                              @RequestParam("image")String image,
                              @RequestParam("name")String name,
                              @RequestParam("description")String description,
                              @RequestParam("price")float price,
                              @RequestParam("quantity")int quantiy) throws IOException {
        Product product = new Product();
        String imageUUID;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = image;
        }
        product.setImage(imageUUID);
        product.setName(name);
        product.setQuantity(quantiy);
        product.setPrice(price);
        product.setDescription(description);
        IProductService.save(product);
        return "redirect:/";
    }
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable int id){
        IProductService.remove(id);
        return "redirect:/";
    }
    @GetMapping("/product/update/{id}")
    public String updateProduct(@PathVariable int id, Model model){
        Product product = IProductService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        model.addAttribute("productDTO", productDTO);
        return "redirect:/";
    }

}
