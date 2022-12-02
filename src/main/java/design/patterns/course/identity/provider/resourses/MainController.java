package design.patterns.course.identity.provider.resourses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(path="/demo")

public class MainController {

    @Autowired
    private userRepository userRepository;
    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String email , @RequestParam String info, @RequestParam String password) {
        UserInfo newUser= new UserInfo();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setInfo(info);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return "Created";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }

}
