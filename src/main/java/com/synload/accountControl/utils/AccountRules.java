package com.synload.accountControl.utils;

import com.synload.accountControl.request.AccountRequest;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

/**
 * Created by Nathaniel on 7/23/2017.
 */
public class AccountRules {
    public static boolean user(String user){
        if(user==null){
            return false;
        }
        if(user.length()<=5){
            return false;
        }
        return true;
    }
    public static boolean password(String password){
        if(password==null){
            return false;
        }
        if(password.length()<=5){
            return false;
        }
        return true;
    }
    public static String hash(String plaintext){
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(plaintext.getBytes());
        return Hex.toHexString(digest);
    }
}
