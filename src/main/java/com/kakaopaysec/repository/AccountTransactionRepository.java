package com.kakaopaysec.repository;

import com.kakaopaysec.model.AccountDTO;
import com.kakaopaysec.model.AccountTrxDTO;
import com.kakaopaysec.model.TransactionListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTrxDTO, Long> {

    @Query(value = "SELECT YEAR, ACC_NO, (SELECT ACC_NM FROM ACCOUNT_INFO WHERE ACCTRX.ACC_NO = ACC_NO) AS ACC_NM, SUM(SUM_AMT) AS SUM_AMT \n" +
                     "FROM (\n" +
                        "SELECT SUBSTR(TRX_DT, 0,4) YEAR\n" +
                            "    ,ACC_NO\n" +
                                ",(TRX_AMT - FEE) SUM_AMT\n" +
                        " FROM ACCOUNT_TRANSACTION\n" +
                        "WHERE CNCL_YN = 'N' \n" +
                            ") AS ACCTRX\n" +
                    "WHERE YEAR IN ('2018', '2019') GROUP BY YEAR, ACC_NO"
            , nativeQuery = true)
    //고객(계좌)별 년도별 합계금액(거래금액 - 수수료) , 취소거래 제외
    List<Map<String, Object>> findByYearMaxAccSumAmt();

    @Query(value = "SELECT YEAR, max(SUM_AMT) AS MAX_AMT " +
            "FROM (\n" +
            "SELECT SUBSTR(TRX_DT, 0,4) YEAR,  ACC_NO, SUM(TRX_AMT - FEE) SUM_AMT " +
            "FROM ACCOUNT_TRANSACTION WHERE CNCL_YN = 'N' GROUP BY YEAR,ACC_NO)\n" +
            "GROUP By YEAR"
            , nativeQuery = true)
    //년도별 최대 합계금액, 취소거래 포함
    List<Map<String, Object>> findByYearMaxSumAmt();


    @Query(value = "SELECT ACC.YEAR AS year, ACC.ACC_NM AS name , ACC.ACC_NO AS accNo FROM ( \n" +
                        "SELECT *\n" +
                        "FROM ACCOUNT_INFO CROSS JOIN \n" +
                        "(SELECT SUBSTR(TRX_DT, 0 ,4 ) YEAR FROM ACCOUNT_TRANSACTION GROUP BY YEAR) ) ACC LEFT OUTER JOIN \n" +
                        "(SELECT SUBSTR(TRX_DT,0,4) YEAR, ACC_NO, SUM(TRX_AMT - FEE) TRX_AMT " +
                            "FROM ACCOUNT_TRANSACTION " +
                            "GROUP BY YEAR, ACC_NO) ACCTRX ON ACC.ACC_NO = ACCTRX.ACC_NO AND ACC.YEAR = ACCTRX.YEAR\n" +
                    "WHERE ACC.YEAR IN ('2018', '2019') " +
                    "AND TRX_AMT IS NULL"
            , nativeQuery = true)

    //2018, 2019 거래내역 없는 고객 , 취소거래 제외
    List<Map<String, Object>> findByNotYearTrx();

    @Query(value = "SELECT YEAR, (SELECT BRAN_NM FROM BRAN_INFO WHERE BRAN_CD = SUMBRAN.BRAN_CD) BRAN_NM, BRAN_CD, SUM_AMT \n" +
                    "FROM (\n" +
                        "SELECT SUBSTR(TRX_DT, 0, 4) YEAR, BRAN_CD, SUM(TRX_AMT - FEE) SUM_AMT\n" +
                        "FROM ACCOUNT_INFO ACC JOIN ACCOUNT_TRANSACTION ACCTRX ON ACC.ACC_NO = ACCTRX.ACC_NO\n" +
                        "WHERE CNCL_YN = 'N'\n " +
                        "GROUP BY YEAR, BRAN_CD\n" +
                        ")SUMBRAN\n" +
                    "ORDER BY YEAR, SUM_AMT DESC"
    , nativeQuery =  true)
    //년도별 관리점별 거래내역
    List<Map<String, Object>> findByYearBranSumTrx();


    @Query(value = "SELECT BRAN_NM, BRAN_CD, SUM_AMT \n" +
            "FROM (\n" +
            "SELECT  BRAN_CD, SUM(TRX_AMT - FEE) SUM_AMT, (SELECT BRAN_NM FROM BRAN_INFO WHERE BRAN_CD = ACC.BRAN_CD) BRAN_NM\n" +
            "FROM ACCOUNT_INFO ACC JOIN ACCOUNT_TRANSACTION ACCTRX ON ACC.ACC_NO = ACCTRX.ACC_NO\n" +
            "WHERE CNCL_YN = 'N'\n" +
            " GROUP BY BRAN_CD\n" +
            ")\n" +
            "WHERE BRAN_NM = ?1 "
            , nativeQuery =  true)
    //관리점별 거래내역 합계
    //List<Map<String, Object>> findByBranSumTrx(String brName);
    Map<String, Object> findByBranSumTrx(String brName);



}

