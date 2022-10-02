package ewm.compilation.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {
    private int id;
    private boolean pinned;
    private String title;
    private List<Integer> events;
}
