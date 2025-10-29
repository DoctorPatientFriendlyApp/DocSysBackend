package app.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter 
@Setter @NoArgsConstructor 
@AllArgsConstructor
//@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String sex;
    private String address;
    private String mobile;
    private String aadhaarOrPan;
    private LocalDate dob;
    private String bloodGroup;
    private String diagnosis;
    private String socialStatus;
    private String zodiacSign;
    private String doctorAdvice;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "doctor_patient",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    private List<Doctor> doctors = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private History history;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore // To prevent infinite recursion during JSON serialization (when using Jackson):
    private List<Treatment> treatments = new ArrayList<>();
    

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();
    
    //--------------------------------------------------------------------------------
    
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

}
