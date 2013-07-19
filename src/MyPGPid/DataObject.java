/*
 * DataObject.java - Data object class for JOpenPGPCard.
 *
 * Copyright (C) 2007  Sten Lindgren
 *
 * This file is part of OpenPGPCard.
 *
 *  JOpenPGPCard is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package MyPGPid;

import javacard.framework.*;

/**
 *
 * @author stelin
 */
public class DataObject {
    
    byte[] data;
    byte[] tag;
    short dataLength = 0;
    boolean staticLength = false;
    
    /** Creates a new instance of DataObject */
    public DataObject() {
    }
    
    public DataObject(byte p2, short length) {
        init((byte) 0, p2, length, false);
    }
    
    public DataObject(byte p1, byte p2, short length) {
        init(p1, p2, length, false);
    }
    
    public DataObject(byte p1, byte p2, short length, boolean full) {
        init(p1, p2, length, full);
    }
    
    private void init(byte p1, byte p2, short length) {
        init(p1, p2, length, false);
    }
    
    private void init(byte p1, byte p2, short length, boolean full) {
        
        data = new byte[length];
        if (p1 == 0) {
            tag = new byte[1];
            tag[0] = p2;
        } else {
            tag = new byte[2];
            tag[0] = p1;
            tag[1] = p2;
        }
        if (full) {
            dataLength = length;
            staticLength = true;
        }
    }
    
    
    public byte[] getData() {
        return data;
    }
    
    public short getData(byte[] buffer, short offset) {
        Util.arrayCopy(data, (short) 0, buffer, (short) offset, dataLength);
        return (short)(offset + dataLength);
    }
        
    public short getTlv(byte[] buffer, short offset) {
        short len = offset;
        
        Util.arrayCopy(tag, (short) 0, buffer, (short) len, (short) tag.length);
        len += (short) tag.length;
            if (dataLength < 0x80) {
                buffer[len++] = (byte) dataLength;
            } else {
                buffer[len++] = (byte) (0x81);
                buffer[len++] = (byte) dataLength;
            }
            Util.arrayCopy(data, (short) 0, buffer, (short) len, (short) dataLength);
            len += (short) dataLength;
        return len;
    }
    
    public short getDataLength() {
        return (short) dataLength;
    }
    
    public void setData(byte[] buffer, short len) {
        if (len > data.length) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }
        Util.arrayCopy(buffer, (short) 0, data, (short) 0, len);
        if (!staticLength) {
            dataLength = len;
        }
    }
    
    public void setData(byte[] buffer, short offset, short len) {
        if ((short)(len + offset) > data.length) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }
        Util.arrayCopy(buffer, (short) 0, data, offset, len);
        if (!staticLength) {
            dataLength = (short) (len + offset);
        }
    }
    
}
