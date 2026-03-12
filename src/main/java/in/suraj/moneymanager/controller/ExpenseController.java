package in.suraj.moneymanager.controller;

import in.suraj.moneymanager.dto.ExpenseDto;
import in.suraj.moneymanager.exception.ResourceNotFoundException;
import in.suraj.moneymanager.exception.UnauthorizedException;
import in.suraj.moneymanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static in.suraj.moneymanager.constants.UrlConstants.EXPENSE;

@RestController
@RequiredArgsConstructor
@RequestMapping(EXPENSE)
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto dto) throws ResourceNotFoundException {
        ExpenseDto savedExpense = expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getExpenses(){
        List<ExpenseDto> expenses =  expenseService.getCurrentMonthExpenses();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) throws UnauthorizedException, ResourceNotFoundException {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
