import com.dungeoncode.cae.core.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTest.class);

    @Test
    void testLoadDescriptors(){
        Repository<Cell<CellState<?>>, CellState<?>> repository = new Repository<>();

        // Test rules descriptors
        Map<Integer, Descriptor> rulesDescriptors = new HashMap<>();
        repository.loadDescriptors(Constants.DESCRIPTORS_FILE_RULES, rulesDescriptors);
        assertNotNull(rulesDescriptors.get(12));
        
        // Check for duplicate names in rules
        Set<String> ruleNames = new HashSet<>();
        int maxRuleId = 0;
        for (Descriptor descriptor : rulesDescriptors.values()) {
            assertTrue(ruleNames.add(descriptor.getName()), 
                "Duplicate name found in rules: " + descriptor.getName());
            maxRuleId = Math.max(maxRuleId, descriptor.getId());
        }
        LOGGER.debug("Max rule ID: {}", maxRuleId);

        // Test initializer descriptors
        Map<Integer, Descriptor> initDescriptors = new HashMap<>();
        repository.loadDescriptors(Constants.DESCRIPTORS_FILE_INITS, initDescriptors);
        assertNotNull(initDescriptors.get(2));
        
        // Check for duplicate names in initializers
        Set<String> initNames = new HashSet<>();
        int maxInitId = 0;
        for (Descriptor descriptor : initDescriptors.values()) {
            assertTrue(initNames.add(descriptor.getName()), 
                "Duplicate name found in initializers: " + descriptor.getName());
            maxInitId = Math.max(maxInitId, descriptor.getId());
        }
        LOGGER.debug("Max initializer ID: {}", maxInitId);

        // Test configuration descriptors
        Map<Integer, Descriptor> confDescriptors = new HashMap<>();
        repository.loadDescriptors(Constants.DESCRIPTORS_FILE_CONFS, confDescriptors);
        assertNotNull(confDescriptors.get(16));
        
        // Check for duplicate names in configurations
        Set<String> confNames = new HashSet<>();
        int maxConfId = 0;
        for (Descriptor descriptor : confDescriptors.values()) {
            assertTrue(confNames.add(descriptor.getName()), 
                "Duplicate name found in configurations: " + descriptor.getName());
            maxConfId = Math.max(maxConfId, descriptor.getId());
        }
        LOGGER.debug("Max configuration ID: {}", maxConfId);
    }

}
