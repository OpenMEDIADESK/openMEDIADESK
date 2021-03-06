package com.stumpner.mediadesk.media.image.util;

import com.imagero.reader.ImageProducerAdapter;
import com.imagero.reader.jpeg.JpegReader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franzstumpner
 * Date: 28.04.2005
 * Time: 22:12:48
 * To change this template use File | Settings | File Templates.
 */
public class ImageroUtil implements IImageUtil {

    public void resizeImageHorizontal(String originalFileName, String resizeFileName, int width) {

        File f = new File(originalFileName);

        try {
            JpegReader decoder = new JpegReader(f,1);
            final ImageProducerAdapter producer = (ImageProducerAdapter) decoder.getProducer(0);

            //scale
            producer.setDimensions(width,width);
            

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void resizeImageVertical(String originalFileName, String resizeFileName, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void overlayWatermark(String originalFileName, String watermarkFile) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List getImageBulkData(String fileName) {
        return new LinkedList();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageSize(String fileName) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageWidth(String fileName) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageHeight(String fileName) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageDpi(String fileName) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean convertToJpeg(String originalFileName, String jpegFilename) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
