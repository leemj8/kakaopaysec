package com.kakaopaysec.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
public class TransactionListDTO {

    /* 거래일자 */
    private String trxDt;
    private String year;
    /* 계좌번호 */
    private String accNo;
    /* 합계 */
    @Column(name = "SUM_AMT")
    private BigDecimal sumAmt;
    /* 수수료 */
    private BigDecimal fee;
    /* 취소여부 */
    private String cnclYn;
    /* 계좌명 */
    private String accNm;
    /* 관리점 */
    @Column(name = "BRAN_CD")
    private String branCd;
    /* 관리점명*/
    @Column(name = "BRAN_NM")
    private String branNm;
}
