package ewm.category.controller;

import ewm.category.dto.CategoryDto;
import ewm.category.service.CategoryService;
import ewm.exceptions.ObjectNotFoundException;
import ewm.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

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
    public CategoryDto createCategory(@RequestHeader("X-Explorer-User-Id") Integer userId,
                                      @RequestBody CategoryDto categoryDto) {
        validateUser(userId);
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@RequestHeader("X-Explorer-User-Id") Integer userId,
                                      @RequestBody CategoryDto categoryDto) {
        validateUser(userId);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@RequestHeader("X-Explorer-User-Id") Integer userId,
                               @PathVariable Integer catId) {
        validateUser(userId);
        categoryService.deleteCategoryById(catId);
    }

    private void validateUser(Integer userId) {
        if (!userService.isUserCreated(userId)) {
            throw new ObjectNotFoundException("User is not created");
        }
    }
}
