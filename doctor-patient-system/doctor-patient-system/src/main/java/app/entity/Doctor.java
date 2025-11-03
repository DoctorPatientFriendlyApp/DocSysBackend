package app.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
//@Builder
public class Doctor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    private String address;
    @Column(unique = true)
    private String mobile;
    @Column(unique = true)
    private String aadhaar;
    @Column(unique = true)
    private String pan;
    private String specialization;
    @Column(length = 500)
    private String certificateUrl; // uploaded file
    
    private boolean verified = false;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Doctors ↔ Patients (M:M)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "doctors", fetch = FetchType.EAGER) // get all patients when accessing patient
//    @JsonIgnore  // To prevent infinite recursion during JSON serialization (when using Jackson):
    private List<Patient> patients = new ArrayList<>();
    
 //------------------------------------------------------------------------------------------------------------------    
     // ✅ Helper methods: addPatient()
    
    // add patient to current doctor
    public void addPatient(Patient patient) {
        if (patient == null) return;
        if (!this.patients.contains(patient)) {
            this.patients.add(patient);
        }
        if (!patient.getDoctors().contains(this)) {
            patient.getDoctors().add(this);
        }
    }

    // ✅ Helper method: removePatient()
    public void removePatient(Patient patient) {
        if (patient == null) return;
        this.patients.remove(patient);
        patient.getDoctors().remove(this);
    }

    // ✅ Optional: clear all relationships safely
    public void clearPatients() {
        for (Patient p : new ArrayList<>(patients)) {
            removePatient(p);
        }
    }

    
}
