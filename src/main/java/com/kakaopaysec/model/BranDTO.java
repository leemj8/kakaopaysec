package com.kakaopaysec.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 관리지점 DTO
 */
@Entity
@Builder
@Table(name = "BRAN_INFO")
@AllArgsConstructor()
@NoArgsConstructor

public class BranDTO {
    /* 관리점코드 */
    @Id
    private String branCd;
    /* 관리점명*/
    private String branNm;

    public String getBranCd() {
        return branCd;
    }

    public void setBranCd(String branCd) {
        this.branCd = branCd;
    }

    public String getBranNm() {
        return branNm;
    }

    public void setBranNm(String branNm) {
        this.branNm = branNm;
    }


}
