package com.tre3p.fileserver.view;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.ArchiveService;
import com.tre3p.fileserver.service.FileService;
import com.tre3p.fileserver.service.impl.FileServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
@Route(value = "")
@PageTitle("File Server")
public class MainView extends VerticalLayout {

    private static final int DEFAULT_NOTIFICATION_TIMEOUT = 5000;

    private Grid<FileMetadata> grid = new Grid<>(FileMetadata.class, false);

    private final FileService fileService;

    public MainView(FileServiceImpl fileService, ArchiveService archiveService) {
        this.fileService = fileService;

        setSizeFull();
        configureGrid();
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
                                Notification.Position.BOTTOM_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                throw new RuntimeException(e);
            }
            Notification.show("File successfully uploaded!",
                    DEFAULT_NOTIFICATION_TIMEOUT,
                    Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
                    String unzippedFilePath = fileService.prepareForDownload(file.getId());
                    inputStream = new FileInputStream(unzippedFilePath);
                } catch (Exception e) {
                    Notification.show("Error while downloading file!", // todo: инит уведомлений в конструкторе над
                            DEFAULT_NOTIFICATION_TIMEOUT,
                            Notification.Position.BOTTOM_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    throw new RuntimeException(e);
                }
                return inputStream;
            }), StringUtils.EMPTY);
            anchor.getElement().setAttribute("download", true);
            anchor.getElement().appendChild(button.getElement());
            // todo: думал тут вызывать удаление анзип файла, но не срабатывает
            return anchor;
        });
    }

    private void addDeleteButton() {
        grid.addComponentColumn(file -> {
            Button button = new Button("Delete", VaadinIcon.FILE_REMOVE.create());
            button.addClickListener(event -> {
                try {
                    fileService.removeById(file.getId());
                } catch (FileNotFoundException e) {
                    Notification.show("Error while deleting file!",
                            DEFAULT_NOTIFICATION_TIMEOUT,
                            Notification.Position.BOTTOM_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    throw new RuntimeException(e);
                }
                grid.setItems(fileService.getAll());
            });
            return button;
        });
    }

}
