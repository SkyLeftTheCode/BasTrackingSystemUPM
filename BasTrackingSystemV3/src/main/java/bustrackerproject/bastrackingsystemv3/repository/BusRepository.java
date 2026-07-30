package bustrackerproject.bastrackingsystemv3.repository;

import bustrackerproject.bastrackingsystemv3.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    List<Bus> findByCurrentRouteId(String routeId);
}