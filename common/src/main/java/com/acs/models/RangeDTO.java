package com.acs.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RangeDTO {
    private Long min;
    private Long max;
}
