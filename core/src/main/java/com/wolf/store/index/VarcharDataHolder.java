package com.wolf.store.index;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public class VarcharDataHolder extends DataHolder<VarcharDataHolder> {

    private String key;

    private String charset = "UTF-8";

    public VarcharDataHolder(String data){
        this.key =data;
    }


    @Override DataHolder fromRow(String indexData) {
        return new VarcharDataHolder(indexData);
    }

    @Override public int compareTo(VarcharDataHolder another) {
        return this.key.compareTo(another.getKey());
    }

    @Override public int length() {
        return key.length();
    }

    @Override public void serialize(ByteBuffer byteBuffer) {
        byte [] data = new byte[0];
        try {
            data = key.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //todo
        }
        byteBuffer.putInt(data.length);
        byteBuffer.put(data);
    }

    @Override public VarcharDataHolder deSerialize(ByteBuffer byteBuffer) {
        int length =byteBuffer.getInt();
        byte [] data =new byte[length];
        return new VarcharDataHolder(new String(data, Charset.forName(charset)));
    }
}
