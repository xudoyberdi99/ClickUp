package com.clickup.repository;

import com.clickup.entity.WorkspacePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspacePermissionRepository extends JpaRepository<WorkspacePermission, UUID> {
}
