package org.noktron.femboyFreebuild.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Formatter {
    
    public static Component messageFormatting(String message) {
        
        return formatMinimessage(
                cleanupSerializedString(
                        serializeMinimessage(
                                formatLagacy(
                                        changeAmpersand(
                                                message
                                        )
                                )
                        )
                )
        );
        
    }
    
    public static Component formatMinimessage(String message) {
        
        return MiniMessage.miniMessage().deserialize(message);
        
    }
    
    public static Component formatLagacy(String message) {
        
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        
    }
    
    public static String serializeMinimessage(Component component) {
        
        return MiniMessage.miniMessage().serialize(component);
        
    }
    
    public static String serializeLagacy(Component component) {
        
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
        
    }
    
    public static String changeAmpersand(String message) {
        
        return message.replace("§", "&");
        
    }
    
    public static String cleanupSerializedString(String message) {
        
        return message.replace("\\", "");
        
    }
    
}
