package com.kakaopaysec.model;

import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;

/**
 * 데이터_거래내역 DTO
 */

@Entity
@Builder
@Table(name = "ACCOUNT_TRANSACTION")
@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
public class AccountTrxDTO {

    @EmbeddedId
    private AccountTrxPK pk;
    /* 금액 */
    @Column(precision = 13, scale = 0)
    private BigDecimal trxAmt;
    /* 수수료 */
    @Column(precision = 13, scale = 0)
    private BigDecimal fee;
    /* 취소여부 */
    @Column
    private String cnclYn;

}
