package com.tre3p.fileserver.view;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.FileService;
import com.tre3p.fileserver.service.impl.FileServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.olli.ClipboardHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_END;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;

@Slf4j
@Route(value = "")
@PageTitle("File Server")
public class MainView extends VerticalLayout {

    private static final int DEFAULT_NOTIFICATION_TIMEOUT = 5000;

    private Grid<FileMetadata> grid = new Grid<>(FileMetadata.class, false);

    private final FileService fileService;

    public MainView(FileServiceImpl fileService) {
        this.fileService = fileService;

        setSizeFull();
        configureGrid();
        addCopyButton();
        addDownloadButton();
        addDeleteButton();
        add(grid);
        add(getUpload());
    }

    private Upload getUpload() {
        MultiFileBuffer mf = new MultiFileBuffer();
        Upload upload = new Upload(mf);

        upload.addSucceededListener(q -> {
            String fileName = q.getFileName();
            String contentType = q.getMIMEType();
            File file = mf.getFileData(fileName).getFile();
            try {
                fileService.prepareAndSave(fileName, contentType, file);
            } catch (Exception e) {
                Notification.show("Error while saving file!",
                        DEFAULT_NOTIFICATION_TIMEOUT,
                        BOTTOM_END).addThemeVariants(LUMO_ERROR);
                throw new RuntimeException(e);
            }
            Notification.show("File successfully uploaded!",
                    DEFAULT_NOTIFICATION_TIMEOUT,
                    BOTTOM_END).addThemeVariants(LUMO_SUCCESS);
            grid.setItems(fileService.getAll());
        });
        return upload;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(FileMetadata::getOriginalFileName).setHeader("File Name");
        grid.addColumn(FileMetadata::getContentType).setHeader("Content Type");
        grid.addColumn(FileMetadata::getOriginalSize).setHeader("Size");
        grid.setItems(fileService.getAll());
    }

    private void addDownloadButton() {
        grid.addComponentColumn(file -> {
            Button button = new Button("Download", VaadinIcon.CLOUD_DOWNLOAD.create());
            Anchor anchor = new Anchor(new StreamResource(file.getOriginalFileName(), (InputStreamFactory) () ->
            {
                InputStream inputStream;

                try {
                    inputStream = fileService.prepareForDownload(file.getId());
                } catch (Exception e) {
                    Notification.show(
                            "Error while downloading file!",
                            DEFAULT_NOTIFICATION_TIMEOUT,
                            BOTTOM_END).addThemeVariants(LUMO_ERROR);
                    throw new RuntimeException(e);
                }
                return inputStream;
            }), StringUtils.EMPTY);
            anchor.getElement().setAttribute("download", true);
            anchor.getElement().appendChild(button.getElement());
            return anchor;
        });
    }

    private void addCopyButton() {
        grid.addComponentColumn(file -> {
            Button button = new Button("Copy", VaadinIcon.COPY.create());
            button.addClickListener(click -> {
                Notification.show(
                        "Copied!",
                        DEFAULT_NOTIFICATION_TIMEOUT,
                        BOTTOM_END).addThemeVariants(LUMO_SUCCESS);
            });
            String linkToFileWithHash = fileService.buildPathToFileHash(file.getHash());
            return new ClipboardHelper(linkToFileWithHash, button);
        });
    }

    private void addDeleteButton() {
        grid.addComponentColumn(file -> {
            Button button = new Button("Delete", VaadinIcon.FILE_REMOVE.create());
            button.addClickListener(event -> {
                try {
                    fileService.removeById(file.getId());
                    Notification.show(
                            "Successfully deleted!",
                            DEFAULT_NOTIFICATION_TIMEOUT,
                            BOTTOM_END).addThemeVariants(LUMO_SUCCESS);
                } catch (FileNotFoundException e) {
                    Notification.show(
                            "Error while deleting file!",
                            DEFAULT_NOTIFICATION_TIMEOUT,
                            BOTTOM_END).addThemeVariants(LUMO_ERROR);
                    throw new RuntimeException(e);
                }
                grid.setItems(fileService.getAll());
            });
            return button;
        });
    }

}
