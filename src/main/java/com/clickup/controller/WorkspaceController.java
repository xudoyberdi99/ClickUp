package com.clickup.controller;

import com.clickup.entity.User;
import com.clickup.entity.Workspace;
import com.clickup.payload.ApiResponse;
import com.clickup.payload.MemberDto;
import com.clickup.payload.WorkspaceDto;
import com.clickup.payload.WorkspaceRoleDto;
import com.clickup.security.CurrentUser;
import com.clickup.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    public HttpEntity<?> addWorkspace(@Valid  @RequestBody WorkspaceDto workspaceDto, @CurrentUser User user){
        ApiResponse apiResponse=workspaceService.addWorkspace(workspaceDto,user);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }
    @PutMapping("/{id}")
    public HttpEntity<?> editWorkspace(@PathVariable Long id,@RequestBody WorkspaceDto workspaceDto){
        ApiResponse apiResponse=workspaceService.editWorkspace(id,workspaceDto);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }
    @PutMapping("/changeOwner/{id}")
    public HttpEntity<?> changeOwnerWorkspace(@PathVariable Long id,@RequestParam UUID ownerId){
        ApiResponse apiResponse=workspaceService.changeOwnerWorkspace(id,ownerId);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWorkspace(@PathVariable Long id){
        ApiResponse apiResponse=workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }

    @PostMapping("/workspaceMemberAddorEditorRemove/{id}")
    public HttpEntity<?> workspaceMemberAddorEditorRemove(@PathVariable Long id,@RequestBody MemberDto memberDto){
        ApiResponse apiResponse=workspaceService.workspaceMemberAddorEditorRemove(id,memberDto);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }

    @PutMapping("/join")
    public HttpEntity<?> joinToWorkspace(@RequestParam Long id,@CurrentUser User user){
        ApiResponse apiResponse=workspaceService.joinToWorkspace(id,user);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }
    @GetMapping("/member/{id}")
    public HttpEntity<?> getMemberAndGuest(@PathVariable Long id){
        List<MemberDto> members =workspaceService.getMemberAndGuest(id);
        return ResponseEntity.ok(members);
    }
    @PostMapping("/role")
    public HttpEntity<?> addRole(@RequestParam Long workspaceId,@RequestBody WorkspaceRoleDto workspaceRoleDto, @CurrentUser User user){
        ApiResponse apiResponse=workspaceService.addRole(workspaceId,workspaceRoleDto,user);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getMyWorkspace(@CurrentUser User user){
        List<WorkspaceDto> workspace =workspaceService.getMyWorkspace(user);
        return ResponseEntity.ok(workspace);
    }

    @PutMapping("/addOrRemovePermission")
    public HttpEntity<?> addOrRemovePermission(@RequestBody WorkspaceRoleDto workspaceRoleDto){
            ApiResponse apiResponse=workspaceService.addOrRemovePermission(workspaceRoleDto);
            return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }


}
