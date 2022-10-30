package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvailableDates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    LocalDate avalabletime;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id" )
    private Doctor doctor;
    boolean booked;


    public AvailableDates() {
    }

    public AvailableDates(LocalDate avalabletime, boolean booked, Doctor doctor) {
        this.avalabletime = avalabletime;
        this.booked = booked;
        this.doctor = doctor;
    }

    public AvailableDates(LocalDate avalabletime, boolean booked) {
        this.avalabletime = avalabletime;
        this.booked = booked;
    }

    public LocalDate getAvalabletime() {
        return avalabletime;
    }

    public void setAvalabletime(LocalDate avalabletime) {
        this.avalabletime = avalabletime;
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
