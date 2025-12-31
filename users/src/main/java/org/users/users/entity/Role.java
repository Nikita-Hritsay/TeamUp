package org.users.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@With
public class Role extends BaseEntity
{

    @Column(name = "role_name", unique = true)
    private String roleName;

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Role role))
            return false;
        return roleName.equals(role.roleName);
    }

    @Override
    public int hashCode()
    {
        return roleName.hashCode();
    }
}