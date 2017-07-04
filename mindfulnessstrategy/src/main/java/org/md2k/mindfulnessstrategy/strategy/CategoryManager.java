package org.md2k.mindfulnessstrategy.strategy;
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
import android.os.Environment;

import com.google.gson.Gson;

import org.md2k.utilities.FileManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class CategoryManager {
    private static final String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mCerebrum/org.md2k.mindfulnessstrategy/";
    private static final String FILENAME = "strategy.json";

    private Categories categories;

    public CategoryManager(Context context) throws IOException {
        read(context);
    }
    public Category getCategoryRandom(String type, boolean isPreQuit, boolean isLapse){
        ArrayList<Category> c = categories.getCategory(type, isPreQuit, isLapse);
        Random random=new Random();
        return c.get(random.nextInt(c.size()));
    }
    public Category getCategoryRandom(String type, boolean isPreQuit, boolean isLapse, Category categoryNotUsed){
        ArrayList<Category> c = categories.getCategory(type, isPreQuit, isLapse);
        for(int i=0;i<c.size();i++)
            if(c.get(i).getId().equals(categoryNotUsed.getId())) {
                c.remove(i);
                break;
            }

        Random random=new Random();
        return c.get(random.nextInt(c.size()));
    }
    private Categories readDefault(Context context){
        Categories categories=null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("strategy.json")));
            Gson gson=new Gson();
            categories = gson.fromJson(reader, Categories.class);
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return categories;
    }
    private void read(Context context) throws IOException {
        try {
            categories =  FileManager.readJSON(DIRECTORY, FILENAME, Categories.class);
            if(categories==null) throw new FileNotFoundException();
        } catch (FileNotFoundException e) {
            categories = readDefault(context);
            FileManager.writeJSON(DIRECTORY, FILENAME, categories);
        }
    }
    public void write() throws IOException {

        FileManager.writeJSON(DIRECTORY, FILENAME, categories);
    }
}
