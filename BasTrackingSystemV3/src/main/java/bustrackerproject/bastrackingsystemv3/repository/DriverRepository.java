package bustrackerproject.bastrackingsystemv3.repository;

import bustrackerproject.bastrackingsystemv3.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    Driver findByDriverIdAndPassword(String driverId, String password);
}