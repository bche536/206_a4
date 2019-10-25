package helperClasses;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.scene.control.Alert;

public class AlertGenerator {
    private String _title, _header , _content, _alertType;
    private Alert alert;
    private Object InvalidArgumentException;

    public Alert newAlert(String title, String header, String content, String type) throws Throwable {
        if (type.toLowerCase().equals("error")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setTitle(header);
            alert.setContentText(content);
            return alert;
        }
        if(type.toLowerCase().equals("information")){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setTitle(header);
            alert.setContentText(content);
            return alert;
        }        if(type.toLowerCase().equals("confirmation")){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setTitle(header);
            alert.setContentText(content);
            return alert;
        }
        System.out.println("No such Alert Type: " + type + " in this alertGenerator");
        throw (Throwable) InvalidArgumentException;
    }
}
