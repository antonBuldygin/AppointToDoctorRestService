package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Appoint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idApp;


    @JoinColumn(name = "doctor_id")
    @ManyToOne(fetch = FetchType.EAGER)
        @NotNull
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    @NotNull
    private Patient patient;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;

    public Appoint(long idApp, Doctor doctor, Patient patient, LocalDate date) {
        this.idApp = idApp;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    public Appoint() {
    }

    public long getIdApp() {
        return idApp;
    }

    public void setIdApp(long idApp) {
        this.idApp = idApp;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
