package com.tre3p.fileserver.view;

import com.tre3p.fileserver.model.FileContent;
import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.service.FileService;
import com.tre3p.fileserver.service.impl.FileServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

@Route(value = "")
@PageTitle("File Server")
public class MainView extends VerticalLayout {
    Grid<FileMetadata> grid = new Grid<>(FileMetadata.class, false);

    private final FileService fileService;

    public MainView(FileServiceImpl fileService) {
        this.fileService = fileService;

        setSizeFull();
        configureGrid();
        add(grid);
        add(getUpload());

    }

    private Upload getUpload() {
        MultiFileMemoryBuffer mf = new MultiFileMemoryBuffer();

        Upload upload = new Upload(mf);

        upload.addSucceededListener(q -> {
            String fileName = q.getFileName();
            String contentType = q.getMIMEType();
            byte[] data;
            InputStream inputStream = mf.getInputStream(fileName);
            try {
                data = inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fileService.prepareAndSave(fileName, contentType, data);
            } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                     BadPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
            grid.setItems(fileService.getAll());
            upload.clearFileList();
        });

        return upload;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(FileMetadata::getFileName).setHeader("File Name");
        grid.addColumn(FileMetadata::getContentType).setHeader("Content Type");
        grid.addColumn(FileMetadata::getOriginalSize).setHeader("Original Size");
        grid.addColumn(FileMetadata::getZippedSize).setHeader("Compressed Size");
        grid.setItems(fileService.getAll());

        grid.addComponentColumn(file -> {
            Button button = new Button("Download", VaadinIcon.CLOUD_DOWNLOAD.create());
            Anchor anchor = new Anchor(new StreamResource(file.getFileName(), (InputStreamFactory) () ->
            {
                try {
                    return new ByteArrayInputStream(fileService.decompressAndGetById(file.getId()));
                } catch (DataFormatException | NoSuchPaddingException | IllegalBlockSizeException |
                         NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }), "");
            anchor.getElement().setAttribute("download", true);
            anchor.getElement().appendChild(button.getElement());
            return anchor;
        });

        grid.addComponentColumn(file -> {
            Button button = new Button("Delete", VaadinIcon.FILE_REMOVE.create());
            button.addClickListener(event -> {
                fileService.removeById(file.getId());
                grid.setItems(fileService.getAll());
            });
            return button;
        });
    }


}
