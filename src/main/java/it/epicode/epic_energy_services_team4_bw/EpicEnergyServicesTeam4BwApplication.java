package it.epicode.epic_energy_services_team4_bw;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EpicEnergyServicesTeam4BwApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpicEnergyServicesTeam4BwApplication.class, args);
	}
	@PostConstruct
	public void avvioMessaggio() {
		System.out.println("""
            ┌───────────────────────────────────────────────────────────────┐
            │ 🤖 EPIC ENERGY SERVICES™                                      │
            │                                                               │
            │ 💡 ENERGIA PER TUTTI... TRANNE PER CHI AMA I TESTING E IL SASS│
            │                                                               │
            │ 📝 AVVIA LA REGISTRAZIONE QUI:                                │
            │     http://localhost:8080/register.html                       │
            │                                                               │
            │ 🔐 AVVIA IL LOGIN QUI:                                        │
            │     http://localhost:8080/login.html                          │
            │                                                               │
            │ ☕ OFFRIMI UN CAFFÈ (O UNA FATTURA PAGATA):                    │
            │     https://www.paypal.com/paypalme/lucaf95                   │
            └───────────────────────────────────────────────────────────────┘
            """);
	}

}
