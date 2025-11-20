
package app.entity; 


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.query.sqm.FetchClauseType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "patients")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder    // Enables builder pattern — you can create objects like Patient p = Patient.builder().name("Rushikesh").age(23).build();
 public class Patient extends BaseEntity{
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
    @Column(nullable = true, unique = true)
    private String aadhaar;
    @Column(nullable = true,unique=true)
    private String pan;
    
    private LocalDate dob;
    
    @Column(nullable = true)
    private String bloodGroup;
  
    @Column(nullable = true)
    private Thermal thermalType;
    
    
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private SocialClass socialEconomicalStatus;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ZodiacSign zodiacSign; // राशी
    @Column(nullable = true)
    private String doctorAdvice;

    // User (1:1)
    @OneToOne                      // Each patient has one user account
    @JoinColumn(name = "user_id")  // Creates a foreign key column in the patient table pointing to the users table.
    private User user;

    // joinColumns: This attribute within @JoinTable defines the columns in the join table that refer to the owning side of the relationship.
    // inverseJoinColumns: This attribute defines the columns in the join table that refer to the inverse side of the relationship.
    
    
    // Doctors (M:M)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)                            // A patient can consult multiple doctors & A doctor can treat multiple patients.
    @JoinTable(name = "doctor_patient",
            joinColumns = @JoinColumn(name = "patient_id"),       // creates an intermediate table doctor_patient with both IDs: doctorID & PatientId 
            inverseJoinColumns = @JoinColumn(name = "doctor_id")) // referse to doctor (inverse side)
    @JsonIgnore
    private List<Doctor> doctors = new ArrayList<>();

    
    // History (1:1)    
    @OneToOne(cascade = CascadeType.ALL, optional = true,fetch = FetchType.EAGER)       // cascade = CascadeType.ALL means — when you save a patient, the related history gets automatically saved too.
    private History history;

    // GeneralExamination(1:1)
    @OneToOne(cascade=CascadeType.ALL,optional=true, fetch=FetchType.EAGER)
    private GeneralExamination generalExamination;
    
    //PatientDescription (1:1)
    @OneToOne(cascade=CascadeType.ALL,optional=true, fetch=FetchType.EAGER)
    private PatientDescription patientDescription;
    
    // VitalSigns (1:1)
    @OneToOne(cascade=CascadeType.ALL,optional=true, fetch=FetchType.EAGER)
    private VitalSigns vitalSigns;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "systemic_exam_id")
    private SystemicExamination systemicExamination;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "diagnosis_details_id")
    private DiagnosisDetails diagnosisDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    
    
   // Treatments (1:N)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude                                                         // remove treatments which are not mapped with patient
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)     // One patient can have multiple treatments.
    @JsonIgnore                                                     // To prevent infinite recursion during JSON serialization (when using Jackson):
    private List<Treatment> treatments = new ArrayList<>();         //Treatment entity has a field private Patient patient;
    
    // Reports (1:N)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude                                                       // remove report which are not mapped with patient
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval = true)    //Automatically persisted/deleted with the patient.
    @JsonManagedReference  // avoid infinite looping
    private List<Report> reports = new ArrayList<>();              //One patient can have many lab reports (e.g., CBC, MRI, etc.)
    
 // -------------------------------------------------------------------
    // ✅ Helper Methods — maintain bidirectional consistency
    // -------------------------------------------------------------------


//  /** Doctors **/
  public void addDoctor(Doctor doctor) {
      if (doctor == null) return;
      if (!doctors.contains(doctor)) doctors.add(doctor);
      if (!doctor.getPatients().contains(this)) doctor.getPatients().add(this);
  }

  public void removeDoctor(Doctor doctor) {
      if (doctor == null) return;
      doctors.remove(doctor);
      doctor.getPatients().remove(this);
  }

  public void clearDoctors() {
      for (Doctor d : new ArrayList<>(doctors)) removeDoctor(d);
  }
  
  
//    /** Treatments **/
    public void addTreatment(Treatment treatment) {
        if (treatment != null) {
            treatments.add(treatment);
            treatment.setPatient(this);
        }
    }

    public void removeTreatment(Treatment treatment) {
        if (treatment != null) {
            treatments.remove(treatment);
            treatment.setPatient(null);
        }
    }

    public void clearTreatments() {
        for (Treatment t : new ArrayList<>(treatments)) removeTreatment(t);
    }

    /** Reports **/
    public void addReport(Report report) {
        if (report != null) {
            reports.add(report);
            report.setPatient(this);
        }
    }

    public void removeReport(Report report) {
        if (report != null) {
            reports.remove(report);
            report.setPatient(null);
        }
    }

    public void clearReports() {
        for (Report r : new ArrayList<>(reports)) removeReport(r);
    }

    
    
}