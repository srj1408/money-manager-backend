package in.suraj.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentTransactionDto {

    private Long id;
    private Long profileId;
    private String icon;
    private String name;
    private Double amount;
    private LocalDate date;
    private Instant createdAt;
    private Instant updatedAt;
    private String type;

}
