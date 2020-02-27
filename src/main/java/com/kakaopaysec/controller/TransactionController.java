package com.kakaopaysec.controller;


import com.kakaopaysec.exception.ExceptionMessage;
import com.kakaopaysec.exception.ServiceException;
import com.kakaopaysec.service.AccountTransactionService;
import com.kakaopaysec.service.CSVReaderService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final AccountTransactionService accountTransactionService;
    private final ExceptionMessage exceptionMessage;
    private final CSVReaderService csvReaderService;


    @GetMapping("/db/csv")
    public void csvDB() throws Exception {

            int a = csvReaderService.putAccount();
            int b = csvReaderService.putAccountTrx();
            int c = csvReaderService.putBran();

            if( a+b+c != 3){
                throw new Exception("db저장오류");
            }
    }

    @GetMapping("/year/maxSumAmt")
    public ResponseEntity maxSumAmt() {
        try {
            return new ResponseEntity<>(accountTransactionService.findByYearMaxSumAmt(), httpHeaders, HttpStatus.OK);
        }catch (ServiceException se){
            return new ResponseEntity<>(exceptionMessage.errMsg(se.getMessage(), String.valueOf(se.getERR_CODE().value())) ,httpHeaders,se.getERR_CODE());
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/year/notTrx")
    public ResponseEntity yearNotTrx(){
        try {
            return new ResponseEntity<>(accountTransactionService.findByNotYearTrx(), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>( Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/year/branSumAmt")
    public ResponseEntity yearBranSumAmt(){
        try {
            return new ResponseEntity<>(accountTransactionService.findByBranTrxSum(), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bran/transfer")
    public ResponseEntity branTransfer(HttpServletRequest request){
        try {
            String brName = StringUtils.trim(request.getParameter("brName"));
            System.out.println("brName"+brName);
            return new ResponseEntity<>(accountTransactionService.findByBranTransfer(brName), httpHeaders, HttpStatus.OK);
        } catch (ServiceException se){
            logger.error("ServiceExcpeiton"+se.toString());
            return new ResponseEntity<>(exceptionMessage.errMsg(se.getMessage(), String.valueOf(se.getERR_CODE().value())) ,httpHeaders,se.getERR_CODE());
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
