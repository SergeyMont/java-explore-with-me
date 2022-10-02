package ewm.user.controller;

import ewm.exceptions.ObjectNotFoundException;
import ewm.user.dto.UserDto;
import ewm.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestHeader("X-Explorer-User-Id") Integer userId,
                                  @RequestParam List<Integer> array,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        validateUser(userId);
        return userService.searchByIds(array, from, size);

    }

    @PostMapping
    public UserDto crateUser(@RequestHeader("X-Explorer-User-Id") Integer userId,
                             @RequestBody UserDto userDto) {
        validateUser(userId);
        return userService.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@RequestHeader("X-Explorer-User-Id") Integer id,
                           @PathVariable Integer userId) {
        validateUser(id);
        userService.deleteUser(userId);
    }

    private void validateUser(Integer userId) {
        if (!userService.isUserCreated(userId)) {
            throw new ObjectNotFoundException("User is not created");
        }
    }
}
