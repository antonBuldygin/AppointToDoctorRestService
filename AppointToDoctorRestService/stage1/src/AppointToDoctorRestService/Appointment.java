package AppointToDoctorRestService;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


public class Appointment implements Comparable {
    private long idStatus;
    private long idApp;
    @NotEmpty
    @NotNull
    private String doctorName;

    @NotEmpty
    @NotNull
    private String patientName;


    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")

    private Date date;


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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) throws ParseException {
        if(date==null){
            date =new Date();
            date.setYear(0000);

        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        this.date = formatter.parse(formatter.format(date));
    }

    public Appointment(String doctorName, String patientName, Date date) {
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.date = date;
    }

    public Appointment() {
    }

    @Override
    public String toString() {
        return "Appointment{" +

                " Doctor=" + doctorName +
                ", Patient=" + patientName +
                ", Date =" + date +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;


    }
}
