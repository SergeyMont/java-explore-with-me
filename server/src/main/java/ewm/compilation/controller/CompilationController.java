package ewm.compilation.controller;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.CompilationShortDto;
import ewm.compilation.service.CompilationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilation(@RequestParam boolean pinned,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return compilationService.getAllCompilation(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable int compId) {
        return compilationService.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    public CompilationShortDto addNewCompilation(@RequestBody CompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable int compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventCompilation(@PathVariable int compId,
                                              @PathVariable int eventId) {
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDto deleteEventCompilation(@PathVariable int compId,
                                                 @PathVariable int eventId) {
        return compilationService.deleteEventToCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public CompilationDto pinCompilation(@PathVariable int compId) {
        return compilationService.updateCompilationPin(compId, true);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public CompilationDto deletePinCompilation(@PathVariable int compId) {
        return compilationService.updateCompilationPin(compId, false);
    }
}
