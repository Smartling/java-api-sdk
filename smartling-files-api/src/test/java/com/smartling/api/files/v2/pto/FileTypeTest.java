package com.smartling.api.files.v2.pto;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileTypeTest
{
    @Test
    public void mdx_hasCorrectMimeTypeAndIsTextFormat()
    {
        assertEquals("text/plain", FileType.MDX.getMimeType());
        assertTrue(FileType.MDX.isTextFormat());
    }

    @Test
    public void imagePng_hasCorrectMimeTypeAndIsNotTextFormat()
    {
        assertEquals("image/png", FileType.IMAGE_PNG.getMimeType());
        assertFalse(FileType.IMAGE_PNG.isTextFormat());
    }

    @Test
    public void imageJpg_hasCorrectMimeTypeAndIsNotTextFormat()
    {
        assertEquals("image/jpeg", FileType.IMAGE_JPG.getMimeType());
        assertFalse(FileType.IMAGE_JPG.isTextFormat());
    }

    @Test
    public void imageWebp_hasCorrectMimeTypeAndIsNotTextFormat()
    {
        assertEquals("image/webp", FileType.IMAGE_WEBP.getMimeType());
        assertFalse(FileType.IMAGE_WEBP.isTextFormat());
    }

    @Test
    public void imageGif_hasCorrectMimeTypeAndIsNotTextFormat()
    {
        assertEquals("image/gif", FileType.IMAGE_GIF.getMimeType());
        assertFalse(FileType.IMAGE_GIF.isTextFormat());
    }

    @Test
    public void imageBmp_hasCorrectMimeTypeAndIsNotTextFormat()
    {
        assertEquals("image/bmp", FileType.IMAGE_BMP.getMimeType());
        assertFalse(FileType.IMAGE_BMP.isTextFormat());
    }

    @Test
    public void lookup_findsNewEntriesBySnakeCaseName()
    {
        assertEquals(FileType.MDX,       FileType.lookup("MDX"));
        assertEquals(FileType.IMAGE_PNG, FileType.lookup("IMAGE_PNG"));
        assertEquals(FileType.IMAGE_JPG, FileType.lookup("IMAGE_JPG"));
        assertEquals(FileType.IMAGE_WEBP,FileType.lookup("IMAGE_WEBP"));
        assertEquals(FileType.IMAGE_GIF, FileType.lookup("IMAGE_GIF"));
        assertEquals(FileType.IMAGE_BMP, FileType.lookup("IMAGE_BMP"));
    }

    @Test
    public void lookup_findsNewEntriesByCamelCaseName()
    {
        assertEquals(FileType.MDX,        FileType.lookup("mdx"));
        assertEquals(FileType.IMAGE_PNG,  FileType.lookup("imagePng"));
        assertEquals(FileType.IMAGE_JPG,  FileType.lookup("imageJpg"));
        assertEquals(FileType.IMAGE_WEBP, FileType.lookup("imageWebp"));
        assertEquals(FileType.IMAGE_GIF,  FileType.lookup("imageGif"));
        assertEquals(FileType.IMAGE_BMP,  FileType.lookup("imageBmp"));
    }
}
