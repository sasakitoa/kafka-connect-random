package sasakitoa.kafka.connect.random.summary;

/**
 * Base class of TaskSummary.
 * if want to use original TaskSummary, create new class extends this class.
 */
public class TaskSummary {

    /**
     * Task ID which handled this TaskSumamry
      */
    private int taskId;

    /**
     * Number of records which send in this task
     */
    private long messages;

    public TaskSummary(int taskId) {
        this.taskId = taskId;
        this.messages = 0;
    }

    /**
     * Get Task ID
     * @return
     */
    public int getTaskId() {
        return this.taskId;
    }


    /**
     * Add number of send messages.
     * NOTICE: This method is called in RandomSourceTask, you need NOT to use this one usually.
     * @param numMessages
     */
    public void addSendMessages(long numMessages) {
        this.messages += numMessages;
    }

    /**
     * Get number of send messages by this task.
     * @return
     */
    public long getSendMessages() {
        return this.messages;
    }

    @Override
    public String toString() {
        return "Task:" + getTaskId() + " Processed " + getSendMessages() + " messages";
    }
}
