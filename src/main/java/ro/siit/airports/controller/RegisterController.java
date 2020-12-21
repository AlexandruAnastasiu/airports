package ro.siit.airports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.airports.domain.User;
import ro.siit.airports.domain.UserDto;
import ro.siit.airports.repository.UserRepository;
import ro.siit.airports.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public ModelAndView displayRegisterPage() {
        final ModelAndView model = new ModelAndView("register");
        model.addObject("newUser", new UserDto());
        return model;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("newUser") final UserDto userDto, final BindingResult result) {
        final ModelAndView model = new ModelAndView("register");

        if (result.hasErrors()) {
            return model;
        }

        User registered = userService.registerNewUserAccount(userDto);
        return new ModelAndView("login");
    }

}
