package de.htwg.in.schneider.saitenweise.backend.controller;

import org.springframework.web.bind.annotation.*;

import de.htwg.in.schneider.saitenweise.backend.model.Category;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @GetMapping()
    public List<Category> getCategories() {
        return Arrays.asList(Category.values());
    }
}