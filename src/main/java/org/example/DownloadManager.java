package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.config.AppConfig;
import org.example.config.AppUtil;
import org.example.constants.Constants;
import org.example.models.FileInfo;

import java.io.File;

public class DownloadManager {
    @FXML
    private TextField urlTextField;
    @FXML
    private TableView<FileInfo> tableView;
    public int index = 0;
    @FXML
    void downloadButtonClicked(ActionEvent event) {
        // * declaring all variables for file
        String url = this.urlTextField.getText().trim();
        String fileName = url.substring(url.lastIndexOf("/")+1);
        long fileSize = 0;
        double progress = 0.0;
        String status = Constants.STARTING;
        String action = Constants.OPEN;
        String path = AppConfig.DOWNLOAD_PATH + File.separator + fileName;

        // * adding the variables to a FileInfo Object
        FileInfo file = new FileInfo(String.valueOf(index + 1), fileName, url, String.valueOf(fileSize), progress, status, action, path);
        this.index++;

        // * Download Thread started
        DownloadThread thread = new DownloadThread(file, this);
        this.tableView.getItems().add(Integer.parseInt(file.getIndex())-1, file);
        thread.start();
        this.urlTextField.setText("");
    }
    // * Whenever this function is called the UI is updated
    void updateUI(FileInfo file) {
        FileInfo fileInfo = this.tableView.getItems().get(Integer.parseInt(file.getIndex()) - 1);
        fileInfo.setStatus(file.getStatus());
        fileInfo.setSize(AppUtil.formatFileSize(AppUtil.parseDouble(file.getSize())));
        this.tableView.refresh();
        System.out.println(file);
    }

    @FXML
    void initialize() {
        TableColumn<FileInfo, String> sn = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(0);
        sn.setCellValueFactory(p->{
            return p.getValue().indexProperty();
        });
        TableColumn<FileInfo, String> fileName = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(1);
        fileName.setCellValueFactory(p->{
            return p.getValue().nameProperty();
        });
        TableColumn<FileInfo, String> fileSize = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(2);
        fileSize.setCellValueFactory(p -> {
            return p.getValue().sizeProperty();
        });
        TableColumn<FileInfo, Double> progressBar = (TableColumn<FileInfo, Double>) this.tableView.getColumns().get(3);
        progressBar.setCellValueFactory(p -> {
            return p.getValue().progressProperty().asObject();
        });
        progressBar.setCellFactory(ProgressBarTableCell.forTableColumn());
        TableColumn<FileInfo, String> status = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(4);
        status.setCellValueFactory(p->{
            return p.getValue().statusProperty();
        });
        status.setCellFactory(column -> new TableCell<FileInfo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    switch (item) {
                        case Constants.DOWNLOADING:
                            setTextFill(Color.BLUE);
                            break;
                        case Constants.DONE:
                            setTextFill(Color.GREEN);
                            break;
                        case Constants.FAILED:
                            setTextFill(Color.RED);
                            break;
                        default:
                            setTextFill(Color.BLACK);
                            break;
                    }
                }
            }
        });
        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(5);
        action.setCellValueFactory(p->{
            return p.getValue().actionProperty();
        });

    }
}
