package org.md2k.mindfulnessstrategy.datakit;
/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.datatype.DataType;
import org.md2k.datakitapi.datatype.DataTypeJSONObject;
import org.md2k.datakitapi.datatype.DataTypeLong;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.datakitapi.source.datasource.DataSource;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceClient;
import org.md2k.datakitapi.source.datasource.DataSourceType;
import org.md2k.datakitapi.time.DateTime;
import org.md2k.mindfulnessstrategy.data.DataManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataKitManager {
    private DataKitAPI dataKitAPI;
    private Context context;
    private DataSourceDesc dataSourceDesc;
    public DataKitManager(Context context){
        this.context=context;
        dataKitAPI=DataKitAPI.getInstance(context);
        dataSourceDesc=read();
    }
    public boolean isPreQuit() throws DataKitException {
        DataSourceBuilder dataSourceBuilder=new DataSourceBuilder().setType(DataSourceType.POST_QUIT);
        ArrayList<DataSourceClient> dataSourceClient = dataKitAPI.find(dataSourceBuilder);
        if(dataSourceClient.size()==0) return true;
        ArrayList<DataType> dataType=dataKitAPI.query(dataSourceClient.get(0),1);
        if(dataType.size()==0) return true;
        DataTypeLong dataTypeLong= (DataTypeLong) dataType.get(0);
        return dataTypeLong.getSample() > DateTime.getDateTime();
    }
    public void connect(OnConnectionListener onConnectionListener) throws DataKitException {
        dataKitAPI.connect(onConnectionListener);
    }

    public boolean isLapse() throws DataKitException {
        if(isPreQuit()) return false;
        DataSourceBuilder dataSourceBuilder=new DataSourceBuilder().setType(DataSourceType.POST_QUIT);
        ArrayList<DataSourceClient> dataSourceClient = dataKitAPI.find(dataSourceBuilder);
        ArrayList<DataType> dataType=dataKitAPI.query(dataSourceClient.get(0),1);
        DataTypeLong dataTypeLong= (DataTypeLong) dataType.get(0);
        long startTime=dataTypeLong.getSample();
        long endTime=DateTime.getDateTime();
        if(endTime-startTime>=48*60*60*1000L) startTime = endTime-48*60*60*1000L;
        dataSourceBuilder=new DataSourceBuilder().setType("PUFFMARKER_SMOKING_EPISODE");
        dataSourceClient = dataKitAPI.find(dataSourceBuilder);
        if(dataSourceClient.size()==0) return false;
        dataType = dataKitAPI.query(dataSourceClient.get(0), startTime, endTime);
        return dataType.size() != 0;
    }

    public void save(String id, DataManager dataManager) throws DataKitException {
        DataSource dataSource=getDataSource(id);
        if(id!=null) {
            DataSourceClient dataSourceClient = dataKitAPI.register(new DataSourceBuilder(dataSource));
            Gson gson=new Gson();
            String data = gson.toJson(dataManager);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject= (JsonObject) jsonParser.parse(data);
            DataTypeJSONObject dataTypeJsonObject=new DataTypeJSONObject(DateTime.getDateTime(), jsonObject);
            dataKitAPI.insert(dataSourceClient, dataTypeJsonObject);
        }
    }
    private DataSourceDesc read() {
        BufferedReader reader = null;
        DataSourceDesc dataSourceDesc=null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("datasource.json")));
            Gson gson=new Gson();
            dataSourceDesc = gson.fromJson(reader, DataSourceDesc.class);
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return dataSourceDesc;
    }
    private DataSource getDataSource(String id){
        for(int i=0;i<dataSourceDesc.list.length;i++){
            if(dataSourceDesc.list[i].getId().equals(id))
                return dataSourceDesc.list[i];
        }
        return null;
    }
    public void disconnect(){
        dataKitAPI.disconnect();
    }

    public void registerAll() throws DataKitException {
        for(int i=0;i<dataSourceDesc.list.length;i++){
            dataKitAPI.register(new DataSourceBuilder(dataSourceDesc.list[i]));
        }
    }
}
