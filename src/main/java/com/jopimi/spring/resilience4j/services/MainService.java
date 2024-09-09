package com.jopimi.spring.resilience4j.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class MainService {

  private final Environment environment;

  private final AtomicInteger counter = new AtomicInteger(0);

  @Retry(name = "example-retry", fallbackMethod = "fallbackMethod")
  public String fetchWithRetry() {
    return WebClient
            .create("http://localhost:3000")
            .get()
            .uri("v1/sections")
            .retrieve()
            .bodyToMono(Object.class)
            .block()
            .toString();
  }

  @CircuitBreaker(name = "circuit-breaker-count", fallbackMethod = "fallback")
  public String fetchWithCircuitBreaker() {
    System.out.println("fetchWithCircuitBreaker " + counter.getAndIncrement());
    throw new NumberFormatException();
  }

  private String fallback(IllegalArgumentException e) {
    return "El servicio no está disponible en este momento. Por favor, inténtelo más tarde.";
  }

  public String fallbackMethod(Throwable t) {
    return "El servicio no está disponible en este momento. Por favor, inténtelo más tarde.";
  }

}
