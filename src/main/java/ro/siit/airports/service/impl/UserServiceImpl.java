package ro.siit.airports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.siit.airports.domain.Airport;
import ro.siit.airports.domain.User;
import ro.siit.airports.repository.UserRepository;
import ro.siit.airports.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> listAllUsers(final int pageNumber, final String sortField, final String sortDir, final String keyword) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sortDir.equals("asc") ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending());

        if(keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public User insertIntoDatabase(User myUser) {
        return userRepository.save(myUser);
    }
}
