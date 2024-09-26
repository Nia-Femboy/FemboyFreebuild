package org.noktron.femboyFreebuild.lang;

import me.monst.pluginutil.configuration.ConfigurationBranch;
import me.monst.pluginutil.configuration.YamlFile;
import org.noktron.femboyFreebuild.FemboyFreebuildPlugin;

import java.nio.file.Path;

/**
 * This class handles the language configuration for the plugin.
 * It reads and writes to a YAML file in the plugin's data folder, which can be switched out at runtime.
 * It also provides default values for the language strings.
 */
public class LanguageConfiguration extends ConfigurationBranch {
    
    public final Translation pluginConfigured = addChild(new Translation("plugin-configured", "<green>Set <aqua><path><green> from <aqua><previous><green> to <aqua><current><green>."));
    public final Translation invalidConfigPath = addChild(new Translation("not-a-config-value", "<red><path> is not a configuration value."));
    public final Translation playerCommandOnly = addChild(new Translation("player-command-only", "<red>Player command only!"));
    
    public final Translation chunkClaimed = addChild(new Translation("chunk-claimed", "<yellow>Chunk claimed!"));
    public final Translation chunkUnclaimed = addChild(new Translation("chunk-unclaimed", "<yellow>Chunk unclaimed!"));
    public final Translation chunkNotClaimed = addChild(new Translation("chunk-not-claimed", "<red>You have not claimed this chunk."));
    public final Translation chunkOwnedBySomeoneElse = addChild(new Translation("chunk-owned-by-other", "<red>You do not own this chunk."));
    public final Translation chunkAlreadyClaimed = addChild(new Translation("chunk-already-claimed", "<red>This chunk is already claimed."));
    public final Translation maxClaimedChunksReached = addChild(new Translation("max-claimed-chunks-reached", "<red>You have reached the maximum number of claimed chunks."));
    
    private final FemboyFreebuildPlugin plugin;
    private YamlFile file; // Current language file
    
    public LanguageConfiguration(FemboyFreebuildPlugin plugin) {
        super(null); // Top-level configuration branch does not need a key
        this.plugin = plugin;
    }
    
    public void switchToFile(Path filepath) {
        file = new YamlFile(plugin, filepath);
        reload();
    }
    
    /**
     * Loads the language file from disk into memory, and then saves any missing translations to disk.
     */
    public void reload() {
        load();
        save();
    }
    
    public void load() {
        feed(file.load());
    }
    
    public void save() {
        file.save(this.getAsYaml());
    }
    
}
