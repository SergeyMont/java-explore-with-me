package ewm.compilation.service;

import ewm.compilation.Compilation;
import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.CompilationShortDto;
import ewm.compilation.dto.Eve;
import ewm.compilation.repository.CompilationRepository;
import ewm.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final ModelMapper modelMapper;

    public List<CompilationDto> getAllCompilation(boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return compilationRepository.findAllByPinned(pinned, pageable)
                .stream()
                .map(this::toCompilationDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(int id) {
        return toCompilationDto(compilationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Подборка не найдена")));
    }

    @Transactional
    public CompilationShortDto createCompilation(CompilationDto compilationDto) {
        Compilation compilation = compilationRepository.save(toCompilation(compilationDto));
        CompilationShortDto result = modelMapper.map(compilation, CompilationShortDto.class);
        List<Integer> list = compilation.getEvents();
        List<Eve> comp = new ArrayList<>();
        for (int i : list) {
            comp.add(new Eve(i));
        }
        result.setEvents(comp);
        return result;
    }

    @Transactional
    public CompilationDto updateCompilation(CompilationDto compilationDto) {
        return toCompilationDto(compilationRepository.save(toCompilation(compilationDto)));
    }

    @Transactional
    public void deleteCompilation(int id) {
        compilationRepository.deleteById(id);
    }

    @Transactional
    public CompilationDto addEventToCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборка не найдена"));
        assert compilation != null;
        CompilationDto compilationDto = toCompilationDto(compilation);
        List<Integer> list = compilationDto.getEvents();
        list.add(eventId);
        compilationDto.setEvents(list);
        return toCompilationDto(compilationRepository.save(toCompilation(compilationDto)));
    }

    @Transactional
    public CompilationDto deleteEventToCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборка не найдена"));
        assert compilation != null;
        CompilationDto compilationDto = toCompilationDto(compilation);
        List<Integer> list = compilationDto.getEvents();
        list.remove((Integer) eventId);
        compilationDto.setEvents(list);
        return toCompilationDto(compilationRepository.save(toCompilation(compilationDto)));
    }

    @Transactional
    public CompilationDto updateCompilationPin(int compId, boolean pin) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборка не найдена"));
        assert compilation != null;
        compilation.setPinned(pin);
        return toCompilationDto(compilationRepository.save(compilation));
    }


    private CompilationDto toCompilationDto(Compilation compilation) {
        return modelMapper.map(compilation, CompilationDto.class);
    }

    private Compilation toCompilation(CompilationDto compilationDto) {
        return modelMapper.map(compilationDto, Compilation.class);
    }
}
