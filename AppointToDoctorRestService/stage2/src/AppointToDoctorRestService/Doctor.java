package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Doctor {

    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

//    @NotNull
//    @NotEmpty
   private String doctorName;
    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY,value="specialization")
    @Column(name = "specialization")
    private String dr = "physician";


    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public Doctor() {
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorName='" + doctorName + '\'' +
                '}';
    }

    public Doctor(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appoint> appoint;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<AvailableDates> availableDatesSet;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<AvailableDates> getAvailableDatesSet() {
        return availableDatesSet;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setAvailableDatesSet(List<AvailableDates> availableDatesSet) {
        this.availableDatesSet = availableDatesSet;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Appoint> getAppoint() {
        return appoint;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setAppoint(List<Appoint> appoint) {
        this.appoint = appoint;
    }

//    @PreRemove
//    private void preRemove() {
//        appoint.forEach( child -> child.setDoctor(null));
//    }
}
