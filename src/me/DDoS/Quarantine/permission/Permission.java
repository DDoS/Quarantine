package me.DDoS.Quarantine.permission;

/**
 *
 * @author DDoS
 */

public enum Permission {
    
    PLAY("quarantine.play"),
    SETUP("quarantine.setup"),
    ADMIN("quarantine.admin");

    private final String permissionsString;

    private Permission(String name) {
        
        this.permissionsString = name;
    
    }

    public String getPermissionString() {
    
        return permissionsString;
    
    }
}