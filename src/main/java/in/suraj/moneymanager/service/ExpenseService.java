package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.ExpenseDto;
import in.suraj.moneymanager.entity.Category;
import in.suraj.moneymanager.entity.Expense;
import in.suraj.moneymanager.entity.Profile;
import in.suraj.moneymanager.exception.ResourceNotFoundException;
import in.suraj.moneymanager.exception.UnauthorizedException;
import in.suraj.moneymanager.repository.CategoryRepository;
import in.suraj.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    public ExpenseDto addExpense(ExpenseDto dto) throws ResourceNotFoundException {
        Profile profile = profileService.getCurrentProfile();
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Expense newExpense = toEntity(dto,profile,category);
        newExpense = expenseRepository.save(newExpense);
        return toDto(newExpense);
    }

    public List<ExpenseDto> getCurrentMonthExpenses(){
        Long profileId = profileService.getCurrentProfile().getId();
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<Expense> expenses = expenseRepository.findByProfileIdAndDateBetween(profileId,startDate,endDate);
        return expenses.stream().map(this::toDto).toList();
    }

    public void deleteExpense(Long expenseId) throws ResourceNotFoundException, UnauthorizedException {
        Long currentProfileId = profileService.getCurrentProfile().getId();
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found!"));
        if(!expense.getProfile().getId().equals(currentProfileId)) throw new UnauthorizedException("Unauthorized to delete this expense");
        expenseRepository.delete(expense);
    }

    //helper methods
    private Expense toEntity(ExpenseDto dto, Profile profile, Category category){
        return Expense.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .date(dto.getDate())
                .amount(dto.getAmount())
                .profile(profile)
                .category(category)
                .build();
    }

    private ExpenseDto toDto(Expense expense){
        return ExpenseDto.builder()
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
