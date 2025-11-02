package app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import app.repository.HistoryRepository;
import app.repository.PatientRepository;
import app.repository.ReportRepository;
import app.repository.TreatmentRepository;
import app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PatientService {

	    private final PatientRepository patientRepository;
	    private final DoctorRepository doctorRepository;
	    private final ReportRepository reportRepository;
	    private final TreatmentRepository treatmentRepository;
	    private final HistoryRepository historyRepository;
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
            
            
         // ✅ Handle Reports (Add new or update existing, don't delete old)
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

         // ✅ Handle Treatments (Add new or update existing, don't delete old)
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

            
            // 3️⃣ Update Doctors (ManyToMany)
            if (patientDTO.getDoctorIds() != null) {
                List<Doctor> newDoctors = doctorRepository.findAllById(patientDTO.getDoctorIds());
                patient.clearDoctors();
                newDoctors.forEach(patient::addDoctor);
            }

//            // 4️⃣ Update Treatments (OneToMany)
//            if (patientDTO.getTreatments() != null) {
//                List<Treatment> existingTreatments = patient.getTreatments();
//
//                // Remove deleted ones
//                existingTreatments.removeIf(existing ->
//                        patientDTO.getTreatments().stream()
//                                .noneMatch(dto -> dto.getId() != null && dto.getId().equals(existing.getId()))
//                );
//
//                // Add or update
//                for (TreatmentDTO tDTO : patientDTO.getTreatments()) {
//                    Treatment treatment = existingTreatments.stream()
//                            .filter(t -> t.getId() != null && t.getId().equals(tDTO.getId()))
//                            .findFirst()
//                            .orElse(null);
//
//                    if (treatment == null) {
//                        treatment = new Treatment();
//                        modelMapper.map(tDTO, treatment);
//                        treatment.setPatient(patient);
//                        existingTreatments.add(treatment);
//                    } else {
//                        treatment.setMedicineName(tDTO.getMedicineName());
//                        treatment.setAmount(tDTO.getAmount());
//                        treatment.setDays(tDTO.getDays());
//                        treatment.setBeforeCondition(tDTO.getBeforeCondition());
//                        treatment.setAfterCondition(tDTO.getAfterCondition());
//                    }
//                }
//            }

            // 5️⃣ Update Reports (OneToMany)
//            if (patientDTO.getReports() != null) {
//                List<Report> existingReports = patient.getReports();
//
//                // Step 1 — Remove reports that are not present in DTO
//                existingReports.removeIf(existing ->
//                        patientDTO.getReports().stream()
//                                .noneMatch(dto -> dto.getId() != null && dto.getId().equals(existing.getId()))
//                );
//
//                // Step 2 — Add or update
//                for (ReportDTO reportDTO : patientDTO.getReports()) {
//                    Report report = existingReports.stream()
//                            .filter(r -> r.getId() != null && r.getId().equals(reportDTO.getId()))
//                            .findFirst()
//                            .orElse(null);
//
//                    if (report == null) {
//                        // 🆕 New Report
//                        report = new Report();
//                        modelMapper.map(reportDTO, report);
//                        report.setPatient(patient);
//                        existingReports.add(report);
//                    } else {
//                        // ✏️ Update Existing
//                        report.setReportType(reportDTO.getReportType());
//                        report.setNotes(reportDTO.getNotes());
//                        report.setFileUrl(reportDTO.getFileUrl());
//                        report.setReportDate(reportDTO.getReportDate());
//                    }
//                }
//            }

            // 6️⃣ Save and return
            Patient updated = patientRepository.save(patient);
            return toDTO(updated);
        }

    
