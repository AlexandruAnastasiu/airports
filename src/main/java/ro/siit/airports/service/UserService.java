package ro.siit.airports.service;

import org.springframework.data.domain.Page;
import ro.siit.airports.domain.User;
import ro.siit.airports.domain.UserDto;

public interface UserService {

    Page<User> listAllUsers(int pageNumber, String sortField, String sortDir, String keyword);
    User insertIntoDatabase(User myUser);

    User registerNewUserAccount(UserDto userDto);
}
