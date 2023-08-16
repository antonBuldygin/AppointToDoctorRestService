package AppointToDoctorRestService;


import com.fasterxml.jackson.annotation.JsonFormat;

//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class Appointment implements Comparable {
    private long idStatus;
    private long idApp;
//    @NotEmpty
//    @NotNull
    private String doctor;

//    @NotEmpty
//    @NotNull
    private String patient;


//    @NotNull
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private LocalDate date;


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

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) throws ParseException {
        if(date==null){
            date = LocalDate.ofYearDay(0000,00);;


        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        this.date = LocalDate.parse(date.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")),formatter);
    }

    public Appointment(String doctor, String patient, LocalDate date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    public Appointment() {
    }

    @Override
    public String toString() {
        return "Appointment{" +

                " Doctor=" + doctor +
                ", Patient=" + patient +
                ", Date =" + date +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;


    }
}
