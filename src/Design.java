import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public interface Design {
	static Font HeadingFont() {
		Font font= new Font(20);
        font= Font.font("Comic Sans Ms",25);
        return font;
	}
	static Font ButtonFont() {
		Font font= new Font(20);
        font= Font.font("Comic Sans Ms",15);
        return font;
	}
	static Font H2Font() {
		Font namefont= new Font(20);
        namefont= Font.font("Comic Sans Ms",FontWeight.BOLD,20);
        return namefont;
	}
	static String ButtonStyle() {
		return "-fx-background-color: #FFFFFF; -fx-background-radius: 15px; -fx-effect: dropshadow(gaussian, black, 20, 0, 0, 0);";

	}
	static void Layout(Node node ,int x, int y, Group root) {
		node.setLayoutX(x);
        node.setLayoutY(y);
        root.getChildren().add(node);
	}
	static void AlertMsg(int type, String status, String heading, String Content) {
    	Alert alert= new Alert(AlertType.INFORMATION);
    	switch(type) {
    	case 1:
    	{
    		alert= new Alert(AlertType.INFORMATION);
    		break;
    	}
    	case 2:
    	{
    		alert= new Alert(AlertType.CONFIRMATION);
    		break;
    	}
    	case 3:
    	{
    		alert= new Alert(AlertType.ERROR);
    		break;
    	}
    	case 4:
    	{
    		alert= new Alert(AlertType.WARNING);
    		break;
    	}
    	default:
    	{
    		break;
    	}
    	}
    	alert.setTitle(status);
    	alert.setHeaderText(heading);
    	alert.setContentText(Content);
    	alert.showAndWait();
    }

}
