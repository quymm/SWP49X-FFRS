package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
public class RolePermissionEntityPK implements Serializable {
    private String roleName;
    private String permission;

    @Column(name = "RoleName")
    @Id
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(name = "Permission")
    @Id
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

        RolePermissionEntityPK that = (RolePermissionEntityPK) o;

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
