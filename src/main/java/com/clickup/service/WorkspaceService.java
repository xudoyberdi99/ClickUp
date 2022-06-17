package com.clickup.service;

import com.clickup.entity.User;
import com.clickup.payload.ApiResponse;
import com.clickup.payload.MemberDto;
import com.clickup.payload.WorkspaceDto;
import com.clickup.payload.WorkspaceRoleDto;

import java.util.List;
import java.util.UUID;


public interface WorkspaceService {

    ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user);

    ApiResponse editWorkspace(Long id, WorkspaceDto workspaceDto);

    ApiResponse changeOwnerWorkspace(Long id, UUID ownerId);

    ApiResponse deleteWorkspace(Long id);

    ApiResponse workspaceMemberAddorEditorRemove(Long id, MemberDto memberDto);

    ApiResponse joinToWorkspace(Long id, User user);

    List<MemberDto> getMemberAndGuest(Long id);

    List<WorkspaceDto> getMyWorkspace(User user);

    ApiResponse addOrRemovePermission(WorkspaceRoleDto workspaceRoleDto);

    ApiResponse addRole(Long workspaceId, WorkspaceRoleDto workspaceRoleDto, User user);

}
