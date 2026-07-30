package bustrackerproject.bastrackingsystemv3.controller;

import bustrackerproject.bastrackingsystemv3.model.Driver;
import bustrackerproject.bastrackingsystemv3.model.Student;
import bustrackerproject.bastrackingsystemv3.repository.DriverRepository;
import bustrackerproject.bastrackingsystemv3.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*") // CORS-safe boundary logic
public class AuthController {

    @Autowired private StudentRepository studentRepo;
    @Autowired private DriverRepository driverRepo;

    // 1. Render primary login interface
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    // 2. Control Authorization, Session Seeding & Remote Input Sanitization
    @PostMapping("/login")
    public String handleLogin(@RequestParam String id, @RequestParam String password,
                              @RequestParam String role, HttpSession session, Model model) {

        // KRITIKAL: Sanitize input dengan membuang sebarang space kosong di depan atau belakang string
        String cleanId = (id != null) ? id.trim() : "";
        String cleanPassword = (password != null) ? password.trim() : "";

        if ("STUDENT".equals(role)) {
            // Gunakan 'cleanId' dan 'cleanPassword' yang sudah dibersihkan daripada trailing spaces
            Student student = studentRepo.findByMatricNoAndPassword(cleanId, cleanPassword);
            if (student != null) {
                session.setAttribute("user", student);
                return "redirect:/student";
            }
        } else if ("DRIVER".equals(role)) {
            // Proses pembersihan yang sama untuk Node Pemandu
            Driver driver = driverRepo.findByDriverIdAndPassword(cleanId, cleanPassword);
            if (driver != null) {
                session.setAttribute("user", driver);
                return "redirect:/driver";
            }
        }

        model.addAttribute("error", "Invalid ID, Password, or Role!");
        return "login";
    }

    // 3. Clear authorization token/session context boundary
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}