package q.rorbin.fastimagesize.imp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import q.rorbin.fastimagesize.ImageSize;
import q.rorbin.fastimagesize.ImageType;
import q.rorbin.fastimagesize.util.ByteArrayUtil;

import static android.content.ContentValues.TAG;
import static q.rorbin.fastimagesize.imp.JpgImageSizeCopy.JpgMarker.APP0;
import static q.rorbin.fastimagesize.imp.JpgImageSizeCopy.JpgMarker.EXIF;
import static q.rorbin.fastimagesize.imp.JpgImageSizeCopy.JpgMarker.MARKER;
import static q.rorbin.fastimagesize.imp.JpgImageSizeCopy.JpgMarker.OTHER;
import static q.rorbin.fastimagesize.imp.JpgImageSizeCopy.JpgMarker.SOF0;

/**
 * Created by chqiu on 2016/10/26.
 */

public class JpgImageSizeCopy extends ImageSize {
    @Override
    public ImageType getSupportImageType() {
        return ImageType.JPG;
    }

    @Override
    public boolean isSupportImageType(byte[] buffer) {
        if (buffer == null || buffer.length <= 0)
            return false;
        //Jpg ：FF D8
        if (buffer.length >= 2) {
            return buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xD8;
        }
        return false;
    }
    @Override
    public int[] getImageSize(InputStream stream, byte[] buffer) throws IOException {
        int[] size = new int[2];
        if (buffer == null || buffer.length <= 0)
            return size;
        stream.skip(2);
        int endOfStream = 0;
        while (endOfStream != -1) {
            endOfStream = stream.read();
            byte bufferData = (byte) endOfStream;
            if (endOfStream != -1 && bufferData == (byte) 0xFF) {
                endOfStream = parserMarker(stream, size);
            }
        }
        return size;
    }


    private int parserMarker(InputStream stream, int[] size) throws IOException {
        int endOfStream = stream.read();
        byte bufferData = (byte) endOfStream;
        JpgMarker marker = getJpgMarker(bufferData);
        if (marker == SOF0) {
            stream.skip(3);
            byte[] buffer = new byte[4];
            stream.read(buffer);
            byte[] mergeHead = new byte[]{0, 0};
            byte[] byte1 = ByteArrayUtil.merge(mergeHead, ByteArrayUtil.cut(buffer, 0, 2));
            byte[] byte2 = ByteArrayUtil.merge(mergeHead, ByteArrayUtil.cut(buffer, 2, 2));
            size[1] = ByteBuffer.wrap(byte1).getInt();
            size[0] = ByteBuffer.wrap(byte2).getInt();
            endOfStream = -1;
        } else if (marker == MARKER) {
            endOfStream = parserMarker(stream, size);
        } else {
            byte[] markerLength = new byte[2];
            endOfStream = stream.read(markerLength);
            if (endOfStream != -1) {
                byte[] mergeHead = new byte[]{0, 0};
                int skipLength = ByteBuffer.wrap(ByteArrayUtil.merge(mergeHead, markerLength)).getInt();
                stream.skip(skipLength - 2);
            }
        }
        return endOfStream;
    }

    private JpgMarker getJpgMarker(byte bufferData) {
        switch (bufferData) {
            case (byte) 0xFF:
                return MARKER;
            case (byte) 0xE0:
                return APP0;
            case (byte) 0xE1:
                return EXIF;
            case (byte) 0xC0:
            case (byte) 0xC2:
            case (byte) 0xC3:
            case (byte) 0xC5:
            case (byte) 0xC7:
            case (byte) 0xC9:
            case (byte) 0xCB:
            case (byte) 0xCD:
            case (byte) 0xCF:
                return SOF0;
            default:
                return OTHER;
        }
    }

    enum JpgMarker {
        APP0, EXIF, SOF0, MARKER, OTHER
    }
}
