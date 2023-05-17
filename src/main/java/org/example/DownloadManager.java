package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.config.AppConfig;
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
        String url = this.urlTextField.getText().trim();
        String fileName = url.substring(url.lastIndexOf("/")+1);
        String status = Constants.STARTING;
        String action = Constants.OPEN;
        String path = AppConfig.DOWNLOAD_PATH+ File.separator+fileName;
        FileInfo file = new FileInfo(String.valueOf(index + 1), fileName, url, status, action, path);
        this.index++;
        DownloadThread thread = new DownloadThread(file, this);
        this.tableView.getItems().add(Integer.parseInt(file.getIndex())-1, file);
        thread.start();
        this.urlTextField.setText("");
    }
    void updateUI(FileInfo file) {
        FileInfo fileInfo = this.tableView.getItems().get(Integer.parseInt(file.getIndex())-1);
        fileInfo.setStatus(file.getStatus());
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
        TableColumn<FileInfo, String> status = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(4);
        status.setCellValueFactory(p->{
            return p.getValue().statusProperty();
        });
        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(5);
        action.setCellValueFactory(p->{
            return p.getValue().actionProperty();
        });

    }
}
