package com.tre3p.fileserver.service.impl;

import com.tre3p.fileserver.service.FileCompressorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
@Service
public class FileCompressorServiceImpl implements FileCompressorService {
    @Override
    public byte[] compress(byte[] data) {
        log.info("+compress(): original data size: {}", data.length);
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION, true);
        compressor.setInput(data);
        compressor.finish();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[1024];

        while (!compressor.finished()) {
            int readCount = compressor.deflate(readBuffer);
            if (readCount > 0) {
                bao.write(readBuffer, 0, readCount);
            }
        }

        compressor.end();

        byte[] result = bao.toByteArray();

        log.info("-compress(): compressed data size: {}", result.length);
        return bao.toByteArray();
    }

    @Override
    public byte[] decompress(byte[] data) throws DataFormatException {
        log.info("+decompress(): compressed data size: {}", data.length);

        Inflater decompressor = new Inflater(true);
        decompressor.setInput(data);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[1024];

        while (!decompressor.finished()) {
            int readCount = decompressor.inflate(readBuffer);
            if (readCount > 0) {
                bao.write(readBuffer, 0, readCount);
            }
        }

        decompressor.end();

        byte[] result = bao.toByteArray();
        log.info("-decompress(): decompressed data size: {}", result.length);
        return result;
    }
}
