package me.pixka.update.t;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import me.pixka.update.s.UpdateService;

@Component
public class UpdateTask implements CommandLineRunner {

	@Autowired
	private UpdateService update;

	@Override
	public void run(String... arg0) throws Exception {

		update.run();
	}

}
