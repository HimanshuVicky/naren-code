package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class SecuritySessionBean implements Serializable {
    private Double id;
    private String sessionId;
    private LocalDate dateCreated;
    private Long userId;
    private Integer validityPeriod;
}
