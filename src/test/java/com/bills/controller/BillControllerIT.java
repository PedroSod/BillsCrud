package com.bills.controller;

import com.bills.configuration.ApplicationConfig;
import com.bills.dto.BillInputDTO;
import com.bills.exception.BillInterestRulesNotFoundException;
import com.bills.exception.NoBIllsRecordedException;
import com.bills.exception.RecordNotFoundException;
import com.bills.model.Bill;
import com.bills.service.BillService;
import com.bills.util.BillConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BillController.class)
@Import(ApplicationConfig.class)
public class BillControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillConverter billConverter;

    @MockBean
    private BillService billService;

    private static ObjectMapper mapper;

    private final static Long overDueDays = 1l;

    private static final Long TEST_ID = 1l;

    private final static String endpoint = "/bill";

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void createTest() throws Exception {
        Bill billMock = billBuilder();
        BillInputDTO billInputDTO = billInputDTOBuilder();
        when(billConverter.convertDtoToEntity(billInputDTO)).thenReturn(billMock);
        when(billService.save(eq(billMock))).thenReturn(billMock);
        mockMvc.perform(post(endpoint)
                .content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createNameBadRequestTest() throws Exception {
        BillInputDTO billInputDTO = billInputDTOBuilder();
        billInputDTO.setName(null);
        mockMvc.perform(post(endpoint)
                .content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors[0]").value("name - must not be blank"));
    }

    @Test
    public void createOriginalAmountBadRequestTest() throws Exception {
        BillInputDTO billInputDTO = billInputDTOBuilder();
        billInputDTO.setOriginalAmount(null);
        mockMvc.perform(post(endpoint)
                .content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors[0]").value("originalAmount - must not be null"));
    }

    @Test
    public void createPaymentDateBadRequestTest() throws Exception {
        BillInputDTO billInputDTO = billInputDTOBuilder();
        billInputDTO.setPaymentDate(null);
        mockMvc.perform(post(endpoint)
                .content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.fieldErrors[0]").value("paymentDate - must not be null"));
    }


    @Test
    public void createNoBIllsRecordedExceptionTest() throws Exception {
        BillInputDTO billInputDTO = billInputDTOBuilder();
        when(billConverter.convertDtoToEntity(eq(billInputDTO)))
                .thenThrow(new BillInterestRulesNotFoundException());

        mockMvc.perform(post(endpoint).content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Interest bill rule not found."));
    }

    @Test
    public void getById() throws Exception {
        Bill billMock = billBuilder();
        when(billService.findById(eq(TEST_ID))).thenReturn(billMock);
        mockMvc.perform(
                get((String.format("%s/%s", endpoint, TEST_ID))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(billMock.getName()))
                .andExpect(jsonPath("$.originalAmount").value(billMock.getOriginalAmount()))
                .andExpect(jsonPath("$.correctedAmount").value(billMock.getCorrectedAmount()))
                .andExpect(jsonPath("$.overDueDays").value(billMock.getOverDueDays()))
                .andExpect(jsonPath("$.paymentDate").value(billMock.getPaymentDate().toString()));
    }

    @Test
    public void getByIdRecordNotFoundTest() throws Exception {
        when(billService.findById(eq(TEST_ID))).thenThrow(new RecordNotFoundException(TEST_ID));
        mockMvc.perform(
                get((String.format("%s/%s", endpoint, TEST_ID))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No entity with id " + TEST_ID + " exists!"));
    }

    @Test
    public void updateTest() throws Exception {
        Bill billMock = billBuilder();
        BillInputDTO billInputDTO = billInputDTOBuilder();
        when(billConverter.convertDtoToEntity(billInputDTO)).thenReturn(billMock);
        doNothing().when(billService).update(eq(TEST_ID), eq(billMock));

        mockMvc.perform(put((String.format("%s/%s", endpoint, TEST_ID)))
                .content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateNotFoundTest() throws Exception {
        Bill billMock = billBuilder();
        BillInputDTO billInputDTO = billInputDTOBuilder();
        when(billConverter.convertDtoToEntity(billInputDTO)).thenReturn(billMock);
        doThrow(new RecordNotFoundException(TEST_ID)).when(billService).update(eq(TEST_ID), eq(billMock));

        mockMvc.perform(put((String.format("%s/%s", endpoint, TEST_ID)))
                .content(mapper.writeValueAsString(billInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No entity with id " + TEST_ID + " exists!"));
    }

    @Test
    public void getAllTest() throws Exception {
        Bill billMock = billBuilder();
        Collection<Bill> billCollection = Collections.singletonList(billMock);
        when(billService.findAll()).thenReturn(billCollection);
        mockMvc.perform(
                get(endpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(billMock.getName()))
                .andExpect(jsonPath("$[0].originalAmount").value(billMock.getOriginalAmount()))
                .andExpect(jsonPath("$[0].correctedAmount").value(billMock.getCorrectedAmount()))
                .andExpect(jsonPath("$[0].overDueDays").value(billMock.getOverDueDays()))
                .andExpect(jsonPath("$[0].paymentDate").value(billMock.getPaymentDate().toString()));
    }

    @Test
    public void getAllNoContent() throws Exception {
        when(billService.findAll()).thenThrow(new NoBIllsRecordedException());
        mockMvc.perform(
                get(endpoint))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.errorCode").value(204))
                .andExpect(jsonPath("$.error").value("NO_CONTENT"))
                .andExpect(jsonPath("$.message").value("There is no recorded bill yet."));
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete((String.format("%s/%s", endpoint, TEST_ID))))
                .andExpect(status().isNoContent());
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

}