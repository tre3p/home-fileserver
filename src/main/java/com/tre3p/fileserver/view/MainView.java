package com.tre3p.fileserver.view;

import com.tre3p.fileserver.model.FileMetadata;
import com.tre3p.fileserver.repository.FileRepository;
import com.tre3p.fileserver.service.FileService;
import com.tre3p.fileserver.service.impl.FileServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

@Slf4j
@Route(value = "")
@PageTitle("File Server")
public class MainView extends VerticalLayout {

    Grid<FileMetadata> grid = new Grid<>(FileMetadata.class, false);

    private final FileService fileService;

    private final FileRepository fileRepository;

    public MainView(FileServiceImpl fileService, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;

        setSizeFull();
        configureGrid();
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            grid.setItems(fileService.getAll());
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

                InputStream inputStream;
                try {
                    inputStream = new FileInputStream(fileRepository.getById(file.getId()).getPathToFile());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return inputStream;
            }), "");
            anchor.getElement().setAttribute("download", true);
            anchor.getElement().appendChild(button.getElement());
            return anchor;
        });

        grid.addComponentColumn(file -> {
            Button button = new Button("Delete", VaadinIcon.FILE_REMOVE.create());
            button.addClickListener(event -> {
                try {
                    fileService.removeById(file.getId());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                grid.setItems(fileService.getAll());
            });
            return button;
        });
    }


}
