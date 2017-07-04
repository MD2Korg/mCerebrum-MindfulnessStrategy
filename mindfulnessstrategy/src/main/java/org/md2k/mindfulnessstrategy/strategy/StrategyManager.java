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
import java.util.Random;

public class StrategyManager {
    private ArrayList<Strategy> getValidStrategies(Strategy[] strategies)
    {
        ArrayList<Strategy> validStrategy=new ArrayList<>();
        for (Strategy strategy : strategies) {
            if (strategy.getRating() == 1.0) continue;
            validStrategy.add(strategy);
        }
        return validStrategy;
    }
    public Strategy getRandomStrategy(Strategy[] strategies){
        ArrayList<Strategy> validStrategies=getValidStrategies(strategies);
        Random random=new Random();
        return validStrategies.get(random.nextInt(validStrategies.size()));
    }
    public void setStrategy(Strategy[] strategies, Strategy strategy){
        for (Strategy strategy1 : strategies)
            if (strategy1.getId().equals(strategy.getId())) {
                strategy1.setRating(strategy.getRating());
                return;
            }
    }
}
