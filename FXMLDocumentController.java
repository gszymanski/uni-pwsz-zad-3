import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class FXMLDocumentController implements Initializable {
	@FXML private AnchorPane anchorPane;
	@FXML private Label nameLabel;
	@FXML private TextField nameTextField;
	@FXML private Label numberLabel;
	@FXML private TextField numberTextField;
	@FXML private Button addButton;
	@FXML private Button updateButton;
	@FXML private Button deleteButton;
	@FXML private TableView table;
	@FXML private TableColumn tableColName;
	@FXML private TableColumn tableColNumber;

	public void initialize(URL url, ResourceBundle rb) {
		tableColName.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
		tableColNumber.setCellValueFactory(new PropertyValueFactory<Contact, String>("number"));
		ObservableList<Contact> list = FXCollections.observableArrayList();
		table.setItems(list);
		loadFromFile();
	}

	@FXML
	private void addButtonClick(ActionEvent event) {
		if (nameTextField.getText().equals("") || numberTextField.getText().equals(""))
			return;

		table.getItems().add(new Contact(nameTextField.getText(), numberTextField.getText()));
		saveToFile();
	}

	@FXML
	private void updateButtonClick(ActionEvent event) {
		if (nameTextField.getText().equals("") || numberTextField.getText().equals(""))
			return;

		Contact c = (Contact)table.getSelectionModel().getSelectedItem();
		c.setName(nameTextField.getText());
		c.setNumber(numberTextField.getText());
		tableColName.setVisible(false); // workaround for
		tableColName.setVisible(true);  // TableView refresh bug
		saveToFile();
	}

	@FXML
	private void deleteButtonClick(ActionEvent event) {
		if (table.getSelectionModel().isEmpty())
			return;

		table.getItems().remove(table.getSelectionModel().getSelectedIndex());
		saveToFile();
	}

	@FXML
	private void tableClick() {
		Contact c = (Contact)table.getSelectionModel().getSelectedItem();
		nameTextField.setText(c.getName());
		numberTextField.setText(c.getNumber());
	}

	private void loadFromFile() {
		try {
			Scanner fin = new Scanner(new File("data.txt"));
			while (fin.hasNextLine())
				table.getItems().add(new Contact(fin.nextLine(), fin.nextLine()));
			fin.close();
		} catch (Exception e) {}
	}

	private void saveToFile() {
		try {
			PrintWriter fout = new PrintWriter("data.txt");
			for (int i = 0; i < table.getItems().size(); ++i) {
				fout.println(((Contact)table.getItems().get(i)).getName());
				fout.println(((Contact)table.getItems().get(i)).getNumber());
			}
			fout.close();
		} catch (Exception e) {}
	}
}
