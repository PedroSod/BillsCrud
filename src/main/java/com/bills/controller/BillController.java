package com.bills.controller;

import com.bills.dto.BillInputDTO;
import com.bills.dto.BillOutputDTO;
import com.bills.model.Bill;
import com.bills.service.BillService;
import com.bills.util.BillConverter;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bill")
public class BillController {

    private BillService billService;
    private BillConverter billConverter;
    private ModelMapper defaultModelMapper;

    public BillController(BillService billService, BillConverter billConverter, ModelMapper defaultModelMapper) {
        this.billService = billService;
        this.billConverter = billConverter;
        this.defaultModelMapper = defaultModelMapper;
    }

    @PostMapping
    public ResponseEntity create(@Validated @RequestBody BillInputDTO billInputDTO) {
        Bill bill = billConverter.convertDtoToEntity(billInputDTO);
        Long id = billService.save(bill).getId();
        URI location = UriComponentsBuilder.fromUriString("bill")
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<BillOutputDTO> getById(@PathVariable Long id) {
        Bill bill = billService.findById(id);
        BillOutputDTO billOutputDTO = defaultModelMapper.map(bill, BillOutputDTO.class);
        return ResponseEntity.ok(billOutputDTO);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id,
                       @Validated @RequestBody BillInputDTO billInputDTO) {
        Bill bill = billConverter.convertDtoToEntity(billInputDTO);
        billService.update(id, bill);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Collection<BillOutputDTO>> getAll() {
        Collection<Bill> billCollection = billService.findAll();
        Collection<BillOutputDTO> billOutputDTOCollection =
                billCollection.stream()
                        .map(role -> defaultModelMapper.map(role, BillOutputDTO.class))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(billOutputDTOCollection);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        billService.delete(id);
    }
}