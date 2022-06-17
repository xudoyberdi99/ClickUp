package com.clickup.payload;

import com.clickup.entity.enums.WorkspacePermissionName;
import com.clickup.entity.enums.WorkspaceRoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkspaceRoleDto {

    private UUID id;
    private String name;
    private WorkspaceRoleName extendsRole;
    private WorkspacePermissionName permissionName;
}
