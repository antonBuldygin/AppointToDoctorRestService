package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @NotNull
    @NotEmpty
    private String patientName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Set<Appoint> getAppoints() {
        return appoints;
    }
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setAppoints(Set<Appoint> appoints) {
        this.appoints = appoints;
    }

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Set<Appoint> appoints;

    public Patient(String patientName) {
        this.patientName = patientName;
    }

    public Patient() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


}
