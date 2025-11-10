package app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import app.dto.LoginDTO;
import app.dto.PatientDTO;
import app.dto.PatientRegisterDTO;
import app.dto.ReportDTO;
import app.dto.TreatmentDTO;
import app.entity.Doctor;
import app.entity.History;
import app.entity.Patient;
import app.entity.PersonalHistory;
import app.entity.Report;
import app.entity.Role;
import app.entity.Treatment;
import app.entity.User;
import app.repository.DoctorRepository;
import app.repository.PatientRepository;
import app.repository.ReportRepository;
import app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PatientServiceImpl implements IPatientService{

	    private final PatientRepository patientRepository;
	    private final DoctorRepository doctorRepository;
	    private final ReportRepository reportRepository;
	    private final CloudinaryService cloudinaryService;
	    private final ModelMapper modelMapper;
	    private final UserRepository userRepository;
//    private PasswordEncoder passwordencoder;  // implement with spring security
    //-----------------------------------------------------------------------------------------------------
    // Conversion 
    // ✅ Convert Entity → DTO
    private PatientDTO toDTO(Patient patient) {
    	
    	 System.out.println("Entity DOB before mapping: " + patient.getDob());

        PatientDTO dto = modelMapper.map(patient, PatientDTO.class);
        System.out.println("Entity DOB after mapping: " + patient.getDob());
        System.out.println("DTO DOB after mapping: " + dto.getDob());

        // manually handle doctorIds (since ModelMapper won't map relationships by default)
        if (patient.getDoctors() != null) {
            dto.setDoctorIds(
                patient.getDoctors().stream()
                    .map(Doctor::getId)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // ✅ Convert DTO → Entity
    private Patient toEntity(PatientRegisterDTO dto) {
        Patient patient = modelMapper.map(dto, Patient.class);

        // manually set doctors if doctorIds are provided
        if (dto.getDoctorIds() != null && !dto.getDoctorIds().isEmpty()) {
            List<Doctor> doctors = doctorRepository.findAllById(dto.getDoctorIds());
            for (Doctor d : doctors) {
                patient.addDoctor(d); // maintains bidirectional consistency
            }
        }

        return patient;
    }

    //------------------------------------------------------------------------------------------------------
    // ✅ Create or register a new patient
    public PatientDTO createPatient(PatientRegisterDTO dto) {
        Patient patient = toEntity(dto);

        User user = new User();
        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPassword(dto.getPassword());// save raw password for now
        user.setRole(Role.PATIENT);

        User savedUser = userRepository.save(user);

        patient.setUser(savedUser);

        Patient savedPatient = patientRepository.save(patient);
        return toDTO(savedPatient);
    }
    
    
    
	@Override
	public PatientDTO login(LoginDTO dto) {
	
	Patient patient = patientRepository.findByUserEmailAndUserPassword(dto.getEmail(), dto.getPassword()).orElseThrow(()->new RuntimeException(" Invalid Email or Password "));
	
		return toDTO(patient);
	}

    
    
    
    
    
  
//        // 5️⃣ Update 
        @Transactional
        public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {

            // 1️⃣ Fetch managed entity
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

            // 2️⃣ Update basic fields
            modelMapper.map(patientDTO, patient); // if your mapper ignores nulls, safe shortcut for simple fields

            System.out.println("DTO DOB: " + patientDTO.getDob());
            System.out.println("Entity DOB before map: " + patient.getDob());

         // ✅ Manually apply DOB because ModelMapper sometimes skips LocalDate fields
            if (patientDTO.getDob() != null) {
                patient.setDob(patientDTO.getDob());
            }
            System.out.println("Entity DOB before map: " + patient.getDob());
            
            
         // ✅3 Handle Reports (Add new or update existing, don't delete old)
            if (patientDTO.getReports() != null && !patientDTO.getReports().isEmpty()) {
                for (ReportDTO reportDTO : patientDTO.getReports()) {

                    // 1️⃣ Try to find an existing report by ID
                    Optional<Report> existingReportOpt = patient.getReports().stream()
                            .filter(r -> reportDTO.getId() != null && r.getId().equals(reportDTO.getId()))
                            .findFirst();

                    if (existingReportOpt.isPresent()) {
                        // 2️⃣ Update existing report fields
                        Report existingReport = existingReportOpt.get();
                        modelMapper.map(reportDTO, existingReport);

                    } else {
                        // 3️⃣ Add new report (no ID or not found in existing)
                        Report newReport = modelMapper.map(reportDTO, Report.class);
                        newReport.setPatient(patient); // maintain bidirectional relation
                        patient.addReport(newReport);
                    }
                }
            }

         // ✅ 4.Handle Treatments (Add new or update existing, don't delete old)
            if (patientDTO.getTreatments() != null && !patientDTO.getTreatments().isEmpty()) {
                for (TreatmentDTO treatmentDTO : patientDTO.getTreatments()) {

                    // 1️⃣ Try to find an existing treatment by ID
                    Optional<Treatment> existingTreatmentOpt = patient.getTreatments().stream()
                            .filter(t -> treatmentDTO.getId() != null && t.getId().equals(treatmentDTO.getId()))
                            .findFirst();

                    if (existingTreatmentOpt.isPresent()) {
                        // 2️⃣ Update existing treatment fields
                        Treatment existingTreatment = existingTreatmentOpt.get();
                        modelMapper.map(treatmentDTO, existingTreatment);

                    } else {
                        // 3️⃣ Add new treatment (no ID or not found in existing)
                        Treatment newTreatment = modelMapper.map(treatmentDTO, Treatment.class);
                        newTreatment.setPatient(patient); // maintain bidirectional relation
                        patient.addTreatment(newTreatment);
                    }
                }
            }

            
            // 5. Update Doctors (ManyToMany)
            if (patientDTO.getDoctorIds() != null) {
                List<Doctor> newDoctors = doctorRepository.findAllById(patientDTO.getDoctorIds());
                patient.clearDoctors();
                newDoctors.forEach(patient::addDoctor);
            }

            // 6️⃣ Save and return
            Patient updated = patientRepository.save(patient);
            return toDTO(updated);
        }

  //------------------------------------------------------------------------------------------------------
        public Report uploadPatientReport(Long patientId,
                MultipartFile file,
                String reportType,
                String notes,
                String description) throws IOException {

       // 1️⃣ Fetch patient
          Patient patient = patientRepository.findById(patientId)
                 .orElseThrow(() -> new RuntimeException("Patient not found"));

            // 2️⃣ Upload file to Cloudinary : upload under patient_reports folder 
            String folderName = "patient_reports/" + patientId;// To keep uploads more organized,group by entity ID:
            String fileUrl = cloudinaryService.uploadFile(file, "patient_reports");

             // 3️⃣ Create Report entity
            Report report = Report.builder()
                                  .reportType(reportType)
                                  .notes(notes)
                                  .description(description)
                                  .reportDate(LocalDate.now())
                                  .fileUrl(fileUrl)
                                  .patient(patient)
                                  .build();

              // 4️⃣ Save report
                   return reportRepository.save(report);
}

        
        
        
   

    // ✅ Get all patients
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get all active patients
    public List<PatientDTO> getAllActivePatients() {
        return patientRepository.findByActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get active patient by ID
    public PatientDTO getActivePatientById(Long id) {
        Patient patient = patientRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Active patient not found"));
        return toDTO(patient);
    }

    // ✅ Get patient by ID
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return toDTO(patient);
    }

    // ✅ Soft delete
    public void deactivatePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setActive(false);
        patientRepository.save(patient);
    }

    // ✅ Assign a doctor to patient
    public PatientDTO assignDoctor(Long patientId, Long doctorId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        patient.addDoctor(doctor);
        return toDTO(patientRepository.save(patient));
    }



}
