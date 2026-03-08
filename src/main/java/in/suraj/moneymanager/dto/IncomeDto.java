package in.suraj.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDto {

    private Long id;
    private String name;
    private String icon;
    private Double amount;
    private LocalDate date;
    private Instant createdAt;
    private Instant updatedAt;
    private Long profileId;
    private Long categoryId;

}
