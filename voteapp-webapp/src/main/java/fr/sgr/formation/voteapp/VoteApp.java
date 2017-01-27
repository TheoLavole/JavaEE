package fr.sgr.formation.voteapp;

import javax.servlet.Servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.sgr.formation.voteapp.servlet.AdditionnerServlet;

@SpringBootApplication
public class VoteApp {
	@Bean(name = "additionner")
	public Servlet additionnerServlet() {
		return new AdditionnerServlet();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(VoteApp.class, args);
	}
}
