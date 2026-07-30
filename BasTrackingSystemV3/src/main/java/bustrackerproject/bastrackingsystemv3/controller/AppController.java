package bustrackerproject.bastrackingsystemv3.controller;

import bustrackerproject.bastrackingsystemv3.model.Bus;
import bustrackerproject.bastrackingsystemv3.model.Driver;
import bustrackerproject.bastrackingsystemv3.model.Route;
import bustrackerproject.bastrackingsystemv3.repository.BusRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*") // Kalis CORS untuk laptop Zulfiqar
public class AppController {

    @Autowired private BusRepository busRepo;

    @GetMapping("/student")
    public String studentDashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/";

        model.addAttribute("routeABuses", busRepo.findByCurrentRouteId("A"));
        model.addAttribute("routeBBuses", busRepo.findByCurrentRouteId("B"));
        model.addAttribute("routeCBuses", busRepo.findByCurrentRouteId("C"));
        return "student";
    }

    @GetMapping("/driver")
    public String driverDashboard(HttpSession session, Model model) {
        Driver driver = (Driver) session.getAttribute("user");
        if (driver == null) return "redirect:/";

        Bus activeBus = null;
        List<Bus> allBuses = busRepo.findAll();
        for (Bus b : allBuses) {
            if (driver.getDriverId().equals(b.getCurrentDriverId()) && "OCCUPIED".equals(b.getStatus())) {
                activeBus = b;
                break;
            }
        }

        model.addAttribute("driver", driver);
        model.addAttribute("buses", allBuses);

        if (activeBus != null) {
            model.addAttribute("activeBus", activeBus);
            List<String> checkpoints = Route.getRouteCheckpoints(activeBus.getCurrentRouteId());
            int currentIndex = checkpoints.indexOf(activeBus.getCurrentLocation());
            String nextStop = (currentIndex != -1 && currentIndex < checkpoints.size() - 1) ? checkpoints.get(currentIndex + 1) : "End of Route";
            model.addAttribute("nextStop", nextStop);
            model.addAttribute("isInJourney", true);
        } else {
            model.addAttribute("isInJourney", false);
        }

        return "driver";
    }

    @PostMapping("/driver/start")
    public String startJourney(@RequestParam String plateNo, @RequestParam String routeId, HttpSession session) {
        Driver driver = (Driver) session.getAttribute("user");
        if (driver == null) return "redirect:/";

        Bus bus = busRepo.findById(plateNo).orElse(null);

        if (bus != null && "AVAILABLE".equals(bus.getStatus())) {
            List<String> checkpoints = Route.getRouteCheckpoints(routeId);
            bus.setStatus("OCCUPIED");
            bus.setCurrentDriverId(driver.getDriverId());
            bus.setCurrentRouteId(routeId);
            bus.setCurrentLocation(checkpoints.get(0));
            bus.setEtaNextStop(Route.getEtaToNextStop(checkpoints.get(1)));

            bus.setOnDuty(false);
            bus.setTrafficStatus("NORMAL");
            bus.setPreviousLocation(checkpoints.get(0));
            bus.setPreviousEta(bus.getEtaNextStop());
            busRepo.save(bus);
        }
        return "redirect:/driver";
    }

    @PostMapping("/driver/toggle-duty")
    public String toggleDuty(@RequestParam String plateNo) {
        Bus bus = busRepo.findById(plateNo).orElse(null);
        if (bus != null) {
            bus.setOnDuty(!bus.isOnDuty());
            busRepo.save(bus);
        }
        return "redirect:/driver";
    }

    @PostMapping("/driver/report-jam")
    public String reportJam(@RequestParam String plateNo, @RequestParam String status) {
        Bus bus = busRepo.findById(plateNo).orElse(null);
        if (bus != null) {
            bus.setTrafficStatus(status);
            if ("HEAVY JAM".equals(status)) {
                bus.setEtaNextStop(bus.getEtaNextStop() + 10);
            } else {
                bus.setEtaNextStop(Math.max(2, bus.getEtaNextStop() - 10));
            }
            busRepo.save(bus);
        }
        return "redirect:/driver";
    }

    @PostMapping("/driver/next-stop")
    public String nextStop(@RequestParam String plateNo) {
        Bus bus = busRepo.findById(plateNo).orElse(null);
        if (bus != null) {
            bus.setPreviousLocation(bus.getCurrentLocation());
            bus.setPreviousEta(bus.getEtaNextStop());

            List<String> checkpoints = Route.getRouteCheckpoints(bus.getCurrentRouteId());
            int currentIndex = checkpoints.indexOf(bus.getCurrentLocation());

            if (currentIndex != -1 && currentIndex < checkpoints.size() - 1) {
                String nextStop = checkpoints.get(currentIndex + 1);
                bus.setCurrentLocation(nextStop);

                if (currentIndex + 2 < checkpoints.size()) {
                    String afterNext = checkpoints.get(currentIndex + 2);
                    int calculatedEta = Route.getEtaToNextStop(afterNext);
                    if ("HEAVY JAM".equals(bus.getTrafficStatus())) {
                        calculatedEta += 10;
                    }
                    bus.setEtaNextStop(calculatedEta);
                } else {
                    bus.setEtaNextStop(0);
                }
                busRepo.save(bus);
            }
        }
        return "redirect:/driver";
    }

    @PostMapping("/driver/undo-stop")
    public String undoStop(@RequestParam String plateNo) {
        Bus bus = busRepo.findById(plateNo).orElse(null);
        if (bus != null) {
            bus.setCurrentLocation(bus.getPreviousLocation());
            bus.setEtaNextStop(bus.getPreviousEta());
            busRepo.save(bus);
        }
        return "redirect:/driver";
    }

    @PostMapping("/driver/end")
    public String endJourney(@RequestParam String plateNo) {
        Bus bus = busRepo.findById(plateNo).orElse(null);
        if (bus != null) {
            bus.setStatus("AVAILABLE");
            bus.setCurrentDriverId(null);
            bus.setCurrentRouteId(null);
            bus.setCurrentLocation("Depot");
            bus.setEtaNextStop(0);
            bus.setOnDuty(false);
            bus.setTrafficStatus("NORMAL");
            busRepo.save(bus);
        }
        return "redirect:/driver";
    }
}