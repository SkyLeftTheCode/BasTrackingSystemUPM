package bustrackerproject.bastrackingsystemv3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Driver {
    @Id
    private String driverId;
    private String password;
    private String name;
    private int age;
    private String sex;

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
}