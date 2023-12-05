package com.tudorgiu.springboot.TicketShopApplication;

import com.tudorgiu.springboot.TicketShopApplication.dao.UserDAO;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class TicketShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketShopApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserDAO userDAO){

		return runner -> {
			queryForUsersByLastName(userDAO);
		};
	}

	private void queryForUsersByLastName(UserDAO userDAO) {
		List<User> userList = userDAO.findByLastName("Ratiu");

		for(User user : userList)
			System.out.println(user.toString());
	}

	private void queryForUsers(UserDAO userDAO) {
		for (User user:
			 userDAO.findAll()) {
			System.out.println(user);
		}
	}


	private void readUser(UserDAO userDAO) {
		System.out.println(userDAO.findById(1).toString());
	}

	private void createUser(UserDAO userDAO) {
		System.out.println("Creating new user");
		User tempUser = new User("Mihai", "Ratiu", "ratiu@gmail.ro", 21, "parola", false);

		System.out.println("Saving the user");
		userDAO.save(tempUser);

		System.out.println("Saved user. Generated id: " + tempUser.getId());
	}
}


