package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.IncomeDto;
import in.suraj.moneymanager.entity.Category;
import in.suraj.moneymanager.entity.Income;
import in.suraj.moneymanager.entity.Profile;
import in.suraj.moneymanager.exception.ResourceNotFoundException;
import in.suraj.moneymanager.exception.UnauthorizedException;
import in.suraj.moneymanager.repository.CategoryRepository;
import in.suraj.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryService categoryService;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    public IncomeDto addIncome(IncomeDto dto) throws ResourceNotFoundException {
        Profile profile = profileService.getCurrentProfile();
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Income newIncome = toEntity(dto,profile,category);
        newIncome = incomeRepository.save(newIncome);
        return toDto(newIncome);
    }

    public List<IncomeDto> getCurrentMonthIncomes(){
        Long profileId = profileService.getCurrentProfile().getId();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<Income> incomes = incomeRepository.findByProfileIdAndDateBetween(profileId,startDate,endDate);
        return incomes.stream().map(this::toDto).toList();
    }

    public void deleteIncome(Long incomeId) throws ResourceNotFoundException, UnauthorizedException {
        Long currentProfileId = profileService.getCurrentProfile().getId();
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found!"));
        if(!income.getProfile().getId().equals(currentProfileId)) throw new UnauthorizedException("Unauthorized to delete this income");
        incomeRepository.delete(income);
    }

    //helper methods
    private Income toEntity(IncomeDto dto, Profile profile, Category category){
        return Income.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .date(dto.getDate())
                .amount(dto.getAmount())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDto toDto(Income expense){
        return IncomeDto.builder()
                .id(expense.getId())
                .name(expense.getName())
                .icon(expense.getIcon())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .profileId(expense.getProfile().getId())
                .categoryId(expense.getCategory().getId())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }


}
