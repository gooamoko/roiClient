package ru.gooamoko.roiClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.gooamoko.roiClient.service.RoiService;

@EnableFeignClients
@SpringBootApplication
public class RoiClientApplication implements CommandLineRunner {
	private final static Logger log = LoggerFactory.getLogger(RoiClientApplication.class);

	private final RoiService roiService;

	public RoiClientApplication(RoiService roiService) {
		this.roiService = roiService;
	}

	public static void main(String[] args) {
		SpringApplication.run(RoiClientApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Получаем список инициатив на голосовании.");
		roiService.processPoll();
	}
}
