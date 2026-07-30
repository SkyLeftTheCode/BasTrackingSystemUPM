package bustrackerproject.bastrackingsystemv3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Bus {
    @Id
    private String plateNo;
    private String status;
    private String currentRouteId;
    private String currentLocation;
    private int etaNextStop;
    private String currentDriverId;

    // Distribution & Operations management fields
    private boolean onDuty = false;
    private String trafficStatus = "NORMAL";
    private String previousLocation = "Depot";
    private int previousEta = 0;

    // --- GETTERS & SETTERS ---
    public String getPlateNo() { return plateNo; }
    public void setPlateNo(String plateNo) { this.plateNo = plateNo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrentRouteId() { return currentRouteId; }
    public void setCurrentRouteId(String currentRouteId) { this.currentRouteId = currentRouteId; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public int getEtaNextStop() { return etaNextStop; }
    public void setEtaNextStop(int etaNextStop) { this.etaNextStop = etaNextStop; }

    public String getCurrentDriverId() { return currentDriverId; }
    public void setCurrentDriverId(String currentDriverId) { this.currentDriverId = currentDriverId; }

    public boolean isOnDuty() { return onDuty; }
    public void setOnDuty(boolean onDuty) { this.onDuty = onDuty; }

    public String getTrafficStatus() { return trafficStatus; }
    public void setTrafficStatus(String trafficStatus) { this.trafficStatus = trafficStatus; }

    public String getPreviousLocation() { return previousLocation; }
    public void setPreviousLocation(String previousLocation) { this.previousLocation = previousLocation; }

    public int getPreviousEta() { return previousEta; }
    public void setPreviousEta(int previousEta) { this.previousEta = previousEta; }
}