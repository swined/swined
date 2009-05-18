
import javax.jdo.annotations.Persistent;


public class Note {

        @Persistent
        private String text;

        public Note(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

}
