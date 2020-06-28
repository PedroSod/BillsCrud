package com.bills.service;

import com.bills.exception.NoBIllsRecordedException;
import com.bills.exception.RecordNotFoundException;
import com.bills.model.Bill;
import com.bills.repository.BillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
public class BillServiceTest {

    @Mock
    private static BillRepository billRepository;

    @InjectMocks
    private static BillService billService;

    private static final Long TEST_ID = 1l;

    @Test
    public void saveTest() {
        Bill billMock = billBuilder();
        when(billRepository.save(eq(billMock))).thenReturn(billMock);
        Bill billReturned = billService.save(billMock);
        assertEquals(billMock, billReturned);
        verify(billRepository).save(eq(billMock));

    }

    @Test
    public void findByIdTest() {
        Bill billMock = billBuilder();
        Optional<Bill> optionalBill = Optional.ofNullable(billMock);
        when(billRepository.findById(eq(TEST_ID))).thenReturn(optionalBill);
        Bill billReturned = billService.findById(TEST_ID);
        assertEquals(billMock, billReturned);
        verify(billRepository).findById(eq(TEST_ID));

    }

    @Test
    public void findByIdRecordNotFoundTest() {
        assertThrows(RecordNotFoundException.class, () ->
                        billService.findById(TEST_ID),
                "No record found for id : " + TEST_ID);
    }

    @Test
    public void updateTest() {
        Bill billToUpdate = billBuilder();
        when(billRepository.existsById(eq(TEST_ID))).thenReturn(true);
        billToUpdate.setId(TEST_ID);
        billToUpdate.setName("test 2");
        when(billRepository.save(eq(billToUpdate))).thenReturn(billToUpdate);
        billService.update(TEST_ID, billToUpdate);
        verify(billRepository).existsById(eq(TEST_ID));
        verify(billRepository).save(eq(billToUpdate));
    }

    @Test
    public void deleteSuccessTest() {
        doNothing().when(billRepository).deleteById(eq(TEST_ID));
        billService.delete(TEST_ID);
        verify(billRepository).deleteById(eq(TEST_ID));
    }

    @Test
    public void updateRecordNotFoundTest() {
        Bill billMock = billBuilder();
        assertThrows(RecordNotFoundException.class, () ->
                        billService.update(TEST_ID, billMock),
                "No record found for id : " + TEST_ID);

    }

    @Test
    public void findAllTest() {
        Bill billMock = billBuilder();
        List<Bill> billCollectionExpected = Collections.singletonList(billMock);
        when(billRepository.findAll()).thenReturn(billCollectionExpected);
        Collection<Bill> billCollectionReturned = billService.findAll();
        assertEquals(billCollectionExpected, billCollectionReturned);
        verify(billRepository).findAll();

    }

    @Test
    public void findAllNoContentTest() {
        List<Bill> billCollectionExpected = Collections.emptyList();
        when(billRepository.findAll()).thenReturn(billCollectionExpected);
        assertThrows(NoBIllsRecordedException.class, () ->
                        billService.findAll(),
                "There is no recorded bill yet.");
        verify(billRepository).findAll();

    }

    private static Bill billBuilder() {
        return Bill.builder()
                .correctedAmount(102.1)
                .originalAmount(100.00)
                .dueDate(LocalDate.now().minusDays(2))
                .paymentDate(LocalDate.now())
                .overDueDays(1)
                .name("test")
                .build();
    }

}