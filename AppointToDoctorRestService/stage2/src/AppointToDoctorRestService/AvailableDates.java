package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

//import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvailableDates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    LocalDate availabletime;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id" )
    private Doctor doctor;
    boolean booked;


    public AvailableDates() {
    }

    public AvailableDates(LocalDate avalabletime, boolean booked, Doctor doctor) {
        this.availabletime = avalabletime;
        this.booked = booked;
        this.doctor = doctor;
    }

    public AvailableDates(LocalDate availabletime, boolean booked) {
        this.availabletime = availabletime;
        this.booked = booked;
    }

    public LocalDate getAvailabletime() {
        return availabletime;
    }

    public void setAvailabletime(LocalDate availabletime) {
        this.availabletime = availabletime;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Doctor getDoctor() {
        return doctor;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


}
