package in.suraj.moneymanager.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
