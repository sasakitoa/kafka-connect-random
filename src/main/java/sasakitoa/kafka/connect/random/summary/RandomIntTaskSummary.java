package sasakitoa.kafka.connect.random.summary;

/**
 * Implement Task Summary for RandomInt Generator
 */
public class RandomIntTaskSummary extends TaskSummary {

    private int keyMin, keyMax, valueMin, valueMax;

    public RandomIntTaskSummary(int taskId) {
        super(taskId);
        keyMin = Integer.MAX_VALUE;
        keyMax = Integer.MIN_VALUE;
        valueMin = Integer.MAX_VALUE;
        valueMax = Integer.MIN_VALUE;
    }

    public void setKey(int key) {
        if(keyMin > key) {
            keyMin = key;
        }

        if(keyMax < key) {
            keyMax = key;
        }
    }

    public void setValue(int value) {
        if(valueMin > value) {
            valueMin = value;
        }

        if(valueMax < value) {
            valueMax = value;
        }
    }

    public void setKeyValue(int key, int value) {
        setKey(key);
        setValue(value);
    }

    @Override
    public String toString() {
        return "Task: " + getTaskId() + " Processed " + getSendMessages() + " messages " +
                "Generated key min:" + keyMin + " max:" + keyMax + ", Generated value min:" + valueMin + " max:" + valueMax;
    }
}
