package com.tre3p.fileserver.service;

import java.io.File;
import java.io.IOException;

public interface ArchiveService {

    File zipFile(String fileName, String sourceFile) throws IOException;
}
