package org.noktron.femboyFreebuild.config.parse;

import me.monst.pluginutil.configuration.exception.ArgumentParseException;
import me.monst.pluginutil.configuration.transform.Transformer;
import me.monst.pluginutil.configuration.validation.Bound;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTransformer implements Transformer<Path> {
    
    @Override
    public Path parse(String input) throws ArgumentParseException {
        try {
            return Paths.get(input);
        } catch (InvalidPathException e) {
            throw new ArgumentParseException("Invalid path: " + input);
        }
    }
    
    @Override
    public Object toYaml(Path value) {
        return value.toString();
    }
    
    /**
     * Returns a transformer that requires the path to be a certain file type or one of its alternatives.
     * If the user enters a path that does not meet the requirements, the main file extension will be appended to the path.
     * @param mainExtension the main file extension
     * @param alternatives alternative file extensions
     * @return a transformer that requires the path to be a certain file type or one of its alternatives
     */
    public Transformer<Path> ofType(String mainExtension, String... alternatives) {
        return bounded(
                Bound.requiring(path -> {
                            if (path.toString().endsWith(mainExtension))
                                return true;
                            for (String exception : alternatives) {
                                if (path.toString().endsWith(exception))
                                    return true;
                            }
                            return false;
                        },
                        path -> path.resolveSibling(path.getFileName() + mainExtension))
        );
    }
    
}
