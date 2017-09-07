package models;

import javax.persistence.*;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "RolePermission", schema = "mola", catalog = "")
@IdClass(RolePermissionEntityPK.class)
public class RolePermissionEntity {
    private String roleName;
    private String permission;

    @Id
    @Column(name = "RoleName")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Id
    @Column(name = "Permission")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePermissionEntity that = (RolePermissionEntity) o;

        if (roleName != null ? !roleName.equals(that.roleName) : that.roleName != null) return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleName != null ? roleName.hashCode() : 0;
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        return result;
    }
}
