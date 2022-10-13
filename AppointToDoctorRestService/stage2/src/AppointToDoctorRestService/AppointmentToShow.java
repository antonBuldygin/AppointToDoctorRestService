package AppointToDoctorRestService;

import java.time.LocalDate;

public class AppointmentToShow {
    private long idApp;
    private String doctorName;
    private String specialization;
    private long doctorId;
    private String patientName;
    private LocalDate date;

    public AppointmentToShow(long idApp, String doctorName, String specialization, long doctorId, String patientName, LocalDate date) {
        this.idApp = idApp;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.doctorId = doctorId;
        this.patientName = patientName;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
