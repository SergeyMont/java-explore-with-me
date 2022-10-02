package ewm.category.dto;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class CategoryDto {
    private int id;
    @UniqueElements
    private String name;
}
