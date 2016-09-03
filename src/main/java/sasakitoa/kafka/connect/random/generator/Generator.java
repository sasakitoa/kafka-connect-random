package sasakitoa.kafka.connect.random.generator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import sasakitoa.kafka.connect.random.params.Params;
import sasakitoa.kafka.connect.random.summary.TaskSummary;
import sasakitoa.kafka.connect.random.utils.KeyValue;

import java.util.Map;

/**
 * Base class of generator
 */
public abstract class Generator {

    /**
     * Parameter List which is used in Generator
     */
    protected Params params;

    /**
     * Add configurations for generator to ConfigDef
     * @param configDef
     */
    public void setConfigDef(ConfigDef configDef) {
        if(params != null) {
            params.setConfig(configDef);
        }
    }

    /**
     * Return properties for this generator
     * NOTE: This method is given props specified when start this connector, you should chose props from these.
     * @param props
     * @return
     */
    public abstract Map<String, String> setTaskConfigs(Map<String, String> props);

    /**
     * Create TaskSummary Object
     * this method called when task.summary.enable is true only.
     * @return
     */
    public TaskSummary createTaskSummary(int taskId) {
       return new TaskSummary(taskId);
    }

    /**
     * Return schema type of KEY which this generator will generate
     * @return
     */
    public abstract Schema getKeySchema();

    /**
     * Return schema type of VALUE which this generator will generate
     * @return
     */
    public abstract Schema getValueSchema();

    /**
     * Initialize process for this generator
     * @param props
     */
    public void start(Map<String, String> props) throws ConnectException {
        // Default is nothing to do.
        // if need to do something, should override this method.
        // (For example: read configuration for Generator from Properties)
    }

    /**
     * Generate KEY and VALUE to send by this Connector
     * @return
     */
    public abstract KeyValue generate();

    /**
     * finalize process for this generator
     * @throws ConnectException
     */
    public void stop() throws ConnectException {
        // Default is nothing to do.
        // if need to do something, should override this method.
        // (For example: close file stream which opened in this class)
    }

}
