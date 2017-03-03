/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fraunhofer.iais.eis.jrdfb.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author <a href="mailto:bernd.breitenbach@iml.fraunhofer.de">Bernd Breitenbach</a>
 */
public class FileUtils {

	public static String readFile(String path, Charset encoding)
	{
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);
	}

	public static void writeFile(String path, String content, Charset encoding){
        final File file = new File(path);
        try {
            org.apache.commons.io.FileUtils.writeStringToFile(file, content, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readResource(String fileName, Class<?> resourceClass) throws IOException {

        return IOUtils.toString(
                resourceClass.getResourceAsStream(fileName),
                "UTF-8"
        );
    }

}
