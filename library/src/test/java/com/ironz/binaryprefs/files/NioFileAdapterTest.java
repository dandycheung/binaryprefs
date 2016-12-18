package com.ironz.binaryprefs.files;

import com.ironz.binaryprefs.exception.FileOperationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.*;

@SuppressWarnings("FieldCanBeLocal")
public class NioFileAdapterTest {

    private final String fileName = "file.name";
    private final String fileNameTwo = fileName + 2;
    private final byte[] bytes = "value".getBytes();

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();
    private FileAdapter fileAdapter;
    private File srcDir;

    @Before
    public void setUp() throws Exception {
        srcDir = folder.newFolder();
        fileAdapter = new NioFileAdapter(srcDir);
    }

    @Test(expected = FileOperationException.class)
    public void deletedDirectory() {
        folder.delete(); //trying to remove root directory before starting write operation
        fileAdapter.save(fileName, bytes);
    }

    @Test
    public void correctSaving() {
        fileAdapter.save(fileName, bytes);
    }

    @Test(expected = FileOperationException.class)
    public void missingFile() {
        fileAdapter.fetch(fileName);
    }

    @Test
    public void correctFile() {
        fileAdapter.save(fileName, bytes);
        byte[] fetch = fileAdapter.fetch(fileName);
        assertEquals(new String(bytes), new String(fetch));
    }

    @Test
    public void deleteOne() {
        File file = new File(srcDir, fileName);

        fileAdapter.save(fileName, bytes);
        assertTrue(file.exists());
        assertFalse(file.isDirectory());

        fileAdapter.remove(fileName);
        assertFalse(file.exists());
    }

    @Test
    public void deleteAll() {
        File file = new File(srcDir, fileName);
        File fileTwo = new File(srcDir, fileNameTwo);

        fileAdapter.save(fileName, bytes);
        fileAdapter.save(fileNameTwo, bytes);

        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        assertTrue(fileTwo.exists());
        assertFalse(fileTwo.isDirectory());

        fileAdapter.clear();

        assertFalse(file.exists());
        assertFalse(fileTwo.exists());
    }
}