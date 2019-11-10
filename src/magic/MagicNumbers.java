package magic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MagicNumbers {
	
    private static final int BUFFER_SIZE = 4096;
    private static final int MAX_SIGNATURE_SIZE = 4;
    
    private static final String FILE_PATH = "data/";

    // bytes for pdf files
    private static final int[] pdfSig = { 0x25, 0x50, 0x44, 0x46 };
    // bytes for different jpg files
    private static final int[] jpga = { 0xff, 0xd8, 0xff, 0xdb};
    private static final int[] jpgb = { 0xff, 0xd8, 0xff, 0xe3};
    private static final int[] jpgc = { 0xff, 0xd8, 0xff, 0xe0};
    // bytes for gif files
    private static final int[] gif = { 0x47, 0x49, 0x46, 0x38};
    // map for all types
    private Map<String,int[]> signatureMap;
    
    // initializing 
    public MagicNumbers() {
	signatureMap = new HashMap<String,int[]>();
	signatureMap.put("PDF", pdfSig);
	signatureMap.put("GIF", gif);
	signatureMap.put("JPG", jpga);
	signatureMap.put("JPG", jpgb);
	signatureMap.put("JPG", jpgc);
    }
    
    public String getFile(File f) throws IOException {
    	
   
    	
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream in = new FileInputStream(f);
	try {
	    int n = in.read(buffer, 0, BUFFER_SIZE);
	    int m = n;
	    while ((m < MAX_SIGNATURE_SIZE) && (n > 0)) { 
		n = in.read(buffer, m, BUFFER_SIZE - m);
		m += n;
	    }

	    String fileType = "UNSUPPORTED FILE";
	    for (Iterator<String> i = signatureMap.keySet().iterator(); i.hasNext();) {
		String key = i.next();
		if (matchesSignature(signatureMap.get(key), buffer, m) || suffix(f).equals("TXT")) {
			
			// there is no magic number for txt files so i think that if file supported
			// by my app and is not  detected it must be a txt file because
			// my app doesn't support other files
			if(!matchesSignature(signatureMap.get(key), buffer, m) && suffix(f).equals("TXT")) {
		    fileType = "File type is : TXT";
		    
		    return fileType;
			}
			else if(checkSignature(f, key)) {
			    fileType = key;
			    return fileType;
				}
			else {
				fileType = key;
			return fileType + " but is declared as; " + suffix(f);	
			}
		}
	    }
	    // if file is  by app throws exception 
	    throw new IllegalArgumentException("Unsuported file");
	    
	}
	finally {
	    in.close();
	}
	
    }
    
    // that helper method checks is it file signature is the same as detected
    private static boolean checkSignature(File f, String s) {
    	
    	if(suffix(f).equals(s))
    		return true;
    		else
    			return false;
    		
    	}
    
    // this method get file suffix
    private static String suffix(File f) {
    	String suffix = f.toString();
    	suffix = suffix.substring(suffix.length() - 3);
    	suffix = suffix.toUpperCase();
    	return suffix;
    }
    	
    
    // this method matches bytes sign from map
    private static boolean matchesSignature(int[] signature, byte[] buffer, int size) {
        if (size < signature.length) {
            return false;
        }

        boolean b = true;
        for (int i = 0; i < signature.length; i++) {
            if (signature[i] != (0x00ff & buffer[i])) {
                b = false;
                break;
            }
        }
        
        return b;
    }


    public static void main(String[] args) throws Exception {
    	/*
    	 * examples
    	 * test1.gif is a gif
    	 * test2.pdf is a pdf
    	 * test3.txt is a jpg
    	 * test4.jpg is a jpg
    	 */
    	
	MagicNumbers mg = new MagicNumbers();
	System.out.println("File type is: " + mg.getFile(new File(FILE_PATH + "test1.gif")));
    }
}
