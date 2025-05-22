import com.dungeoncode.cae.core.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RepositoryTest {

    @Test
    void testLoadDescriptors(){
        Repository<Cell<CellState<?>>, CellState<?>> repository = new Repository<>();

        Map<Integer, Descriptor> rulesDescriptors = new HashMap();
        repository.loadDescriptors(Constants.DESCRIPTORS_FILE_RULES,rulesDescriptors);
        assertNotNull(rulesDescriptors.get(12));

        Map<Integer, Descriptor> initDescriptors = new HashMap();
        repository.loadDescriptors(Constants.DESCRIPTORS_FILE_INITS,initDescriptors);
        assertNotNull(initDescriptors.get(2));

        Map<Integer, Descriptor> confDescriptors = new HashMap();
        repository.loadDescriptors(Constants.DESCRIPTORS_FILE_CONFS,confDescriptors);
        assertNotNull(confDescriptors.get(16));
    }

}
