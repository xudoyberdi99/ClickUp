package com.clickup.payload;

import com.clickup.entity.enums.AddType;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class MemberDto {
    private UUID memberId;

    private String fullName;

    private String email;

    private String roleName;

    private Timestamp lastAvtiveTime;

    private UUID roleId;

    private AddType addType;
}
