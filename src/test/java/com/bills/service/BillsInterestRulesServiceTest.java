package com.bills.service;

import com.bills.exception.BillInterestRulesNotFoundException;
import com.bills.model.BillsInterestRules;
import com.bills.repository.BillsInterestRulesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BillsInterestRulesServiceTest {

    @Mock
    private BillsInterestRulesRepository billsInterestRulesRepository;

    @InjectMocks
    private BillInterestRulesService billsInterestRulesService;

    private static Integer overDueDays = 1;

    @Test
    public void findBillsInterestRulesTest() {
        BillsInterestRules billsInterestRulesMock = billsInterestRulesBuilder();
        Optional<BillsInterestRules> optionalBillsInterestRules = Optional.ofNullable(billsInterestRulesMock);
        when(billsInterestRulesRepository.findByUnderDayLimitLessThanEqualAndOverDayLimitGreaterThanEqual(eq(overDueDays), eq(overDueDays)))
                .thenReturn(optionalBillsInterestRules);
        BillsInterestRules billsInterestRulesReturned = billsInterestRulesService.findBillsInterestRules(overDueDays);
        assertEquals(billsInterestRulesMock, billsInterestRulesReturned);
        verify(billsInterestRulesRepository).findByUnderDayLimitLessThanEqualAndOverDayLimitGreaterThanEqual(eq(overDueDays), eq(overDueDays));
    }

    @Test
    public void findBillsInterestRulesTestException() {
        assertThrows(BillInterestRulesNotFoundException.class, () ->
                        billsInterestRulesService.findBillsInterestRules(overDueDays),
                "Interest bill rule not found.");
    }

    private static BillsInterestRules billsInterestRulesBuilder() {
        return BillsInterestRules.builder()
                .id(1L)
                .fineBill(2.0)
                .interestOnDayOverdue(0.1)
                .underDayLimit(1)
                .overDayLimit(3)
                .build();
    }

}