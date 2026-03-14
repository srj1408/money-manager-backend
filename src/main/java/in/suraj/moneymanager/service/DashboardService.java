package in.suraj.moneymanager.service;

import in.suraj.moneymanager.dto.ExpenseDto;
import in.suraj.moneymanager.dto.IncomeDto;
import in.suraj.moneymanager.dto.RecentTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static in.suraj.moneymanager.constants.DashboardConstants.*;
import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public Map<String, Object> getDashboardData(){
        Map<String,Object> dashboardData = new LinkedHashMap<>();
        List<RecentTransactionDto> latestIncomes = incomeToRecentTransactionDto(incomeService.getLatest5Incomes()).toList();
        List<RecentTransactionDto> latestExpenses = expenseToRecentTransactionDto(expenseService.getLatest5Expenses()).toList();
        List<RecentTransactionDto> recentTransactions = concat(latestIncomes.stream(),latestExpenses.stream())
                .sorted((a,b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) cmp = b.getCreatedAt().compareTo(a.getCreatedAt());
                    return cmp;
                })
                .toList();
        dashboardData.put(TOTAL_BALANCE, incomeService.getTotalIncome()-expenseService.getTotalExpense());
        dashboardData.put(TOTAL_INCOME, incomeService.getTotalIncome());
        dashboardData.put(TOTAL_EXPENSE, expenseService.getTotalExpense());
        dashboardData.put(RECENT_INCOMES, latestIncomes);
        dashboardData.put(RECENT_EXPENSES, latestExpenses);
        dashboardData.put(RECENT_TRANSACTIONS, recentTransactions);
        return dashboardData;
    }

    private Stream<RecentTransactionDto> incomeToRecentTransactionDto(List<IncomeDto> latestIncomes) {
        return latestIncomes.stream().map(income ->
                RecentTransactionDto.builder()
                        .id(income.getId())
                        .profileId(income.getProfileId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type(INCOME)
                        .build()
                );
    }

    private Stream<RecentTransactionDto> expenseToRecentTransactionDto(List<ExpenseDto> latestExpenses) {
        return latestExpenses.stream().map(expense ->
                RecentTransactionDto.builder()
                        .id(expense.getId())
                        .profileId(expense.getProfileId())
                        .icon(expense.getIcon())
                        .name(expense.getName())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updatedAt(expense.getUpdatedAt())
                        .type(EXPENSE)
                        .build()
        );
    }

}