//    @Transactional
//    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
//        // 1️⃣ Fetch existing patient (managed entity)
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
//
//        // 2️⃣ Map all nested properties automatically (ModelMapper handles deep mapping)
//        modelMapper.map(patientDTO, patient);
//
//        // 3️⃣ Manually handle Many-to-Many Doctor relationships
//        if (patientDTO.getDoctorIds() != null) {
//            List<Doctor> newDoctors = doctorRepository.findAllById(patientDTO.getDoctorIds());
//            patient.clearDoctors();  // remove old links
//            newDoctors.forEach(patient::addDoctor);// add new doctors
//        }
//
//        // ⚙️ NOTE: Since ModelMapper can handle nested structures like history.personalHistory we no longer need manual mapping for those.
//        // 4 Handle reports manually
////      // ✅ Update treatments
//        if (patientDTO.getTreatments() != null) {
//            List<Treatment> existingTreatments = patient.getTreatments();
//            List<Treatment> updatedTreatments = new ArrayList<>();
//
//            for (TreatmentDTO tDTO : patientDTO.getTreatments()) {
//                Treatment treatment = null;
//
//                // 1️⃣ If ID present, try fetching managed entity
//                if (tDTO.getId() != null && tDTO.getId() != 0) {
//                    treatment = treatmentRepository.findById(tDTO.getId())
//                            .orElse(null);
//                }
//
//                // 2️⃣ If not found or new, create a fresh entity
//                if (treatment == null) {
//                    treatment = new Treatment();
//                }
//
//                // 3️⃣ Map fields from DTO → Entity
//                modelMapper.map(tDTO, treatment);
//                treatment.setPatient(patient);
//
//                updatedTreatments.add(treatment);
//            }
//
//            // 4️⃣ Remove treatments not in updated list (orphan removal-safe)
//            existingTreatments.clear();
//            existingTreatments.addAll(updatedTreatments);
//        }
//
////        // ✅ Update reports
//        // Handle nested list: Reports
//        if (patientDTO.getReports() != null) {
//            List<Report> existingReports = patient.getReports();
//
//            // 1️⃣ Remove reports that are no longer present
//            existingReports.removeIf(existing ->
//                patientDTO.getReports().stream()
//                    .noneMatch(dto -> dto.getId() != null && dto.getId().equals(existing.getId()))
//            );
//
//            // 2️⃣ Iterate DTO reports
//            for (ReportDTO reportDTO : patientDTO.getReports()) {
//                Report report = null;
//
//                // ✅ Try to find existing one in current managed list
//                if (reportDTO.getId() != null && reportDTO.getId() != 0) {
//                    report = existingReports.stream()
//                            .filter(r -> r.getId().equals(reportDTO.getId()))
//                            .findFirst()
//                            .orElse(null);
//                }
//
//                if (report == null) {
//                    // 🆕 New report
//                    report = new Report();
//                    modelMapper.map(reportDTO, report);
//                    report.setPatient(patient);
//                    existingReports.add(report);
//                } else {
//                    // ✏️ Update managed existing report
//                    report.setReportType(reportDTO.getReportType());
//                    report.setNotes(reportDTO.getNotes());
//                    report.setFileUrl(reportDTO.getFileUrl());
//                    report.setReportDate(reportDTO.getReportDate());
//                }
//            }
//        }
//
//        // 6. Persist changes
//        Patient updated = patientRepository.save(patient);
//
//        // 7. Return DTO
//        return toDTO(updated);
//    }

    
//    @Transactional
//    public PatientDTO updatePatient(Long id, PatientDTO dto) {
//        // 1️⃣ Fetch existing patient (managed entity)
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
//
//        // 2️⃣ Update simple fields
//        modelMapper.map(dto, patient);
//
//        // 3️⃣ Update doctor relationships (M:M)
//        if (dto.getDoctorIds() != null) {
//            List<Doctor> newDoctors = doctorRepository.findAllById(dto.getDoctorIds());
//            patient.clearDoctors();
//            for (Doctor doctor : newDoctors) {
//                patient.addDoctor(doctor);
//            }
//        }
//
//        // 4️⃣ Update history and nested personal history
//        if (dto.getHistory() != null) {
//            History existingHistory = patient.getHistory();
//            if (existingHistory == null) {
//                existingHistory = new History();
//                patient.setHistory(existingHistory);
//            }
//
//            // map simple history fields
//            modelMapper.map(dto.getHistory(), existingHistory);
//
//            if (dto.getHistory().getPersonalHistory() != null) {
//                PersonalHistory existingPH = existingHistory.getPersonalHistory();
//                if (existingPH == null) {
//                    existingPH = new PersonalHistory();
//                    existingHistory.setPersonalHistory(existingPH);
//                }
//                modelMapper.map(dto.getHistory().getPersonalHistory(), existingPH);
//            }
//        }
//        
//        
//        
//
//        // 5️⃣ Update treatments (1:N)
//        if (dto.getTreatments() != null) {
//            patient.clearTreatments();
//            dto.getTreatments().forEach(treatmentDTO -> {
//                Treatment treatment;
//                if (treatmentDTO.getId() != null) {
//                    // Fetch managed entity to avoid detached issue
//                    treatment = treatmentRepository.findById(treatmentDTO.getId())
//                            .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + treatmentDTO.getId()));
//                    modelMapper.map(treatmentDTO, treatment);
//                } else {
//                    treatment = modelMapper.map(treatmentDTO, Treatment.class);
//                }
//                patient.addTreatment(treatment);
//            });
//        }
//
//        // 6️⃣ Update reports (1:N)
//        if (dto.getReports() != null) {
//            patient.clearReports();
//            dto.getReports().forEach(reportDTO -> {
//                Report report;
//
//                if (reportDTO.getId() != null) {
//                    // 🩵 Fetch existing report to get managed entity
//                    report = reportRepository.findById(reportDTO.getId())
//                            .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportDTO.getId()));
//                    modelMapper.map(reportDTO, report);
//                } else {
//                    // 🩵 Create new report (id == null → INSERT)
//                    report = modelMapper.map(reportDTO, Report.class);
//                }
//
//                // Maintain bidirectional link
//                patient.addReport(report);
//            });
//        }
//
//        // 7️⃣ Save only the parent
//        Patient updated = patientRepository.save(patient);
//
//        // 8️⃣ Return DTO
//        return toDTO(updated);
//    }




