package m09uf1.prob.hacking;

public class HackedSystem {

	public static final String MSG1 = 
    		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEFlDw2Oh+CNL9OCWGZGbXr5xGZco"
			+ "MH1g2yb/d1ycyBCvctJPnrslUFgTJTBkaMy9C3dIgvtwt5X2sC1IcPVcpkITmpgiT"
			+ "eIuiw5SQz1CVoO8kuU+3yrrybxCkXOdyeHxHZRAWwnRJH/iR7WttmkDMOeFwAkV4V"
			+ "r+5ApZIpW6UlwIDAQAB"; // rsa public   
    public static final String MSG2 = // aquest missatge es diferent cada cop 
            "Of8U74KkA17e+zOnkFzrRcdv2b6drZzIm7MBXNrUtlX/Ccd9Kh73Anxyzu8r5yaEZ5O"
            + "Bf1L43yngF0B1XzsxFronVlBkn3lwiu0OVqykOuJdPjDaddJ5U5LEdc/h5Mlwvg6j"
            + "Go1kzsWsK0UtzsLEPnfTvo0rGGgcnb6iiXScncw=";
    public static final String MSG3 = 
    		"3dz3uzGNIuqA2orXds3Npz7bg8snXnz+ayE6M/90nsIdI2i9NUQqn4cpxwiA6XxJLch"
    		+ "UP4t15RiRunvaZrgITpQgSeecFsx0HsnPV3K35z0lnorPKEoddDH/ljb/51IDqve3"
    		+ "Fx9ytk6oCWGg22C2mg==";

    public static final String RSA_PRIVATE = 
			"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIQWUPDY6H4I0v04JYZ"
			+ "kZtevnEZlygwfWDbJv93XJzIEK9y0k+euyVQWBMlMGRozL0Ld0iC+3C3lfawLUhw9"
			+ "VymQhOamCJN4i6LDlJDPUJWg7yS5T7fKuvJvEKRc53J4fEdlEBbCdEkf+JHta22aQ"
			+ "Mw54XACRXhWv7kClkilbpSXAgMBAAECgYAUKaPpX/L9ajNq2+s+fI0GV5QvWtJgz1"
			+ "gT5PAY2QLuq5xWcGozb4bkAwhwAeHDEhUgy+OLhBbwGpv4azH5hZOMWBb6bcKKm+V"
			+ "uAjgBz3coUeSTtJI5NmbqcrqTQXGvLe41r/Z5YStKWJU5vGk8H7oo372Q3yNcdRX7"
			+ "vG3X2IbmIQJBANG3arTrWFTX0q500KgdJjm+kBl5jc8FkyKOzbtEeyilGgUGslyGF"
			+ "YZkVhujKQbLRMZkKg+DnBZrCfn36lTXFuUCQQChPP222NiBXCUMrdMALWbHLfsOb/"
			+ "bJptDF4PtGg1I3cYtjNKfcplugJBdWd0nFjn1E2oG1O8fEnlx4dOftVenLAkBbQoz"
			+ "N1AgpS43j2vOd5KlowXNlko7SQuPHSwtd7awGgOo0u7hMKREe3XTXLAZDbZstFhNz"
			+ "ktsOql+6BldTuhG9AkBReBnSO7sZkc9+mo3UtxwJZChIYTZXKZyvf0A3nr76GLsPQ"
			+ "1nV2ZDOV64bGGcEFT0ify2uvyfvJv0eOCO//fsFAkBxeAU0neJBGqTv42SSWCp1+P"
			+ "8TJqiRnb8CxGd4yDIWtDzNosP4hZeO7zJkyk2ZJBSAKUrHj7psFaNhNDgdZ2fQ";    
    
    /*
     * MSG1: de B a A, la clau publica de B (RSA 1024)
     * MSG2: de A a B, la clau secreta (AES 128) xifrada amb la publica de B
     * MSG3: de B a A, un missatge (UTF-8) xifrat amb la clau secreta (AES 128)
     * 
     * A mes, he trobat a una carpeta la clau privada de B!
     * Podria provar les transformacions mes obvies:
     * RSA/ECB/PKCS1Padding i AES/ECB/PKCS5Padding.
     * 
     * Que diu el missatge?
     * Passos:
     * 1) Obtenir la clau secreta (desxifrar MSG2 amb la clau privada)
     * 2) Desxifrar el missatge (UTF8) amb la clau secreta (MSG3)
     */
    
    
}

