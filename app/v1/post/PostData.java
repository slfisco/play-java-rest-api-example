package v1.post;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "posts")
public class PostData {

    public PostData() {
    }

    public PostData(String taskName, String status) {
        this.taskName = taskName;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String taskName;
    public String status;
}
