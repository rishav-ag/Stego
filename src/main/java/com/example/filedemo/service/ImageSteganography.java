package com.example.filedemo.service;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.property.FileStorageProperties;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class ImageSteganography {
    
    private String input1;
    private String input2;
    private String output;

    public ImageSteganography() {

        String coverFile = "spring-boot-file-upload-download-rest-api-example/src/main/resources/cover-file/";
        String secretFile = "spring-boot-file-upload-download-rest-api-example/src/main/resources/secret-file/";

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        nu.pattern.OpenCV.loadLocally();
        File coverFolder = new File("spring-boot-file-upload-download-rest-api-example/src/main/resources/cover-file/");
        File secretFolder = new File("spring-boot-file-upload-download-rest-api-example/src/main/resources/secret-file/");
        if(coverFolder.listFiles().length != 0) {
            input1 = coverFile + coverFolder.listFiles()[0].getName();
        }
        if(secretFolder.listFiles().length != 0) {
            input2 = secretFile + secretFolder.listFiles()[0].getName();
        }
        output = "spring-boot-file-upload-download-rest-api-example/src/main/resources/encrypted-file/encrypted-file.png";
    }

    public String getCoverFileLocation() {
        return this.input1;
    }

    public String getSecretFileLocation() {
        return this.input2;
    }

    public String getEncryptedFileLocation() {
        return this.output;
    }

    public void encode(String input1, String input2, String output) {
        Imgcodecs image1 = new Imgcodecs();
        Imgcodecs image2 = new Imgcodecs();
        Mat mat1 = Imgcodecs.imread(input1);
        Mat mat2 = Imgcodecs.imread(input2);

        for(int i=0; i<mat1.rows(); i++) {
            for(int j=0; j<mat1.cols(); j++) {
                double[] data2 = mat2.get(i, j);
                double[] data1 = mat1.get(i, j);
                for(int k=0; k<3; k++) {
                    int temp1 = (int)(data1[k]);
                    int temp2 = (int)(data2[k]);
                    //System.out.println(i + " " + j + " " + k + " " + temp1 + " " + temp2);
                    String s1 = Integer.toBinaryString(temp1);
                    String s2 = Integer.toBinaryString(temp2);
                    s1 = ("00000000" + s1).substring(s1.length());
                    s2 = ("00000000" + s2).substring(s2.length());

                    String s = s1.substring(0, 4) + s2.substring(0, 4);
                    data1[k] = (double)(Integer.parseInt(s, 2));
                    //System.out.println(i + " " + j + " " + k + " " + data1[k]);
                }
                mat1.put(i, j, data1);
            }
        }
        Imgcodecs.imwrite(output, mat1);
    }

    public void decode(String path) {
        Imgcodecs image = new Imgcodecs();
        Imgcodecs image1 = new Imgcodecs();
        Imgcodecs image2 = new Imgcodecs();
        Mat mat = image.imread(path);
        Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_64FC3);
        Mat mat2 = new Mat(mat.rows(), mat.cols(), CvType.CV_64FC3);

        for(int i=0; i<mat.rows(); i++) {
            for(int j=0; j<mat.cols(); j++) {
                double[] data = mat.get(i, j);
                double[] data1 = new double[3];
                double[] data2 = new double[3];
                for(int k=0; k<3; k++) {
                    int temp = (int)(data[k]);
                    String s = Integer.toBinaryString(temp);
                    s = ("00000000" + s).substring(s.length());
                    //System.out.println(i + " " + j + " " + k + " " + s);
                    String s1 = s.substring(0, 4) + returnRandom();
                    String s2 = s.substring(4) + returnRandom();
                    //System.out.println(i + " " + j + " " + k + " " + s1 + " " + s2);
                    data1[k] = (double)(Integer.parseInt(s1, 2));
                    data2[k] = (double)(Integer.parseInt(s2, 2));
                    //System.out.println(data1[k] + " " + data2[k]);
                }
                mat1.put(i, j, data1);
                mat2.put(i, j, data2);
            }
        }
        Imgcodecs.imwrite(input1 + "cover.png", mat1);
        Imgcodecs.imwrite(input2 + "secret.png", mat2);
        
    }
    public String returnRandom() {
        Random random = new Random();
        String ch = String.valueOf((char)(random.nextInt(2)+48));
        return String.valueOf(ch+ch+ch+ch);
    }
}