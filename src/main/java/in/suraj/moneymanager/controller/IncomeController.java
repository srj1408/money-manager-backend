package in.suraj.moneymanager.controller;

import in.suraj.moneymanager.dto.IncomeDto;
import in.suraj.moneymanager.exception.ResourceNotFoundException;
import in.suraj.moneymanager.exception.UnauthorizedException;
import in.suraj.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static in.suraj.moneymanager.constants.UrlConstants.INCOME;

@RestController
@RequestMapping(INCOME)
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping()
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto dto) throws ResourceNotFoundException {
        IncomeDto savedIncome = incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIncome);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getIncomes(){
        List<IncomeDto> incomes =  incomeService.getCurrentMonthIncomes();
        return ResponseEntity.ok(incomes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) throws UnauthorizedException, ResourceNotFoundException {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
