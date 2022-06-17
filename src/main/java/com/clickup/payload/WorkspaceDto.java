package com.clickup.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class WorkspaceDto {

    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String color;
    @NotNull
    private UUID avatarId;

    private  UUID ownerId;

    private String initialLetter;
}
