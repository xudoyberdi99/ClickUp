package com.clickup.entity;

import com.clickup.entity.enums.WorkspacePermissionName;
import com.clickup.entity.template.AbsUUIDEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class WorkspacePermission extends AbsUUIDEntity {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private WorkspaceRole workspaceRole;//urinbosar

    @Enumerated(EnumType.STRING)
    private WorkspacePermissionName permissionName;//addMember,remove
}
