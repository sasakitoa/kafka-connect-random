package sasakitoa.kafka.connect.random.summary;

/**
 * Created by sasaki on 16/09/01.
 */
public class TaskSummary {

    // Task ID
    private int taskId;

    // Number of records which send in each tasks.
    private long records;

    public TaskSummary(int taskId) {
        this.taskId = taskId;
        this.records = 0;
    }

    public void addSendRecord(long numRecords) {
        this.records += numRecords;
    }

    @Override
    public String toString() {
        return "Task:" + taskId + " Processed " + records + " messages";
    }
}
