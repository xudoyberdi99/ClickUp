package com.clickup.service;

import com.clickup.entity.*;
import com.clickup.entity.enums.AddType;
import com.clickup.entity.enums.WorkspacePermissionName;
import com.clickup.entity.enums.WorkspaceRoleName;
import com.clickup.payload.ApiResponse;
import com.clickup.payload.MemberDto;
import com.clickup.payload.WorkspaceDto;
import com.clickup.payload.WorkspaceRoleDto;
import com.clickup.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceServiceImpl implements WorkspaceService{
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    WorkspacePermissionRepository workspacePermissionRepository;
    @Autowired
    WorkspaceUserRepository workspaceUserRepository;
    @Autowired
    WorkspaceRoleRepository workspaceRoleRepository;
    @Autowired
    UserRepository userRepository;


    @Override
    public ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user) {
        //workspace qushdik
        if (workspaceRepository.existsByOwnerIdAndName(user.getId(),workspaceDto.getName())){
            return new ApiResponse("sizda bunday ishxona mavjud",false);
        }
        Workspace workspace=new Workspace(
                workspaceDto.getName(),
                workspaceDto.getColor(),
                user,
                workspaceDto.getAvatarId()==null?null:attachmentRepository.findById(workspaceDto.getAvatarId()).orElseThrow(()->new NotFoundException("not found Attachment"))
        );
        workspaceRepository.save(workspace);

        //workspaceRole ochdik
        WorkspaceRole ownerRole=workspaceRoleRepository.save(new WorkspaceRole(
                workspace,
                WorkspaceRoleName.ROLE_OWNER.name(),
                null
        ));

        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));
        //owner huquqlari
        WorkspacePermissionName[] values=WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissionslist=new ArrayList<>();
        for (WorkspacePermissionName workspacePermissionName : values) {
            WorkspacePermission workspacePermission=new WorkspacePermission(
                    ownerRole,
                    workspacePermissionName
            );
            workspacePermissionslist.add(workspacePermission);
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)){
                workspacePermissionslist.add(new WorkspacePermission(
                        adminRole,
                        workspacePermissionName
                ));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)){
                workspacePermissionslist.add(new WorkspacePermission(
                        memberRole,
                        workspacePermissionName
                ));
            }
            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)){
                workspacePermissionslist.add(new WorkspacePermission(
                        guestRole,
                        workspacePermissionName
                ));
            }
        }
        workspacePermissionRepository.saveAll(workspacePermissionslist);

        // qaysi userga tegishliligi
        workspaceUserRepository.save(new WorkspaceUser(
                workspace,
                user,
                ownerRole,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));

        return new ApiResponse("add workspace succes",true);
    }

    @Override
    public ApiResponse editWorkspace(Long id, WorkspaceDto workspaceDto) {
        return null;
    }

    @Override
    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId) {
        return null;
    }

    @Override
    public ApiResponse deleteWorkspace(Long id) {
        try {
            workspaceRepository.deleteById(id);
            return new ApiResponse("workspace delete",true);
        }catch (Exception e){
            return new ApiResponse("xatolik",false);
        }
    }

    @Override
    public ApiResponse workspaceMemberAddorEditorRemove(Long id, MemberDto memberDto) {
        if (memberDto.getAddType().equals(AddType.ADD)){
            WorkspaceUser workspaceUser=new WorkspaceUser(workspaceRepository.findById(id).orElseThrow(()-> new NotFoundException("id")),
                    userRepository.findById(memberDto.getMemberId()).orElseThrow(()-> new NotFoundException("memberId")),
                    workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(()->new NotFoundException("RoleId")),
                    new Timestamp(System.currentTimeMillis()),
                    null
            );
            workspaceUserRepository.save(workspaceUser);
        }
        // TODO email ga  xabar junatishni qilish
         else if (memberDto.getAddType().equals(AddType.EDIT)){
            WorkspaceUser workspaceUser=workspaceUserRepository.findByWorkspaceIdAndUserId(id,memberDto.getMemberId()).orElseGet(WorkspaceUser::new);
            workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(()->new NotFoundException("RoleId")));
            workspaceUserRepository.save(workspaceUser);
        }
        else if (memberDto.getAddType().equals(AddType.REMOVE)){
            workspaceUserRepository.deleteByWorkspaceIdAndUserId(id,memberDto.getMemberId());
        }
        return new ApiResponse("Muvaffaqiyatli", true);
    }

    @Override
    public ApiResponse joinToWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()){
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDateJoined(new Timestamp(System.currentTimeMillis()));
            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Succes",true);
        }
        return new ApiResponse("error",false);
    }

    @Override
    public List<MemberDto> getMemberAndGuest(Long id) {
        List<WorkspaceUser> workspaceUsers= workspaceUserRepository.findAllByWorkspaceId(id);
 //       List<MemberDto> memberDtoList=new ArrayList<>();
//        for (WorkspaceUser workspaceUser : workspaceUsers) {
//            memberDtoList.add(mapWorkspaceUserToMemberDto(workspaceUser));
//        }
//        return memberDtoList;
        return workspaceUsers.stream().map(this::mapWorkspaceUserToMemberDto).collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceDto> getMyWorkspace(User user) {
        List<WorkspaceUser>  workspaceUsers=workspaceUserRepository.findAllByUserId(user.getId());

        return workspaceUsers.stream().map(this::mapWorkspaceUserToWorkspaceDto).collect(Collectors.toList());
    }

    @Override
    public ApiResponse addOrRemovePermission(WorkspaceRoleDto workspaceRoleDto) {
        return null;
    }

    @Override
    public ApiResponse addRole(Long workspaceId, WorkspaceRoleDto workspaceRoleDto, User user) {
//        if (workspaceRoleRepository.existsByWorkspaceIdAndName(workspaceId,workspaceRoleDto.getName()))
//            return new ApiResponse("error", false);
//
//        workspaceRoleRepository.save(new WorkspaceRole(getWorkspaceAfterThrow(workspaceId)));
        return null;

    }

    //My method

    public WorkspaceDto mapWorkspaceUserToWorkspaceDto(WorkspaceUser workspaceUser){
        WorkspaceDto workspaceDto=new WorkspaceDto();
        workspaceDto.setId(workspaceUser.getWorkspace().getId());
        workspaceDto.setName(workspaceUser.getWorkspace().getName());
        workspaceDto.setInitialLetter(workspaceUser.getWorkspace().getInitialLetter());
        workspaceDto.setAvatarId(workspaceUser.getWorkspace().getAvatar()==null?null:workspaceUser.getWorkspace().getAvatar().getId());
        workspaceDto.setColor(workspaceUser.getWorkspace().getColor());
        return workspaceDto;
    }


    public MemberDto mapWorkspaceUserToMemberDto(WorkspaceUser workspaceUser){
        MemberDto memberDto=new MemberDto();
        memberDto.setMemberId(workspaceUser.getUser().getId());
        memberDto.setFullName(workspaceUser.getUser().getFullName());
        memberDto.setEmail(workspaceUser.getUser().getEmail());
        memberDto.setRoleName(workspaceUser.getWorkspaceRole().getName());
        memberDto.setLastAvtiveTime(workspaceUser.getUser().getLastActiveTime());

        return memberDto;
    }
}
