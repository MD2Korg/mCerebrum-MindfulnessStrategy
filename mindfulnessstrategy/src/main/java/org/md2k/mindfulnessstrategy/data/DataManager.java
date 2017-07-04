package org.md2k.mindfulnessstrategy.data;
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

import org.md2k.datakitapi.time.DateTime;

import java.util.ArrayList;

public class DataManager {
    private ArrayList<Data> data;
    private String trigger_type;
    private String day_type;
    private String lapse_type;
    public DataManager(String trigger_type, boolean isPreQuit, boolean isLapse){
        this.trigger_type=trigger_type;
        if(isPreQuit) day_type="PRE_QUIT";
        else day_type="POST_QUIT";
        if(isLapse)
            lapse_type="LAPSE";
        else lapse_type="NO_LAPSE";
        data=new ArrayList<>();
    }
    public void add(String dataType, String message){
        Data data=new Data();
        data.data_type=dataType;
        data.timestamp= DateTime.getDateTime();
        data.message=message;
        this.data.add(data);
    }
    public void add(String dataType, String category, String strategy){
        Data data=new Data();
        data.timestamp= DateTime.getDateTime();
        data.category=category;
        data.strategy=strategy;
        data.data_type=dataType;
        this.data.add(data);
    }
    public void add(String dataType, String category, String strategy, double rating){
        Data data=new Data();
        data.timestamp= DateTime.getDateTime();
        data.category=category;
        data.strategy=strategy;
        data.data_type=dataType;
        data.rating=rating;
        this.data.add(data);
    }

}
