package in.suraj.moneymanager.repository;

import in.suraj.moneymanager.entity.Expense;
import in.suraj.moneymanager.entity.Income;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Expense> findByProfileIdOrderByDateDesc(Long profileId);

    List<Expense> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.profile.id = :profileId")
    Double findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<Expense> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

    List<Expense> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

}
