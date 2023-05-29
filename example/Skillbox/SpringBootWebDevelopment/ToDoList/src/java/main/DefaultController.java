package main;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DefaultController {

    @RequestMapping("/")
    public String index(Model model) {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyyг. HHч.mmмин"));
    }
}
