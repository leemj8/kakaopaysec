package com.kakaopaysec.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountTrxPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column
    private String trxDt;
    @Column
    private String accNo;
    @Column(precision = 13, scale = 0)
    @GeneratedValue
    private BigDecimal trxNo;


}
