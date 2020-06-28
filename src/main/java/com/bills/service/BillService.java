package com.bills.service;

import com.bills.exception.NoBIllsRecordedException;
import com.bills.exception.RecordNotFoundException;
import com.bills.model.Bill;
import com.bills.repository.BillRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;


@Service
public class BillService {

    private BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill findById(Long id) {
        return billRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void update(Long id, Bill bill) {
        if (!billRepository.existsById(id)) {
            throw new RecordNotFoundException(id);
        }
        bill.setId(id);
        billRepository.save(bill);
    }

    public Collection<Bill> findAll() {
        Collection<Bill> billCollection = billRepository.findAll();
        if (CollectionUtils.isEmpty(billCollection)) {
            throw new NoBIllsRecordedException();
        }
        return billCollection;
    }

    public void delete(Long id) {
        billRepository.deleteById(id);
    }
}
