package com.clickup.entity;

import com.clickup.entity.template.AbsUUIDEntity;
import com.clickup.entity.template.AbstrEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Attachment extends AbsUUIDEntity {

    private String name;

    private String orginalName;

    private Long size;

    private String contentType;
}
