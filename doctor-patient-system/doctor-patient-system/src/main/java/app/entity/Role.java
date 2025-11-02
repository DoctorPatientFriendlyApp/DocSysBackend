package app.entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Role of User")
public enum Role {
    ADMIN, DOCTOR, PATIENT
}

