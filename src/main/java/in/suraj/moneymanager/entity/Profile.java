package in.suraj.moneymanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;
    private String password;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_ts", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_ts")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    private Boolean isActive;
    private String activationToken;

    @PrePersist
    public void prePersist(){
        if(this.isActive == null) isActive = false;
    }
}
