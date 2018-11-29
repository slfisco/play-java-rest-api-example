package controllers;

public class TaskData {
    public String taskName;
    //public String status;
    public TaskData() {
    }
    public String getTaskNameaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //FORM WILL NO LONGER HANDLE STATUS INPUT. DEFAULT IS FALSE
    /*public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    */
}
