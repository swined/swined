import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.users.User;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Pic {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.UUIDHEX)
    private String id;
    @Persistent
    private User owner;
    @Persistent
    private Blob data;
    @Persistent
    private Date created;
    @Persistent
    private Date accessed;
    @Persistent
    private int views;

    public Pic(User owner, Image img) {
        this.owner = owner;
        this.data = new Blob(img.getImageData());
        this.created = new Date();
        this.accessed = new Date();
        this.views = 0;
    }

    public String getId() {
        return id;
    }

}
