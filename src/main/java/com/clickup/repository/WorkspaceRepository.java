package com.clickup.repository;

import com.clickup.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {
    boolean existsByOwnerIdAndName(UUID owner_id, String name);
}
