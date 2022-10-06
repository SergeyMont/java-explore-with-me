package ewm.category.controller;

import ewm.category.dto.CategoryDto;
import ewm.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategory(@RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAllCategory(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        return categoryService.getCategoryById(catId);
    }

    @PostMapping("/admin/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategoryById(catId);
    }
}