//  // ✅ Update diagnosis, advice, or history
//    @Transactional
//    public PatientDTO updatePatient(Long id, PatientDTO dto) {
//        // 1️⃣ Fetch existing patient
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
//
//        // 2️⃣ Update simple fields
//        modelMapper.map(dto, patient);
//
//        // 3️⃣ Update doctor relationships
//        if (dto.getDoctorIds() != null) {
//            patient.clearDoctors(); // remove old links
//            List<Doctor> doctors = doctorRepository.findAllById(dto.getDoctorIds());
//            doctors.forEach(patient::addDoctor); // maintain bidirectional sync
//        }
//
//        // 4️⃣ Update history and nested personal history
//        if (dto.getHistory() != null) {
//            History existingHistory = patient.getHistory();
//            if (existingHistory == null) {
//                existingHistory = new History();
//                patient.setHistory(existingHistory);
//            }
//
//            // map simple fields
//            modelMapper.map(dto.getHistory(), existingHistory);
//
//            // handle nested PersonalHistory
//            if (dto.getHistory().getPersonalHistory() != null) {
//                PersonalHistory existingPH = existingHistory.getPersonalHistory();
//                if (existingPH == null) {
//                    existingPH = new PersonalHistory();
//                    existingHistory.setPersonalHistory(existingPH);
//                }
//                modelMapper.map(dto.getHistory().getPersonalHistory(), existingPH);
//            }
//        }
//
//        // 5️⃣ Update treatments using helper
//        if (dto.getTreatments() != null) {
//            patient.clearTreatments(); // clear old ones
//            dto.getTreatments().forEach(tDto -> {
//                Treatment treatment = modelMapper.map(tDto, Treatment.class);
//                patient.addTreatment(treatment); // helper ensures bidirectional consistency
//            });
//        }
//
//        // 6️⃣ Update reports using helper
//        if (dto.getReports() != null) {
//            patient.clearReports();
//            for (ReportDTO rDto : dto.getReports()) {
//                Report report;
//
//                if (rDto.getId() != null) {
//                    report = reportRepository.findById(rDto.getId())
//                            .orElseThrow(() -> new RuntimeException("Report not found: " + rDto.getId()));
//                } else {
//                    report = new Report();
//                }
//
//                modelMapper.map(rDto, report);
//                patient.addReport(report);
//            }
//        }
//
//
//        // 7️⃣ Save updated patient
//        Patient updated = patientRepository.save(patient);
//        return toDTO(updated);
//    }

    
    
