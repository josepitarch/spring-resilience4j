package com.jopimi.spring.resilience4j;

import com.jopimi.spring.resilience4j.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RestController
  public static class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
      this.mainService = mainService;
    }

    @GetMapping("/")
    public String fetch() {
      return mainService.fetchWithCircuitBreaker();
    }

  }

}

