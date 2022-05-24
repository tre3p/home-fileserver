package com.tre3p.fileserver.service;

import java.io.File;
import java.io.IOException;

public interface ArchiveService {

    void zipFile(String fileName, String filePath, File file) throws IOException;
}
