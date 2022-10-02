package ewm.category.service;

import ewm.category.Category;
import ewm.category.dto.CategoryDto;
import ewm.category.repository.CategoryRepository;
import ewm.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
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

    public CategoryDto createCategory(CategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    public void deleteCategoryById(int id) {
        try {
            categoryRepository.deleteById(id);
        } catch (NullPointerException e) {
            throw new ObjectNotFoundException("Категория не найдена");
        }
    }

    private Category toCategory(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto toCategoryDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
