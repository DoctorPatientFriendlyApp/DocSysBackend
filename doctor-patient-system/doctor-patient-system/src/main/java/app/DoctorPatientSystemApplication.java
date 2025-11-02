package app;

import org.modelmapper.Conditions;
import java.time.LocalDate;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import app.dto.PatientDTO;
import app.entity.Patient;

@SpringBootApplication
public class DoctorPatientSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorPatientSystemApplication.class, args);
		System.out.println("🚀 Doctor-Patient System Started Successfully!");
	}
	
//	ModelMapper can map nested properties properly
	@Bean
	public ModelMapper modelMapper() {
	    System.out.println("🧩 Initializing ModelMapper configuration...");

	    ModelMapper mapper = new ModelMapper();

	    mapper.getConfiguration()
	            .setMatchingStrategy(MatchingStrategies.STRICT)              // exact field name matching
	            .setFieldMatchingEnabled(true)                               // allow private field mapping
	            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // map directly to fields
	            .setSkipNullEnabled(true)                                    // ignore nulls on update
	            .setDeepCopyEnabled(true);                                   // allow nested (deep) copying

	    // ✅ Custom converter for LocalDate ↔ LocalDate
	    Converter<LocalDate, LocalDate> localDateConverter = new Converter<>() {
	        @Override
	        public LocalDate convert(MappingContext<LocalDate, LocalDate> context) {
	            return context.getSource() == null ? null : context.getSource();
	        }
	    };

	    mapper.addConverter(localDateConverter);
	    
//	    // ✅ Skip reports mapping to prevent overwrite of existing reports
	    // Skip relational collections to avoid overwriting Hibernate-managed lists
	    mapper.typeMap(PatientDTO.class, Patient.class)
	          .addMappings(m -> {
	              m.skip(Patient::setReports);
	              m.skip(Patient::setTreatments);
	          });
	    
	    return mapper;
	}


//	// ✅ ModelMapper Bean for entity ↔ DTO conversion
//	@Bean
//	public ModelMapper modelMapper() {
//		System.out.println("🧩 Initializing ModelMapper configuration...");
//		ModelMapper mapper = new ModelMapper();
//
//		mapper.getConfiguration()
//				.setMatchingStrategy(MatchingStrategies.STRICT)      // Only map properties that match exactly
//				.setPropertyCondition(Conditions.isNotNull());       // Skip null fields when mapping
//
//		return mapper;
//	}

	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

}
