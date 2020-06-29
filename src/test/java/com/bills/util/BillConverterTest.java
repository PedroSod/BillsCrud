package com.bills.util;


import com.bills.dto.BillInputDTO;
import com.bills.model.Bill;
import com.bills.model.BillsInterestRules;
import com.bills.service.BillInterestRulesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BillConverterTest {


    private static BillInterestRulesService billsInterestRulesService;

    private static BillConverter billConverter;

    private final static Long overDueDays = 1l;

    private static BillsInterestRules billsInterestRulesMock;

    @BeforeAll
    public static void setUp() {
        billsInterestRulesMock = billsInterestRulesBuilder();
        billsInterestRulesService = Mockito.mock(BillInterestRulesService.class);
        billConverter = new BillConverter(new ModelMapper(), billsInterestRulesService);
    }

    @Test
    public void convertDtoToEntityTest() {
        BillInputDTO billInputDTO = billInputDTOBuilder();
        Bill billMock = billBuilder();
        when(billsInterestRulesService.findBillsInterestRules(eq(overDueDays))).thenReturn(billsInterestRulesMock);
        Bill billReturned = billConverter.convertDtoToEntity(billInputDTO);
        assertEquals(billMock, billReturned);
    }

    private static BillInputDTO billInputDTOBuilder() {
        return BillInputDTO.builder()
                .originalAmount(100.00)
                .dueDate(LocalDate.now().minusDays(overDueDays))
                .paymentDate(LocalDate.now())
                .name("test")
                .build();
    }

    private static Bill billBuilder() {
        return Bill.builder()
                .correctedAmount(102.1)
                .originalAmount(100.00)
                .dueDate(LocalDate.now().minusDays(overDueDays))
                .paymentDate(LocalDate.now())
                .overDueDays(overDueDays)
                .name("test")
                .build();
    }

    private static BillsInterestRules billsInterestRulesBuilder() {
        return BillsInterestRules.builder()
                .id(1L)
                .fineBill(2.0)
                .interestOnDayOverdue(0.1)
                .underDayLimit(1l)
                .overDayLimit(3l)
                .build();
    }
}