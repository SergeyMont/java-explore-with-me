package ewm.compilation.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompilationShortDto {
    private int id;
    private boolean pinned;
    private String title;
    private List<Eve> events;
}
