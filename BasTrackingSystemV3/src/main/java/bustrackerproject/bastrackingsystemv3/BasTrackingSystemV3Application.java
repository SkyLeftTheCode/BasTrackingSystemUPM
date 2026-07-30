package bustrackerproject.bastrackingsystemv3;

import bustrackerproject.bastrackingsystemv3.model.Bus;
import bustrackerproject.bastrackingsystemv3.model.Driver;
import bustrackerproject.bastrackingsystemv3.model.Student;
import bustrackerproject.bastrackingsystemv3.repository.BusRepository;
import bustrackerproject.bastrackingsystemv3.repository.DriverRepository;
import bustrackerproject.bastrackingsystemv3.repository.StudentRepository;
import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;

@SpringBootApplication
public class BasTrackingSystemV3Application {

    @Value("${ngrok.authtoken}")
    private String ngrokAuthtoken;

    public static void main(String[] args) {
        SpringApplication.run(BasTrackingSystemV3Application.class, args);
    }

    @Bean
    CommandLineRunner initDatabaseAndNgrok(StudentRepository studentRepo, DriverRepository driverRepo, BusRepository busRepo) {
        return args -> {
            // 1. DATA SEEDING: 31 Authorized Matric Numbers (Reset & Populate)
            System.out.println("Executing automated entity seeding for Authorized Students...");

            // Clear existing records to prevent Duplicate Key Constraint Violations upon restart
            studentRepo.deleteAll();

            String[] authorizedMatrics = {
                    "226246", "226258", "226621", "226661", "226701",
                    "226816", "226972", "226975", "227205", "227210",
                    "228063", "228142", "228161", "228396", "228486",
                    "228828", "228833", "229065", "229193", "229194",
                    "229293", "229295", "229579", "230288", "230349",
                    "230744", "230828", "230997", "231361", "231798",
                    "232065"
            };

            for (String matric : authorizedMatrics) {
                Student s = new Student();
                // Enforce string trimming to safely eliminate any hidden trailing whitespaces
                s.setMatricNo(matric.trim());
                s.setPassword("123"); // Default unified credential password string
                s.setName("Student " + matric.trim()); // Dynamic structural identifier instantiation
                studentRepo.save(s);
            }
            System.out.println("✅ Successfully seeded " + authorizedMatrics.length + " Authorized Student entities into SQLite.");

            // 2. DATA SEEDING: 10 Operational Driver Nodes
            if (driverRepo.count() == 0) {
                String[] driverNames = {
                        "Pak Cik Abu", "Abang Ali", "Encik Osman", "Pak Cik Hassan", "Abang Zaidi",
                        "Encik Rahman", "Pak Cik Idris", "Abang Syafiq", "Encik Kamaruddin", "Pak Cik Johari"
                };

                for (int i = 0; i < 10; i++) {
                    Driver d = new Driver();
                    d.setDriverId(String.format("D%03d", i + 1));
                    d.setPassword("123");
                    d.setName(driverNames[i]);
                    d.setAge(40 + i);
                    d.setSex("Male");
                    driverRepo.save(d);
                }
                System.out.println("✅ 10 Drivers successfully seeded!");
            }

            // 3. DATA SEEDING: 10 Core Transit Bus Assets
            if (busRepo.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Bus b = new Bus();
                    b.setPlateNo("UPM " + i);
                    b.setStatus("AVAILABLE");
                    b.setCurrentLocation("Depot");
                    b.setEtaNextStop(0);
                    busRepo.save(b);
                }
                System.out.println("✅ 10 UPM Buses successfully seeded!");
            }

            // 4. AUTOMATIC NGROK WAN TUNNEL INITIALIZATION
            try {
                if (ngrokAuthtoken == null || ngrokAuthtoken.isEmpty()) {
                    System.err.println("\n[NGROK ERROR] Missing credentials. Please provide a valid ngrok authtoken in application.properties!\n");
                } else {
                    System.out.println("\n⏳ Instantiating secure public proxy tunnel connection, please wait...");

                    final JavaNgrokConfig javaNgrokConfig = new JavaNgrokConfig.Builder()
                            .withAuthToken(ngrokAuthtoken)
                            .withNgrokPath(Paths.get("C:\\ngrok\\ngrok.exe"))
                            .build();

                    final NgrokClient ngrokClient = new NgrokClient.Builder()
                            .withJavaNgrokConfig(javaNgrokConfig)
                            .build();

                    final CreateTunnel createTunnel = new CreateTunnel.Builder()
                            .withAddr(8080)
                            .build();

                    final Tunnel tunnel = ngrokClient.connect(createTunnel);

                    System.out.println("\n==========================================================================");
                    System.out.println("🚀 LIVE TRACKING SYSTEM IS ONLINE FOR MOBILE DEVICES!");
                    System.out.println("🔗 Forwarding global endpoint connection to: " + tunnel.getPublicUrl());
                    System.out.println("==========================================================================\n");
                }
            } catch (Exception e) {
                System.err.println("\n❌ [NGROK ERROR] Failed to instantiate public proxy server boundary: " + e.getMessage() + "\n");
            }
        };
    }
}