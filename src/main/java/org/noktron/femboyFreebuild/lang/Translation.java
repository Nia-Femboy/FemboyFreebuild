package org.noktron.femboyFreebuild.lang;

import me.monst.pluginutil.configuration.ConfigurationValue;
import me.monst.pluginutil.configuration.transform.Transformer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * A translation string that can be read from a configuration file.
 * The string is stored as a {@link Component} object, which can be used to format the text.
 */
public class Translation extends ConfigurationValue<Component>  {
    
    private static Component deserialize(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
    
    public Translation(String key, String defaultValue) {
        super(key, deserialize(defaultValue), new ComponentTransformer()); // s -> s just reads the strings straight from the file
    }
    
    private static class ComponentTransformer implements Transformer<Component> {
        @Override
        public Component parse(String s) {
            return deserialize(s);
        }
    }
    
}
