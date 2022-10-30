package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class AppointmentToShow {
    private long idApp;
    private String doctor;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String specialization;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long doctorId;
    private String patient;
    private LocalDate date;

    public AppointmentToShow(long idApp, String doctor, String specialization, long doctorId, String patient, LocalDate date) {
        this.idApp = idApp;
        this.doctor = doctor;
        this.specialization = specialization;
        this.doctorId = doctorId;
        this.patient = patient;
        this.date = date;
    }

    public AppointmentToShow() {
    }

    public long getIdApp() {
        return idApp;
    }

    public void setIdApp(long idApp) {
        this.idApp = idApp;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
