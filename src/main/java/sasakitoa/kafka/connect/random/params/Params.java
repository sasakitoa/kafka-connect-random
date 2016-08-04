package sasakitoa.kafka.connect.random.params;

import org.apache.kafka.common.config.ConfigDef;

/**
 * Interface of Parameter list class.
 */
public interface Params {

    ConfigDef setConfig(ConfigDef configDef);
}
