package com.kakaopaysec.model;



import lombok.*;

import javax.persistence.*;

/**
 * 데이터_계좌정보 DTO
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor()
@Table(name = "ACCOUNT_INFO")
public class AccountDTO {
    /* 계좌번호 */
    @Id
    private String accNo;
    /* 계좌명 */
    private String accNm;
    /* 관리점 */
    private String branCd;


}
