package ewm.category.service;

import ewm.category.Category;
import ewm.category.dto.CategoryDto;
import ewm.category.repository.CategoryRepository;
import ewm.exceptions.ObjectNotFoundException;
import ewm.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<CategoryDto> getAllCategory(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable).stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(int id) {
        return toCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Категория не найдена")));
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Transactional
    public void deleteCategoryById(int id) {
        if (categoryRepository.existsById(id)) categoryRepository.deleteById(id);
        else throw new ObjectNotFoundException("Категория не найдена");
    }

    private Category toCategory(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto toCategoryDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    private void validateUser(Integer userId) {
        if (!userService.isUserCreated(userId)) {
            throw new ObjectNotFoundException("User is not created");
        }
    }
}
