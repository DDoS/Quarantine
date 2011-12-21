package me.DDoS.Quarantine.permissions;

/**
 *
 * @author DDoS
 */

public enum QPermissions {
    
    PLAY("quarantine.play"),
    SETUP("quarantine.setup"),
    ADMIN("quarantine.admin");

    private String permissionsString;

    private QPermissions(String name) {
        
        this.permissionsString = name;
    
    }

    public String getPermissionsString() {
    
        return permissionsString;
    
    }
}