package AppointmentBookingToDoctorRestService;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Appointment implements Comparable {
    private long idStatus;
    private long idApp;
    private String doctorName;
    private String patientName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        this.date= formatter.parse(formatter.format(date));
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
                ", Date =" + date+
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;


    }
}
