package v1.post;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class PostResource {
    private String id;
    private String link;
    private String taskName;
    private String status;

    public PostResource() {
    }

    public PostResource(String id, String link, String taskName, String status) {
        this.id = id;
        this.link = link;
        this.taskName = taskName;
        this.status = status;
    }

    public PostResource(PostData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.taskName = data.title;
        this.status = data.body;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getStatus() {
        return status;
    }

}
