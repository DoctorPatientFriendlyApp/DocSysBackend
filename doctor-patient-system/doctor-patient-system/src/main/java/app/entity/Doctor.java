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
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String sex;
    private String address;
    private String mobile;
    private String aadhaarOrPan;
    private String specialization;
    private String certificateUrl; // uploaded file
    private boolean verified = false;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "doctors")
    @JsonIgnore  // To prevent infinite recursion during JSON serialization (when using Jackson):
    private List<Patient> patients = new ArrayList<>();
    
    
    //-------------------------------------------------------------------------------------------------
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }


}
