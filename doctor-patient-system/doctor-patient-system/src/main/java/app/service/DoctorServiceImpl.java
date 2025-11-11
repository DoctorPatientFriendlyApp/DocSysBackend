package app.service;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.dto.DoctorDTO;
import app.dto.DoctorRegisterDTO;
import app.dto.LoginDTO;
import app.entity.Doctor;
import app.entity.Patient;
import app.entity.Role;
import app.entity.User;
import app.repository.DoctorRepository;
import app.repository.PatientRepository;
import app.repository.UserRepository;
//import app.repository.userRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class DoctorServiceImpl implements IDoctorService {
	
	
	private final DoctorRepository doctorRepository; 
	private final PatientRepository patientRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final CloudinaryService cloudinaryService;
//	private final PasswordEncoder passwordEncoder;
	//-------------------------------------------------------------------------------------------
	// conversion dto -> entity & entity to dto
	

	  // Convert Entity → DTO
    private DoctorDTO toDTO(Doctor doctor) {
    	// map doctor with doctordto.class
        DoctorDTO doctorDTO = modelMapper.map(doctor, DoctorDTO.class);

        if (doctor.getPatients() != null && !doctor.getPatients().isEmpty()) {
        	 // get the list of Patients Ids 
            List<Long> patientIdsList = doctor.getPatients()
                    .stream()
                    .map(Patient::getId)
                    .collect(Collectors.toList());
        
         // set patients IDs
            doctorDTO.setPatientIds(patientIdsList);
            System.err.println(" Email "+ doctor.getUser().getEmail());
            
            if (doctor.getUser() != null) {
            	doctorDTO.setEmail(doctor.getUser().getEmail());
                }
 
        }

        return doctorDTO;
    }
	
	
    // Convert DTO → Entity
    private Doctor toEntity(DoctorRegisterDTO doctorDTO) {
    	
    	// map dto with doct.class
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);

        // check if any patients mapped with doctor
        if (doctorDTO.getPatientIds() != null && !doctorDTO.getPatientIds().isEmpty()) {
            List<Patient> patients = patientRepository.findAllById(doctorDTO.getPatientIds());
            doctor.setPatients(patients);
        }

        return doctor;
    }
	
	//--------------------------------------------------------------------------------------------
    
    // create doctor
	public DoctorDTO createDoctor(DoctorRegisterDTO doctorDTO) {
	    // 1️⃣ Convert DTO → Entity
	    Doctor doctor = toEntity(doctorDTO);

	    // 2️⃣ Create a new User for this doctor
	    User user = new User();
	    user.setEmail(doctorDTO.getEmail());        // assuming doctorDTO has email field
//	    user.setPassword(passwordEncoder.encode(doctorDTO.getPassword())); // encrypt password
	    user.setPassword(doctorDTO.getPassword());
	    user.setRole(Role.DOCTOR);

	    // 3️⃣ Save user
	    User savedUser = userRepository.save(user);

	    // 4️⃣ Link doctor with user
	    doctor.setUser(savedUser);

	    // 5️⃣ Save doctor
	    Doctor savedDoctor = doctorRepository.save(doctor);

	    return toDTO(savedDoctor);// return savedDoctorDto :  Convert back to DTO for response
	  
	}
	
	
	
