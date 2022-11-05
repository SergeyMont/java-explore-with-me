package ewm.compilation.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewCompilationDto {
    private boolean pinned;
    @NotBlank
    private String title;
    private List<Integer> events;
}
