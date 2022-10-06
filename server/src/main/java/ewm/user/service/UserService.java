package ewm.user.service;

import ewm.user.User;
import ewm.user.dto.UserDto;
import ewm.user.dto.UserShortDto;
import ewm.user.repository.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<UserDto> searchByIds(List<Integer> list, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return userRepository.findAllByIdIn(list, pageable).stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    public boolean isUserCreated(int id) {
        return userRepository.existsById(id);
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    private User toUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = modelMapper.map(user, UserShortDto.class);
        return userShortDto;
    }
}
