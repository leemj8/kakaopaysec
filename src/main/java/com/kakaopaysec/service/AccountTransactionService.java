package com.kakaopaysec.service;

import com.kakaopaysec.controller.TransactionController;
import com.kakaopaysec.exception.ServiceException;
import com.kakaopaysec.model.BranDTO;
import com.kakaopaysec.model.TransactionListDTO;
import com.kakaopaysec.repository.AccountTransactionRepository;
import com.kakaopaysec.repository.BranRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final AccountTransactionRepository accountTransactionRepository;
    private final BranRepository branRepository;

    public List<Map<String, Object>> findByYearMaxSumAmt(){

        List<Map<String, Object>> maxSumAmtList = new ArrayList<Map<String, Object>>();
        try {
            for (Map<String, Object> acc : accountTransactionRepository.findByYearMaxAccSumAmt()) { //년도별 고객 합계금액
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                for (Map<String, Object> year : accountTransactionRepository.findByYearMaxSumAmt()) { //년도별 최대금액
                    if (year.size() == 0) {
                        throw new ServiceException("데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
                    }
                    if (year.get("YEAR").equals(acc.get("YEAR"))
                            && year.get("MAX_AMT").equals(acc.get("SUM_AMT"))) { //년도별 고객별 금액과 년도별 최대금액 비교하여 List 추가
                        row.put("year", acc.get("YEAR"));
                        row.put("name", acc.get("ACC_NM"));
                        row.put("accNo", acc.get("ACC_NO"));
                        row.put("sumAmt", acc.get("SUM_AMT"));
                        maxSumAmtList.add(row);
                    }
                }
            }
        }catch(ServiceException se){
            throw se;
        }catch(Exception e){
            throw e;
        }

        return maxSumAmtList;
    }

    public List<Map<String, Object>> findByNotYearTrx(){
        return accountTransactionRepository.findByNotYearTrx();
    }


    public List<Map<String, Object>> findByBranTrxSum() {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try{
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            Map<String, List> year = new LinkedHashMap<String, List>(); //쿼리 순서(년도별)을 위한 LinkedHashMap 사용
            for (Map<String, Object> bran : accountTransactionRepository.findByYearBranSumTrx()) {
                //년도 key , 데이터리스트 value로 list add
                List dataList = year.get(bran.get("YEAR"));
                if(dataList == null){
                    dataList = new ArrayList<LinkedHashMap<String, List>>();
                    year.put((String) bran.get("YEAR"), dataList);
                }

                Map<String, String> branMap = new LinkedHashMap<String, String>();
                branMap.put("brNm", (String) bran.get("BRAN_NM"));
                branMap.put("brNo", (String) bran.get("BRAN_CD"));
                branMap.put("trxAmt", bran.get("SUM_AMT").toString());
                dataList.add(branMap);

            }

            Iterator<Map.Entry<String, List>> keys = year.entrySet().iterator();
            while (keys.hasNext()){
                Map<String, Object> dto = new LinkedHashMap<String, Object>();
                Map.Entry<String, List> key = keys.next();
                dto.put("year", key.getKey());
                dto.put("dataList", key.getValue());
                resultList.add(dto);
            }

        }catch (Exception e){
            logger.error(e.toString());
            throw e;
        }

        return resultList;
    }


    public Map<String, Object> findByBranTransfer(String brName) {
        //분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        try{
            List<String> brNameList = transferBranName(brName);
            System.out.println("지점카운트"+brNameList.size());
            if(brNameList.size() == 0){
                throw new ServiceException("br code not found error", HttpStatus.NOT_FOUND);
            }

            BigDecimal trxAmt = BigDecimal.ZERO;
            Map<String, Object> branInfo =  accountTransactionRepository.findByBranSumTrx(brName);
            result.put("brName", branInfo.get("BRAN_NM"));
            result.put("brCode", branInfo.get("BRAN_CD"));
            if(brNameList.size() > 1){ //이관된 지점이 포함된 경우
                for(String brNm : brNameList){
                    trxAmt = trxAmt.add((BigDecimal) accountTransactionRepository.findByBranSumTrx(brNm).get("SUM_AMT"));
                }
                result.put("sumAmt", trxAmt);
            }else{
                result.put("sumAmt", branInfo.get("SUM_AMT"));
            }
        }catch (ServiceException se){
            throw se;
        }
        catch(Exception e){
            logger.error(e.toString());
            throw e;
        }

        return result;
    }


    private List<String> transferBranName(String brName){
        /*
        지점이관은 공통테이블이나 관리점테이블에 이관 시 코드를 관리하는게 좋지만 그렇지 않을경우 대비하여 이관유무확인해서 return
        이관된 지점명(또는 지점코드)을 추가로 맵에 넣어 관리
        */
        List<Map<String, String>> branList = new ArrayList<Map<String, String>>();
        List<String> transCdList = new ArrayList<String>();

        try{
            for (BranDTO bran : branRepository.findAll()) {
                Map<String, String> branInfo = new HashMap<String, String>();
                branInfo.put("brName", bran.getBranNm());
                branInfo.put("brCode", bran.getBranCd());
                if("B".equals(bran.getBranCd())){
                    branInfo.put("transferName", "판교점"); // 분당점인경우 판교점으로 이관했다고 코드를 map에 put
                }else{
                    branInfo.put("transferName", bran.getBranNm()); // 이관한적이 없을경우 자기자신을 넣어둠.
                }
                branList.add(branInfo);
            }

            Iterator<Map<String, String>> br = branList.iterator();

            while(br.hasNext()){
                Map<String, String> brDTO = br.next();
                if(brDTO.get("transferName").equals(brName)){ // b
                    transCdList.add(brDTO.get("transferName"));
                }
            }
        }catch (Exception e){
            logger.error(e.toString());
            throw e;
        }

        return transCdList;
    }

}
