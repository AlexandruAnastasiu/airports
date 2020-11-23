package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.siit.airports.domain.User;
import ro.siit.airports.repository.UserRepository;
import ro.siit.airports.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
public class AdministrationUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/admin/settings/users")
    public String displayUsersSettingsPage(Model model) {
        String keyword = null;
        return listByPage(model, 1, "email", "asc", keyword);
    }

    @GetMapping("/admin/settings/users/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") final String sortField,
                             @Param("sortDir") final String sortDir,
                             @Param("keyword") final String keyword) {

        Page<User> page = userService.listAllUsers(currentPage, sortField, sortDir, keyword);

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<User> users = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", users);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "users-settings";
    }

    @GetMapping("/admin/settings/users/editRole/{userId}")
    public String displayEditUserPage(final Model model, @PathVariable("userId") final Long userId) {
        final Optional<User> optUser = userRepository.findById(userId);
        User user = optUser.get();
        model.addAttribute("myUser", user);
        return "edit-user-role";
    }

    @PostMapping("/admin/settings/airports/editRole/result")
    public String displayEditUserResult(final Model model, @ModelAttribute final User myUser) {
        User editedUser = userService.insertIntoDatabase(myUser);
        Optional<User> optAirport = Optional.of(editedUser);
        model.addAttribute("userEmail", optAirport.map(a -> a.getEmail()).orElse("No email"));
        model.addAttribute("userFirstName", optAirport.map(a -> a.getFirstName()).orElse("No First Name"));
        model.addAttribute("userLastName", optAirport.map(a -> a.getLastName()).orElse("No Last Name"));
        model.addAttribute("userRole", optAirport.map(a -> a.getRole()).orElse("No Role"));
        return "edit-user-role-successful";
    }

    @GetMapping("/admin/settings/users/delete/{userId}")
    public String deleteUser(final Model model, @PathVariable("userId") final Long userId) {
        final User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID:" + userId));
        model.addAttribute("userName", userToDelete.getFirstName() + " " + userToDelete.getLastName());
        userRepository.delete(userToDelete);
        return "delete-user-successful";
    }
}
