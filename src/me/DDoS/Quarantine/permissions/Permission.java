package me.DDoS.Quarantine.permissions;

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

    public String getPermissionsString() {
    
        return permissionsString;
    
    }
}