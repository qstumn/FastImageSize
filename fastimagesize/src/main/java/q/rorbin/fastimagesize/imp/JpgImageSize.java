package q.rorbin.fastimagesize.imp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import q.rorbin.fastimagesize.ImageSize;
import q.rorbin.fastimagesize.ImageType;
import q.rorbin.fastimagesize.util.ByteArrayUtil;

/**
 * Created by chqiu on 2016/10/26.
 */

public class JpgImageSize extends ImageSize {
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
        LinkedList<Byte> bufferList = new LinkedList<>();
        for (byte aBuffer : buffer) {
            bufferList.add(aBuffer);
        }
        skip(stream, bufferList, 2);
        int endOfStream = 0;
        while (endOfStream != -1) {
            byte bufferData = readByte(stream, bufferList);
            endOfStream = bufferData & 0xFF;
            if (endOfStream != -1 && bufferData == (byte) 0xFF) {
                endOfStream = parserMarker(stream, bufferList, size);
            }
        }
        return size;
    }

    private int parserMarker(InputStream stream, LinkedList<Byte> bufferList,
                             int[] size) throws IOException {
        byte bufferData = readByte(stream, bufferList);
        int endOfStream = bufferData & 0xFF;
        JpgMarker marker = getJpgMarker(bufferData);
        if (marker == JpgMarker.SOF0) {
            skip(stream, bufferList, 3);
            byte[] buffer = new byte[4];
            stream.read(buffer, 0, buffer.length);
            byte[] mergeHead = new byte[]{0, 0};
            byte[] byte1 = ByteArrayUtil.merge(mergeHead, ByteArrayUtil.cut(buffer, 0, 2));
            byte[] byte2 = ByteArrayUtil.merge(mergeHead, ByteArrayUtil.cut(buffer, 2, 2));
            size[1] = ByteBuffer.wrap(byte1).getInt();
            size[0] = ByteBuffer.wrap(byte2).getInt();
            endOfStream = -1;
        } else if (marker == JpgMarker.MARKER) {
            endOfStream = parserMarker(stream, bufferList, size);
        } else {
            byte[] markerLength = readByteArray(stream, bufferList, 2);
            if (markerLength.length != 2) {
                endOfStream = -1;
            }
            if (endOfStream != -1) {
                byte[] mergeHead = new byte[]{0, 0};
                int skipLength = ByteBuffer.wrap(ByteArrayUtil.merge(mergeHead, markerLength)).getInt();
                skip(stream, bufferList, skipLength - 2);
            }
        }
        return endOfStream;
    }

    private void skip(InputStream stream, LinkedList<Byte> bufferList, int skipLength) throws IOException {
        if (bufferList.isEmpty()) {
            stream.skip(skipLength);
        } else {
            if (skipLength > bufferList.size()) {
                stream.skip(skipLength - bufferList.size());
                bufferList.clear();
            } else if (skipLength == bufferList.size()) {
                bufferList.clear();
            } else {
                for (int i = 0; i < skipLength; i++) {
                    bufferList.remove();
                }
            }
        }

    }

    private byte[] readByteArray(InputStream stream, LinkedList<Byte> bufferList, int count) throws IOException {
        byte[] bufferDataArray = new byte[count];
        for (int i = 0; i < count; i++) {
            byte bufferData = readByte(stream, bufferList);
            int endOfStream = bufferData & 0xFF;
            if (endOfStream != -1) {
                bufferDataArray[i] = bufferData;
            } else {
                bufferDataArray = ByteArrayUtil.cut(bufferDataArray, 0, i);
                break;
            }
        }
        return bufferDataArray;
    }

    private byte readByte(InputStream stream, LinkedList<Byte> bufferList) throws IOException {
        byte bufferData;
        if (bufferList.isEmpty()) {
            bufferData = (byte) stream.read();
        } else {
            bufferData = bufferList.remove();
        }
        return bufferData;
    }

    private JpgMarker getJpgMarker(byte bufferData) {
        switch (bufferData) {
            case (byte) 0xFF:
                return JpgMarker.MARKER;
            case (byte) 0xE0:
                return JpgMarker.APP0;
            case (byte) 0xE1:
                return JpgMarker.EXIF;
            case (byte) 0xC0:
            case (byte) 0xC2:
            case (byte) 0xC3:
            case (byte) 0xC5:
            case (byte) 0xC7:
            case (byte) 0xC9:
            case (byte) 0xCB:
            case (byte) 0xCD:
            case (byte) 0xCF:
                return JpgMarker.SOF0;
        }
        return JpgMarker.OTHER;
    }

    enum JpgMarker {
        APP0, EXIF, SOF0, MARKER, OTHER
    }
}
