package com.kakaopaysec.controller;

import com.kakaopaysec.service.AccountTransactionService;
import com.kakaopaysec.service.CSVReaderService;
import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CSVReaderService csvReaderService;
    @Autowired
    AccountTransactionService accountTransactionService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
        int a = csvReaderService.putBran();
        int b = csvReaderService.putAccountTrx();
        int c = csvReaderService.putAccount();

    }

    @Test
    void maxSumAmt() throws Exception {
        mockMvc.perform(get("/year/maxSumAmt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void yearNotTrx() throws Exception {
        mockMvc.perform(get("/year/notTrx"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void yearBranSumAmt() throws Exception {
        mockMvc.perform(get("/year/branSumAmt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void branTransfer1() throws Exception {
        mockMvc.perform(get("/bran/transfer?brName=분당점"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void branTransfer2() throws Exception {
        mockMvc.perform(get("/bran/transfer?brName=판교점"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

}