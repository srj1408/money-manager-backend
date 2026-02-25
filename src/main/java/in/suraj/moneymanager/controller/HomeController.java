package in.suraj.moneymanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static in.suraj.moneymanager.constants.UrlConstants.*;

@RestController
@RequestMapping({STATUS, HEALTH})
public class HomeController {

    @GetMapping
    public String healthCheck(){
        return "Application is running";
    }
}
