package org.example;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.example.config.AppUtil;
import org.example.models.FileInfo;

public class ProgressBarTableCell<S> extends TableCell<S, Double> {
    private final ProgressBar progressBar;
    public ProgressBarTableCell() {
        this.progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        setGraphic(progressBar);
    }

    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            progressBar.setProgress(item);
            setText(getFormattedProgress(item));
            setGraphic(progressBar);
        }
    }

    public static <S> Callback<TableColumn<S, Double>, TableCell<S, Double>> forTableColumn() {
        return column -> new ProgressBarTableCell<>();
    }
    private String getFormattedProgress(double progress) {
        if (getTableRow() != null && getTableRow().getItem() instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) getTableRow().getItem();
            double fileSize = AppUtil.parseDouble(fileInfo.getSize());
            double downloadedBytes = fileSize*progress;
            return String.format("%.0f%% (%s/%s)", progress * 100, AppUtil.formatFileSize(downloadedBytes), AppUtil.formatFileSize(fileSize));
        }
        return "";
    }
}
