package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.ExpenseDto;
import in.suraj.moneymanager.entity.Category;
import in.suraj.moneymanager.entity.Expense;
import in.suraj.moneymanager.entity.Profile;
import in.suraj.moneymanager.exception.ResourceNotFoundException;
import in.suraj.moneymanager.repository.CategoryRepository;
import in.suraj.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
