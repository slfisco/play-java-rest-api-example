package controllers;

public class TaskData {
    public String taskName;
    public String status;
    public TaskData() {
    }
    //will probably need to add get/set
    public String getTaskNameaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
