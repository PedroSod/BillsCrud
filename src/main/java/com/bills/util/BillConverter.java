package com.bills.util;

import com.bills.dto.BillInputDTO;
import com.bills.model.Bill;
import com.bills.model.BillsInterestRules;
import com.bills.service.BillInterestRulesService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class BillConverter {
    private ModelMapper defaultModelMapper;
    private BillInterestRulesService billsInterestRulesService;
    private final static Long DEFAULT_OVER_DUE_DAYS = 0l;

    public BillConverter(ModelMapper defaultModelMapper, BillInterestRulesService billsInterestRulesService) {
        this.defaultModelMapper = defaultModelMapper;
        this.billsInterestRulesService = billsInterestRulesService;
    }

    public Bill convertDtoToEntity(BillInputDTO billDTO) {
        Bill bill = defaultModelMapper.map(billDTO, Bill.class);
        bill.setOverDueDays(calcOverDueDays(billDTO.getDueDate(), bill.getPaymentDate()));
        bill.setCorrectedAmount(calcCorrectedAmount(bill.getOriginalAmount(), bill.getOverDueDays()));
        return bill;
    }

    private Long calcOverDueDays(LocalDate dueDate, LocalDate paymentDate) {
        Long overDueDays = DEFAULT_OVER_DUE_DAYS;
        if (paymentDate.isAfter(dueDate)) {
            overDueDays = DAYS.between(dueDate, paymentDate);
        }
        return overDueDays;
    }

    private Double calcCorrectedAmount(Double originalAmount, Long overDueDays) {
        Double correctedAmount = originalAmount;
        if (hasOverDueDays(overDueDays)) {
            correctedAmount = calcFineBill(originalAmount, overDueDays);
        }
        return correctedAmount;
    }

    private Double calcFineBill(Double originalAmount, Long overDueDays) {
        Double correctedAmount = originalAmount;
        Double percentage = getTotalPercentage(overDueDays);
        correctedAmount += originalAmount * percentage;
        return correctedAmount;
    }

    private Double getTotalPercentage(Long overDueDays) {
        BillsInterestRules billsInterestRules = billsInterestRulesService.findBillsInterestRules(overDueDays);
        Double totalPercentage = convertPercentage(billsInterestRules.getFineBill()) + getInterestBillPercentage(overDueDays, billsInterestRules);
        return totalPercentage;
    }

    private Double getInterestBillPercentage(Long overDueDays, BillsInterestRules billsInterestRules) {
        Double interestPercentage = overDueDays * (convertPercentage(billsInterestRules.getInterestOnDayOverdue()));
        return interestPercentage;
    }

    private Double convertPercentage(Double value) {
        return value / 100;
    }

    private boolean hasOverDueDays(Long overDueDays) {
        return overDueDays > 0;
    }
}
