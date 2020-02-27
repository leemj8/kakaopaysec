package com.kakaopaysec.controller;

import com.kakaopaysec.model.AccountDTO;
import com.kakaopaysec.model.AccountTrxDTO;
import com.kakaopaysec.model.AccountTrxPK;
import com.kakaopaysec.model.BranDTO;
import com.kakaopaysec.repository.AccountRepository;
import com.kakaopaysec.repository.AccountTransactionRepository;
import com.kakaopaysec.repository.BranRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
public class CsvController {
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final BranRepository branRepository;
    private static final HttpHeaders httpHeaders = new HttpHeaders();

    @PostMapping(value = "/csv/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) {
        Reader reader;
        try {

            reader = new InputStreamReader(multipartFile.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] s;
            while ((s = csvReader.readNext()) != null) {

                if("데이터_계좌정보.csv".equals(multipartFile.getOriginalFilename())){
                    accountRepository.save(AccountDTO.builder().accNo(s[0]).accNm(s[1]).branCd(s[2]).build());
                }else if("데이터_거래내역.csv".equals(multipartFile.getOriginalFilename())){
                    accountTransactionRepository.save((AccountTrxDTO.builder().
                            pk(AccountTrxPK.builder().trxDt(s[0]).accNo(s[1]).trxNo(new BigDecimal(s[2])).build())
                            .trxAmt(new BigDecimal(s[3])).fee(new BigDecimal(s[4])).cnclYn(s[5]).build()));
                }else if("데이터_관리점정보.csv".equals(multipartFile.getOriginalFilename())){
                    branRepository.save(BranDTO.builder().branCd(s[0]).branNm(s[1]).build());
                }else{
                    return new ResponseEntity<>(Collections.singletonMap("error", "파일형식이 옳바르지 않습니다."), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(Collections.singletonMap("success", "파일업로드 성공"), httpHeaders, HttpStatus.OK);
    }


}
