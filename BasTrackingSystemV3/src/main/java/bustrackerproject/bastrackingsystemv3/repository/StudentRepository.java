package bustrackerproject.bastrackingsystemv3.repository;

import bustrackerproject.bastrackingsystemv3.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByMatricNoAndPassword(String matricNo, String password);
}