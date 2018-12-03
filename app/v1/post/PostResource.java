package v1.post;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class PostResource {
    private String id;
    private String link;
    private String taskName;
    private String status;
    private String updateLink;

    public PostResource() {
    }

    public PostResource(String id, String link, String taskName, String status, String updateLink) {
        this.id = id;
        this.link = link;
        this.taskName = taskName;
        this.status = status;
        this.updateLink = updateLink;
    }

    public PostResource(PostData data, String link, String updateLink) {
        this.id = data.id.toString();
        this.link = link;
        this.taskName = data.taskName;
        this.status = data.status;
        this.updateLink = updateLink;
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

    public String getUpdateLink() {
        return updateLink;
    }
}
