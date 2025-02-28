package Main.java;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public interface Navigation {
	static void toHomepage() {
		Homepage homepage= new Homepage();
		Main.ps.setScene(homepage.getscene());
	}
	static void toTransactionPage() {
		Transaction pgtransact= new Transaction();
		Main.ps.setScene(pgtransact.getscene());
	}
	static void toHistoryPage() {
		History history= new History();
		Main.ps.setScene(history.getscene());
	}
	static void toGraphs() {
		Graphs graphs= new Graphs();
		Main.ps.setScene(graphs.getscene());
	}

	static void toModPage() {
		Modifications modifications= new Modifications();
		Main.ps.setScene(modifications.getscene());
	}
}
