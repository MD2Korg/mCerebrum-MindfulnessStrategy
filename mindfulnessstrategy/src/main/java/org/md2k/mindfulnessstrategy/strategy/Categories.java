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

import java.util.ArrayList;

class Categories {
    private static final String TYPE_STRESS_LOW = "STRESS_LOW";
    private static final String TYPE_STRESS_HIGH = "STRESS_HIGH";
    private static final String TYPE_SMOKING = "SMOKING";
    private static final String TYPE_NOT_SMOKING = "NOT_SMOKING";
    private static final String TYPE_USER="USER";

    private static final String B = "breath";
    private static final String T = "thoughts";
    private static final String S = "sensations";
    private static final String A = "acceptance";
    private static final String C_PR = "craving_pre";
    private static final String C_PO = "craving_post";
    private static final String M_PR = "motivational_pre";
    private static final String M_PO = "motivational_post";
    private static final String L_PO = "lapse_postquit";
    private Category[] categories;

    private String[] getCategoriesSmoking(boolean isPreQuit, boolean isLapse) {
        if (isPreQuit) return null;
        if (isLapse) return new String[]{L_PO};
        else return new String[]{B, T, S, A, C_PO, M_PO};
    }

    private String[] getCategoriesStress(boolean isPreQuit) {
        if (isPreQuit)
            return new String[]{B, T, S, A, C_PR, M_PR};
        else return new String[]{B, T, S, A, C_PO, M_PO};
    }
    private String[] getCategoriesDefault(boolean isPreQuit) {
        if (isPreQuit)
            return new String[]{B, T, S, A, C_PR, M_PR};
        else return new String[]{B, T, S, A, C_PO, M_PO};
    }
    private String[] getCategoriesUser(boolean isPreQuit) {
        if (isPreQuit)
            return new String[]{B, T, S, A, C_PR};
        else return new String[]{B, T, S, A, C_PO};
    }

    ArrayList<Category> getCategory(String type, boolean isPreQuit, boolean isLapse){
        String[] categories;
        ArrayList<Category> result=new ArrayList<>();
        if(type==null)
            categories=getCategoriesDefault(isPreQuit);
        else if (TYPE_STRESS_HIGH.equals(type) || TYPE_STRESS_LOW.equals(type)) {
            categories = getCategoriesStress(isPreQuit);
        } else if (TYPE_SMOKING.equals(type) || TYPE_NOT_SMOKING.equals(type)) {
            categories = getCategoriesSmoking(isPreQuit, isLapse);
        } else  if(TYPE_USER.equals(type)){
            categories = getCategoriesUser(isPreQuit);
        }else
            categories = getCategoriesDefault(isPreQuit);
        for(int i = 0; categories!=null && i<categories.length; i++){
            for (Category category : this.categories)
                if (categories[i].equals(category.getId())) {
                    result.add(category);
                    break;
                }
        }
        return result;
    }
}
