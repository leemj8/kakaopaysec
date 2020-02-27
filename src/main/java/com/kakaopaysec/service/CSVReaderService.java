package com.kakaopaysec.service;

import com.kakaopaysec.model.AccountDTO;
import com.kakaopaysec.model.AccountTrxDTO;
import com.kakaopaysec.model.AccountTrxPK;
import com.kakaopaysec.model.BranDTO;
import com.kakaopaysec.repository.AccountRepository;
import com.kakaopaysec.repository.AccountTransactionRepository;
import com.kakaopaysec.repository.BranRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CSVReaderService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountTransactionRepository accountTransactionRepository;
    @Autowired
    private BranRepository branRepository;

    public int putAccount(){
        int result = 1; //실패 : 0 , 성공 1
        try {
            BufferedReader fileData = new BufferedReader(new FileReader("src/main/resources/data/데이터_계좌정보.csv"));

            while((fileData.readLine()) != null) {
                CSVReader reader = new CSVReader(fileData);
                List<String[]> list = reader.readAll();

                for(String[] s : list) {
                    accountRepository.save(AccountDTO.builder().accNo(s[0]).accNm(s[1]).branCd(s[2]).build());
                }
            }

        } catch (IOException e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    public int putAccountTrx(){
        int result = 1; //실패 : 0 , 성공 1
        try {
            BufferedReader fileData = new BufferedReader(new FileReader("src/main/resources/data/데이터_거래내역.csv"));

            while((fileData.readLine()) != null) {
                CSVReader reader = new CSVReader(fileData);
                List<String[]> list = reader.readAll();

                for(String[] s : list) {
                    accountTransactionRepository.save((AccountTrxDTO.builder().
                            pk(AccountTrxPK.builder().trxDt(s[0]).accNo(s[1]).trxNo(new BigDecimal(s[2])).build())
                            .trxAmt(new BigDecimal(s[3])).fee(new BigDecimal(s[4])).cnclYn(s[5]).build()));
                }
            }

        } catch (IOException e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    public int putBran(){
        int result = 1; //실패 : 0 , 성공 1
        try {
            BufferedReader fileData = new BufferedReader(new FileReader("src/main/resources/data/데이터_관리점정보.csv"));
            while((fileData.readLine()) != null) {
                CSVReader reader = new CSVReader(fileData);
                List<String[]> list = reader.readAll();

                for(String[] s : list) {
                    branRepository.save(BranDTO.builder().branCd(s[0]).branNm(s[1]).build());
                }
            }

        } catch (IOException e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

}