//    public PatientDTO updatePatient(Long id, PatientDTO dto) {
//        // 1️ Fetch existing patient (managed entity)
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
//
//        // 2️⃣ Update basic fields	
//        modelMapper.map(dto, patient);  // copies basic non-null fields (name, address, etc.)
//
//        // 3️⃣ Update doctor relationships (bidirectional sync)
//        if (dto.getDoctorIds() != null) {
//            // Fetch managed doctor entities
//            List<Doctor> newDoctors = doctorRepository.findAllById(dto.getDoctorIds());
//
//            // Clear old relationships safely
//            patient.clearDoctors();
//
//            // Add new ones — helper ensures both sides (doctor↔patient) stay consistent
//            for (Doctor doctor : newDoctors) {
//                patient.addDoctor(doctor);
//            }
//        }
//        
//        // 4. Handle history (nested entity)
//        if (dto.getHistory() != null) {
//            History existingHistory = patient.getHistory();
//            History incomingHistory = dto.getHistory();
//
//            if (existingHistory == null) {
//                // first-time assignment: create new entity, don't attach detached one
//                existingHistory = new History();
//                patient.setHistory(existingHistory);
//            }
//
//            // ✅ copy field-by-field (no merge)
//            existingHistory.setPastHistory(incomingHistory.getPastHistory());
//            existingHistory.setSurgeryHistory(incomingHistory.getSurgeryHistory());
//            existingHistory.setTreatmentHistory(incomingHistory.getTreatmentHistory());
//            existingHistory.setCurrentHistory(incomingHistory.getCurrentHistory());
//            existingHistory.setFamilyHistory(incomingHistory.getFamilyHistory());
//
//            // 5 Handle personalHistory (nested inside history)
//            if (incomingHistory.getPersonalHistory() != null) {
//                PersonalHistory existingPH = existingHistory.getPersonalHistory();
//                PersonalHistory incomingPH = incomingHistory.getPersonalHistory();
//
//                if (existingPH == null)
//                    existingPH = new PersonalHistory();
//
//                // ✅ Use model mapper to map this 
//                existingPH.setDiet(incomingPH.getDiet());
//                existingPH.setAppetite(incomingPH.getAppetite());
//                existingPH.setDesiredFood(incomingPH.getDesiredFood());
//                existingPH.setAversion(incomingPH.getAversion());
//                existingPH.setThirst(incomingPH.getThirst());
//                existingPH.setSleep(incomingPH.getSleep());
//                existingPH.setHabits(incomingPH.getHabits());
//                existingPH.setMicturitionPerDay(incomingPH.getMicturitionPerDay());
//                existingPH.setMicturitionPerNight(incomingPH.getMicturitionPerNight());
//                existingPH.setDreams(incomingPH.getDreams());
//                existingPH.setPerspiration(incomingPH.getPerspiration());
//                existingPH.setMasturbationPerWeek(incomingPH.getMasturbationPerWeek());
//                existingPH.setMasturbationPerMonth(incomingPH.getMasturbationPerMonth());
//                existingPH.setPersonalSexualHistory(incomingPH.getPersonalSexualHistory());
//                existingPH.setExtraMaritalHistory(incomingPH.getExtraMaritalHistory());
//                existingPH.setMenstrualHistory(incomingPH.getMenstrualHistory());
//                existingPH.setObstetricHistory(incomingPH.getObstetricHistory());
//
//                // reattach safely
//                existingHistory.setPersonalHistory(existingPH);
//            }
//        }
//
//        // 6. Save only the parent — cascade handles children
//        Patient updated = patientRepository.save(patient);
//
//        // 7. Convert to DTO and return
//        return toDTO(updated);
//    }

  

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
