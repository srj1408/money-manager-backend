package in.suraj.moneymanager.controller;

import in.suraj.moneymanager.dto.CategoryDto;
import in.suraj.moneymanager.exception.ResourceNotFoundException;
import in.suraj.moneymanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static in.suraj.moneymanager.constants.UrlConstants.CATEGORY;

@RestController
@RequestMapping(CATEGORY)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(){
        List<CategoryDto> categories = categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDto>> getCategoriesByTypeForCurrentUser(@PathVariable String type){
        List<CategoryDto> categories = categoryService.getCategoriesByTypeForCurrentUser(type);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) throws ResourceNotFoundException {
        CategoryDto categoryDto = categoryService.updateCategory(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }
}