//	@Override
//	public DoctorLoginDTO login(DoctorLoginDTO dto) {
//
//		Doctor  doctor = doctorRepository.findByUserEmailAndPassword(dto.getEmail(),dto.getPassword()).orElseThrow(()->new RuntimeException(" Wrong email or password"));
//	    
//	   return toDTO(doctor);
//	}

    @Override
    public DoctorDTO login(LoginDTO dto) {
    	
    	System.out.println("Email in dto: "+ dto.getEmail() + "Password in dto : "+ dto.getPassword());
        // ✅ Find doctor by nested email and password
        Doctor doctor = doctorRepository
                .findByUserEmailAndUserPassword(dto.getEmail(), dto.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
       
        System.out.print(" "+ doctor.getName());
        DoctorDTO responce = toDTO(doctor);
        responce.setEmail(doctor.getUser().getEmail());
        return responce;
    }
	
	
	// get all doctors
	public List<DoctorDTO> getAllDoctors(){
	                                 // get doctors & convert them into dtos 
	  List<DoctorDTO> doctorDTOs =  doctorRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	 
	  
	  return doctorDTOs;
	}
	
	
	// Get Doctors who are active
	public List<DoctorDTO> getAllActiveDoctors(){
		
		List<DoctorDTO> activeDoctorDTOs = doctorRepository.findByActiveTrue().stream().map(this::toDTO).collect(Collectors.toList());
		return activeDoctorDTOs;
	}

	
	
	// ✅ Get doctor by ID
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
        System.out.print(" Email : " + doctor.getUser().getEmail());
        DoctorDTO dto= toDTO(doctor);
        //  // set email 
        dto.setEmail(doctor.getUser().getEmail());
//        System.out.print(" Email in dto : " + dto.getEmail());
        return dto;
    }
    
    
 // ✅ Get active doctor by ID
    public DoctorDTO getActiveDoctorById(Long id) {
        // 1️⃣ Fetch doctor by ID
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        // 2️⃣ Check if doctor is active
        if (!doctor.isActive()) {
            throw new RuntimeException("Doctor with ID " + id + " is inactive or deleted.");
        }

        // 3️⃣ Convert entity to DTO and return
        return toDTO(doctor);
    }

    
 // ✅ Update existing doctor and sync patient relationships
    @Transactional
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        // 1️⃣ Fetch existing doctor (managed entity)
        // - findById returns a managed entity (attached to the current persistence context).
        // - Working with a managed entity means Hibernate will automatically detect changes
        //   and flush them to the database at commit/save time.
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        // 2️⃣ Update simple scalar fields using ModelMapper
        // - ModelMapper will copy non-null, matching properties from DTO -> entity.
        // - It does NOT know how to map List<Long> -> List<Patient>, so we handle relationships manually.
        // - Make sure your ModelMapper is configured with:
        //     .setMatchingStrategy(MatchingStrategies.STRICT)
        //     .setPropertyCondition(Conditions.isNotNull())
        modelMapper.map(doctorDTO, existingDoctor);

        // 3️⃣ Update doctor ↔ patient relationships (only if patientIds present in request)
        if (doctorDTO.getPatientIds() != null) {
            // 3.a Fetch managed Patient entities for the provided IDs.
            //     We fetch entities (not DTOs) so they are managed by the same persistence context.
            List<Patient> newPatients = patientRepository.findAllById(doctorDTO.getPatientIds());

            // 3.b Clear old relationships using helper method.
            //     This removes all old links (both sides) cleanly without stale join entries.
            existingDoctor.clearPatients();

            // 3.c Add new patients using helper to maintain bidirectional consistency.
            //     For every Patient, the helper ensures both sides are updated.
            for (Patient patient : newPatients) {
                existingDoctor.addPatient(patient); // ✅ maintains bidirectional sync automatically
            }
        }

        
        // get doctor & set email  
        existingDoctor.getUser().setEmail(doctorDTO.getEmail());
//        existingDoctor.getUser().setPassword(doctorDTO.);
        // 4️⃣ Persist updated doctor (owning side)
        // - Because existingDoctor is managed, you could rely on JPA flush at transaction commit,
        //   but calling save() is explicit and OK — it returns the persisted instance.
        // - Since Doctor is the owning side of the many-to-many (has @JoinTable),
        //   saving it will cause Hibernate to insert/delete join table rows as needed.
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);

        // 5️⃣ Convert back to DTO for response
        // - toDTO should map the doctor entity to DoctorDTO and populate patientIds
        //   (e.g. doctor.getPatients().stream().map(Patient::getId).collect(...))
        return toDTO(updatedDoctor);
    }




    // ✅ Soft delete doctor
    public void deactivateDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }
    
    
    // ✅ Upload certificate
    @Override
    public DoctorDTO uploadDoctorCertificate(Long doctorId, MultipartFile file) throws IOException {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        // 1️⃣ Upload to Cloudinary : uplode under the folder doctorscertification
        String folderName = "doctorscertification/" + doctorId;// To keep uploads more organized, group by entity ID:
        String certificateUrl = cloudinaryService.uploadFile(file,folderName);

        // 2️⃣ Set and save
        doctor.setCertificateUrl(certificateUrl);
        Doctor updated = doctorRepository.save(doctor);

        // 3️⃣ Return updated DTO
        return modelMapper.map(updated, DoctorDTO.class);
    }

    
    public DoctorDTO verifyDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setVerified(true);
        return modelMapper.map(doctorRepository.save(doctor), DoctorDTO.class);
    }


    public boolean assignPatientToDoctor(Long doctorId, Long patientId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);

        if (doctorOpt.isPresent() && patientOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            Patient patient = patientOpt.get();

            // add doctor to patient & maintain bidirectional link
            patient.addDoctor(doctor);

            patientRepository.save(patient);
            return true;
        }
        return false;
    }

}
	

