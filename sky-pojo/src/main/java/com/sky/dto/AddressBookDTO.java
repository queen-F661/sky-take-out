package com.sky.dto;

import lombok.Data;

@Data
public class AddressBookDTO {
    private Long id;
    private String rejectionReason;
    private String cancelReason;
}
